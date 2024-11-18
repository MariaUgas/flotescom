FROM eclipse-temurin:21-jdk-alpine as build
WORKDIR /app
  # Copiar archivos del proyecto
COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw
COPY pom.xml .
COPY src src
  # Construir el proyecto
RUN ./mvnw clean package -DskipTests
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
  # Configuración de la JVM para contenedores
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
EXPOSE 8281
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]