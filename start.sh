#!/bin/sh

tailscaled --tun=userspace-networking --socks5-server=localhost:1055 &
sleep 2

tailscale up \
  --auth-key="${TAILSCALE_AUTH_KEY}?preauthorized=true" \
  --advertise-tags=tag:render \
  --accept-routes || { echo "ERROR: tailscale up ha fallado"; exit 1; }

sleep 3

exec java -Xmx256m -Xms128m -Xss256k \
  -XX:MaxMetaspaceSize=96m \
  -XX:+UseSerialGC \
  -XX:MaxHeapFreeRatio=30 \
  -XX:MinHeapFreeRatio=10 \
  -DsocksProxyHost=localhost \
  -DsocksProxyPort=1055 \
  -jar /app/app.jar