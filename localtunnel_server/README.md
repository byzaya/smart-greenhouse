# localtunnel

## Установка

Для начала просто клонировать репозиторий на наш сервер и поменять настройки env под себя.
Также необходимо правильно настроить DNS-записи, указать по примеру:
```
A example.com (ip сервера)
A *.example.com (ip сервера)
```

После чего необходимо выпустить SSL сертификат для двух записей (example.com, *.example.com), один на двоих.
Выпущенные сертификаты надо положить в папку ./ssl с названиями server.crt и server.key, где:
```
fullchain.pem -> server.crt
privkey.pem -> server.key
```

Далее просто используем:
```
docker-compose build
docker-compose up -d
```
