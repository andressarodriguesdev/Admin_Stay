FROM openjdk:21-jdk-slim
COPY target/desafio-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

