FROM openjdk:8

ADD target/back-office-0.0.1-SNAPSHOT.jar /

ENTRYPOINT java -jar back-office-0.0.1-SNAPSHOT.jar
