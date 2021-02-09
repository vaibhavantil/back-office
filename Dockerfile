##### Setup Maven ####
FROM maven:3.6.3-amazoncorretto-11 AS maven

# Resolve dependencies and cache them
COPY pom.xml /usr/src/app/
RUN mvn -f /usr/src/app/pom.xml dependency:go-offline

##### Build stage #####
FROM maven AS build

COPY src /usr/src/app/src
RUN mvn -f /usr/src/app/pom.xml clean package -Dmaven.javadoc.skip=true -V -e

##### Package artifact as runnable image #####
FROM amazoncorretto:11 AS make_image

# Not sure what this one does
RUN curl -o dd-java-agent.jar -L 'https://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&g=com.datadoghq&a=dd-java-agent&v=LATEST'

# Copy the jar from build stage to this one
COPY --from=build /usr/src/app/target/back-office-0.0.1-SNAPSHOT.jar /usr/app/

# Define entry point
ENTRYPOINT java -javaagent:/dd-java-agent.jar -jar /usr/app/back-office-0.0.1-SNAPSHOT.jar
