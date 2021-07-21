FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY target/apigateway-service-v2-1.0.jar apigatewayService.jar
ENTRYPOINT ["java", "-jar", "apigatewayService.jar"]