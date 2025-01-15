# Etapa de construcción
FROM openjdk:8-jdk-alpine AS builder

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar los archivos del proyecto al contenedor
COPY . .

# Compilar la aplicación con Maven (asegúrate de tener mvnw en tu proyecto)
RUN chmod +x ./mvnw && ./mvnw clean package -DskipTests

# Etapa de ejecución
FROM openjdk:8-jre-alpine

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el JAR generado desde la etapa de construcción
COPY --from=builder /app/target/*.jar app.jar

# Exponer el puerto 8080
EXPOSE 8080

# Definir el comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]
