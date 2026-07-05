FROM maven:3.8.3-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Этап 2: Финальный образ (Run)
FROM eclipse-temurin:17-jdk
COPY --from=build /target/loader-reference-1.0.0.jar app.jar
ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]