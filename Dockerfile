# Etapa de construcción
FROM openjdk:8-jdk-alpine AS builder

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar los archivos del proyecto al contenedor
COPY . .

# Compilar la aplicación con Maven (asegúrate de tener mvnw en tu proyecto)
RUN chmod +x ./mvnw && ./mvnw clean package -DskipTests

# Etapa de ejecución
FROM gcr.io/distroless/java:8

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el JAR generado desde la etapa de construcción
COPY --from=builder /app/target/*.jar app.jar

# Exponer el puerto 8080 (solo informativo, Distroless no soporta EXPOSE directamente)
EXPOSE 8080

# Definir el comando de inicio
ENTRYPOINT ["java", "-Xmx256m", "-Xms128m", "-Xss256k", "-XX:MaxMetaspaceSize=128m", "-XX:+UseG1GC", "-XX:+UseStringDeduplication", "-jar", "/app/app.jar"]