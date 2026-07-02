# Etapa de construcción
FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /app
COPY . .
RUN chmod +x ./mvnw && ./mvnw clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Instalar Tailscale
RUN apt-get update && apt-get install -y curl iptables && \
    curl -fsSL https://tailscale.com/install.sh | sh && \
    rm -rf /var/lib/apt/lists/*

# Copiar el JAR
COPY --from=builder /app/target/*.jar app.jar

# Copiar el script de arranque
COPY start.sh start.sh
RUN chmod +x start.sh

EXPOSE 8080

ENTRYPOINT ["/app/start.sh"]