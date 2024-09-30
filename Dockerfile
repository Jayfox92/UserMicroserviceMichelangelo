FROM openjdk:21-jdk
EXPOSE 7676
MAINTAINER michelangelo
WORKDIR /app
COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]