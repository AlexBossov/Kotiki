FROM openjdk:17
WORKDIR /Kotiki/
ADD target/*.jar data-access-layer.jar
ENTRYPOINT ["java", "-jar","data-access-layer.jar"]
EXPOSE 8082