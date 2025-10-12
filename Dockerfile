FROM openjdk:25-ea-21-slim-bullseye
WORKDIR /app
COPY target/test_project-0.0.1-SNAPSHOT.jar /app/phases.jar
LABEL authors="glebzapara"
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "phases.jar"]