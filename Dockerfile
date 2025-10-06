FROM openjdk:latest
COPY ./target/DevOps_Labs-0.1.0.2-SNAPSHOT-jar-with-dependencies.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "DevOps_Labs-0.1.0.2-SNAPSHOT-jar-with-dependencies.jar"]