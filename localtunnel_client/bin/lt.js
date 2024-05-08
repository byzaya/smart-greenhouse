#!/usr/bin/env node
/* eslint-disable no-console */

const openurl = require('openurl');
const yargs = require('yargs');

const localtunnel = require('../localtunnel');
const { version } = require('../package');

const { argv } = yargs
  .usage('Usage: lt --port [num] <options>')
  .env(true)
  .option('p', {
    alias: 'port',
    describe: 'Internal HTTP server port',
  })
  .option('h', {
    alias: 'host',
    describe: 'Upstream server providing forwarding',
    default: 'https://localtunnel.me/',
  })
  .option('s', {
    alias: 'subdomain',
    describe: 'Request this subdomain',
  })
  .option('a', {
    alias: 'local-address',
    describe: 'Local http(s) server address',
  })
  .option('l', {
    alias: 'local-host',
    describe: 'Tunnel traffic to this host instead of localhost, override Host header to this host',
  })
  .option('local-https', {
    describe: 'Tunnel traffic to a local HTTPS server',
  })
  .option('local-cert', {
    describe: 'Path to certificate PEM file for local HTTPS server',
  })
  .option('local-key', {
    describe: 'Path to certificate key file for local HTTPS server',
  })
  .option('local-ca', {
    describe: 'Path to certificate authority file for self-signed certificates',
  })
  .option('allow-invalid-cert', {
    describe: 'Disable certificate checks for your local HTTPS server (ignore cert/key/ca options)',
  })
  .option('jwt', {
    describe: 'Client JSON Web Tokens (JWT) filename',
  })
  .options('o', {
    alias: 'open',
    describe: 'Opens the tunnel URL in your browser',
  })
  .option('not-print-requests', {
    describe: 'Do not print basic request info',
  })
  .require('port')
  .boolean('local-https')
  .boolean('allow-invalid-cert')
  .boolean('not-print-requests')
  .help('help', 'Show this help and exit')
  .version(version);

if (typeof argv.port !== 'number') {
  yargs.showHelp();
  console.error('\nInvalid argument: `port` must be a number');
  process.exit(1);
}

(async () => {
  const tunnel = await localtunnel({
    port: argv.port,
    host: argv.host,
    subdomain: argv.subdomain,
    remote_ip: argv.remoteIp,
    local_host: argv.localHost,
    local_address: argv.localAddress,
    local_https: argv.localHttps,
    local_cert: argv.localCert,
    local_key: argv.localKey,
    local_ca: argv.localCa,
    jwt: argv.jwt,
    allow_invalid_cert: argv.allowInvalidCert,
  }).catch(err => {
    throw err;
  });

  tunnel.on('error', err => {
    throw err;
  });

  const url = tunnel.url.split('/').pop();
  console.log('Your url is: \x1b[36m%s\x1b[0m', 'http://' + url);
  console.log('Your secure url is: \x1b[36m%s\x1b[0m', 'https://' + url);

  /**
   * `cachedUrl` is set when using a proxy server that support resource caching.
   * This URL generally remains available after the tunnel itself has closed.
   * @see https://github.com/localtunnel/localtunnel/pull/319#discussion_r319846289
   */
  if (tunnel.cachedUrl) {
    console.log('Your cachedUrl is: %s', tunnel.cachedUrl);
  }

  if (argv.open) {
    openurl.open(tunnel.url);
  }

  if (!argv['not-print-requests']) {
    console.log('\nRequests:');
    tunnel.on('request', info => {
      const date = new Date(new Date().getTime()-(new Date().getTimezoneOffset()*60000))
        .toISOString()
        .slice(0, 19)
        .replace('T', ' ');
      console.log('\x1b[32m%s\x1b[0m %s %s', date, info.method, info.path);
    });
  }
})();
