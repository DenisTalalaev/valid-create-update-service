FROM openjdk:17-jdk-alpine

WORKDIR /app
COPY build/libs/*.jar valid-create-update-service.jar

ENTRYPOINT ["java", "-jar", "valid-create-update-service.jar"]