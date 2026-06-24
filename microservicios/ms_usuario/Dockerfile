FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml .
COPY eureka-server eureka-server
COPY ms_usuario ms_usuario
COPY ms_clientes ms_clientes
COPY ms_pedidos ms_pedidos
COPY ms_producto ms_producto
COPY ms_inventario ms_inventario
COPY ms_envio ms_envio
COPY ms_pago ms_pago
COPY ms_categoria ms_categoria
COPY ms_proveedores ms_proveedores
COPY ms_boleta ms_boleta

RUN mvn -pl ms_usuario -am clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /workspace/ms_usuario/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]