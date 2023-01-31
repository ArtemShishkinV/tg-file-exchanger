FROM openjdk:17-alpine

EXPOSE 8085

ADD /target/node-1.0-SNAPSHOT.jar node.jar

ENTRYPOINT ["java", "-jar", "node.jar"]