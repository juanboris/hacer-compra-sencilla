#!/bin/sh

# Arrancar Tailscale en background
tailscaled --tun=userspace-networking --socks5-server=localhost:1055 &

# Esperar a que tailscaled esté listo
sleep 2

# Conectar a la red Tailscale
tailscale up --authkey=${TAILSCALE_AUTH_KEY} --accept-routes

# Esperar a que la conexión esté establecida
sleep 3

# Arrancar Spring Boot
exec java -Xmx256m -Xms128m -Xss256k \
  -XX:MaxMetaspaceSize=96m \
  -XX:+UseSerialGC \
  -XX:MaxHeapFreeRatio=30 \
  -XX:MinHeapFreeRatio=10 \
  -DsocksProxyHost=localhost \
  -DsocksProxyPort=1055 \
  -jar /app/app.jar