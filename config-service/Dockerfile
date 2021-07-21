FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY apikey.jks apikey.jks
COPY build/libs/config-service-1.0.jar configServer.jar
ENTRYPOINT ["java", "-jar", "configServer.jar"]