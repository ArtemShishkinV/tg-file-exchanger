FROM openjdk:17-alpine

EXPOSE 8084

ADD /target/dispatcher-1.0-SNAPSHOT.jar dispatcher.jar

ENTRYPOINT ["java", "-jar", "dispatcher.jar"]
