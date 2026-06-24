# Imagen base con Java (ajusta la versión según tu proyecto)
FROM openjdk:17-jdk-slim

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el jar generado desde target/
COPY target/ms_usuario-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto (ajústalo según tu microservicio)
EXPOSE 8080

# Comando de ejecución
ENTRYPOINT ["java", "-jar", "app.jar"]
  