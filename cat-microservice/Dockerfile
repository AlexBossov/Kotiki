FROM openjdk:17
WORKDIR /Kotiki/
ADD target/*.jar cat-microservice.jar
ENTRYPOINT ["java", "-jar","cat-microservice.jar"]
EXPOSE 8082