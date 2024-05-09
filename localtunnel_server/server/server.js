import log from 'book';
import Koa from 'koa';
import tldjs from 'tldjs';
import Debug from 'debug';
import http from 'http';
import { hri } from 'human-readable-ids';
import Router from 'koa-router';
import jwt from'koa-jwt';

import ClientManager from './lib/ClientManager';

const debug = Debug('localtunnel:server');

function addJwtMiddleware(app, opt) {
    app.use(jwt({
        secret: opt.jwt_shared_secret
    }));
}

export default function(opt) {
    opt = opt || {};

    const validHosts = (opt.domain) ? [opt.domain] : undefined;
    const myTldjs = tldjs.fromUserSettings({ validHosts });
    const landingPage = process.env.REDIRECT_URL || 'https://localtunnel.github.io/www/';

    function GetClientIdFromHostname(hostname) {
        return myTldjs.getSubdomain(hostname);
    }

    const manager = new ClientManager(opt);

    const schema = opt.secure ? 'https' : 'http';

    const app = new Koa();
    const router = new Router();

    if (opt.jwt_shared_secret){
        addJwtMiddleware(app, opt);
    }

    router.get('/api/status', async (ctx, next) => {
        const stats = manager.stats;
        
        const clients = Object.keys(manager.clients).reduce((sort, el) => {
            if (!sort[manager.clients[el].ip])
                sort[manager.clients[el].ip] = []
            
            sort[manager.clients[el].ip].push(manager.clients[el].id)
            return sort
        }, {})
        
        ctx.body = {
            clients,
            tunnels: stats.tunnels,
            mem: process.memoryUsage(),
        };
    });

    router.post('/api/tunnels/:id/close', async (ctx, next) => {
        const clientId = ctx.params.id;
        const client = manager.getClient(clientId);
        if (!client) {
            ctx.status = 404;
            ctx.body = { error: 'Tunnel not found' };
            return;
        }
    
        client.close();
        
        ctx.status = 200;
        ctx.body = { success: `Tunnel ${clientId} closed` };
    });


    router.get('/api/tunnels/test', async (ctx, next) => {
        ctx.status = 200;
        ctx.body = { success: `Tunnel closed` };
    });

    router.get('/api/tunnels/:id/status', async (ctx, next) => {
        const clientId = ctx.params.id;
        const client = manager.getClient(clientId);
        if (!client) {
            ctx.throw(404);
            return;
        }

        const stats = client.stats();
        ctx.body = {
            connected_sockets: stats.connectedSockets,
        };
    });

    router.get('/api/tunnels/:id/kill', async (ctx, next) => {
        const clientId = ctx.params.id;
        if (!opt.jwt_shared_secret){
          debug('disconnecting client with id %s, error: jwt_shared_secret is not used', clientId);
          ctx.throw(403, {
            success: false,
            message: 'jwt_shared_secret is not used'
          });
          return;
        }

        if (!manager.hasClient(clientId)) {
          debug('disconnecting client with id %s, error: client is not connected', clientId);
          ctx.throw(404, {
            success: false,
            message: `client with id ${clientId} is not connected`
          });
        }

        const securityToken = ctx.request.headers.authorization;
        if (!manager.getClient(clientId).isSecurityTokenEqual(securityToken)) {
          debug('disconnecting client with id %s, error: securityToken is not equal ', clientId);
          ctx.throw(403, {
            success: false,
            message: `client with id ${clientId} has not the same securityToken than ${securityToken}`
          });
        }

        debug('disconnecting client with id %s', clientId);
        manager.removeClient(clientId);

      ctx.statusCode = 200;
      ctx.body = {
        success: true,
        message: `client with id ${clientId} is disconected`
      };
    });

    app.use(router.routes());
    app.use(router.allowedMethods());

    // root endpoint
    app.use(async (ctx, next) => {
        const path = ctx.request.path;

        // skip anything not on the root path
        if (path !== '/') {
            await next();
            return;
        }

        const isNewClientRequest = ctx.query['new'] !== undefined;
        if (isNewClientRequest) {
            const reqId = hri.random();
            debug('making new client with id %s', reqId);
            const ip = ctx.request.header['x-real-ip'] || ctx.request.header['x-forwarded-for'];
            const info = await manager.newClient(reqId, ip, opt.jwt_shared_secret ? ctx.request.headers.authorization : null);

            const url = schema + '://' + info.id + '.' + ctx.request.host;
            info.url = url;
            ctx.body = info;
            return;
        }

        // no new client request, send to landing page
        ctx.redirect(landingPage);
    });

    // anything after the / path is a request for a specific client name
    // This is a backwards compat feature
    app.use(async (ctx, next) => {
        const parts = ctx.request.path.split('/');

        // any request with several layers of paths is not allowed
        // rejects /foo/bar
        // allow /foo
        if (parts.length !== 2) {
            await next();
            return;
        }

        const reqId = parts[1];

        if (process.env.BLACKLIST) {
            const blacklist = process.env.BLACKLIST.split(',');
            if (blacklist.includes(reqId))
            {  
                ctx.status = 403;
                ctx.body = {
                    message: `Invalid subdomain. Subdomain '${reqId}' reserved.`,
                };
                return;
            }
        }

        // limit requested hostnames to 63 characters
        if (! /^(?:[a-z0-9][a-z0-9\-]{4,63}[a-z0-9]|[a-z0-9]{4,63})$/.test(reqId)) {
            const msg = 'Invalid subdomain. Subdomains must be lowercase and between 4 and 63 alphanumeric characters.';
            ctx.status = 403;
            ctx.body = {
                message: msg,
            };
            return;
        }

        debug('making new client with id %s', reqId);
        const ip = ctx.request.header['x-real-ip'] || ctx.request.header['x-forwarded-for'];
        const info = await manager.newClient(reqId, ip, opt.jwt_shared_secret ? ctx.request.headers.authorization : null);

        const url = schema + '://' + info.id + '.' + ctx.request.host;
        info.url = url;
        ctx.body = info;
        return;
    });

    const server = http.createServer();

    const appCallback = app.callback();

    server.on('request', (req, res) => {
        // without a hostname, we won't know who the request is for
        const hostname = req.headers.host;
        if (!hostname) {
            res.statusCode = 400;
            res.end('Host header is required');
            return;
        }

        const clientId = GetClientIdFromHostname(hostname);
        if (!clientId) {
            appCallback(req, res);
            return;
        }

        const client = manager.getClient(clientId);
        if (!client) {
            res.writeHead(302,
                { Location: landingPage }
            );
            res.end();
            return;
        }

        client.handleRequest(req, res);
    });

    server.on('upgrade', (req, socket, head) => {
        const hostname = req.headers.host;
        if (!hostname) {
            socket.destroy();
            return;
        }

        const clientId = GetClientIdFromHostname(hostname);
        if (!clientId) {
            socket.destroy();
            return;
        }

        const client = manager.getClient(clientId);
        if (!client) {
            socket.destroy();
            return;
        }

        client.handleUpgrade(req, socket);
    });

    return server;
};
