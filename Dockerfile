FROM openjdk:latest
COPY ./target/DevOps_Labs-1.1-SNAPSHOT-jar-with-dependencies.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "DevOps_Labs-1.1-SNAPSHOT-jar-with-dependencies.jar"]