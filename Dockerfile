
##### Build stage #####
FROM maven:3.6.3-amazoncorretto-11 AS build

# Copy source folder and pom.xml to this stage
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app/

# Resolve dependencies and cache them
RUN mvn -f /usr/src/app/pom.xml dependency:go-offline
# Build and produce an artifact
RUN mvn -f /usr/src/app/pom.xml clean install -DskipTests=true -Dmaven.javadoc.skip=true -V -e

##### Test stage #####
FROM maven:3.6.3-amazoncorretto-11 AS test

COPY --from=build /usr/src/app/pom.xml /usr/src/app/
RUN mvn -f /usr/src/app/pom.xml test

##### Package artifact as runnable image #####
FROM amazoncorretto:11 AS make-image

# Not sure what this one does
RUN curl -o dd-java-agent.jar -L 'https://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&g=com.datadoghq&a=dd-java-agent&v=LATEST'

# Copy the jar from build stage to this one
COPY --from=build /usr/src/app/target/back-office-0.0.1-SNAPSHOT.jar /usr/app/

# Define entry point
ENTRYPOINT java -javaagent:/dd-java-agent.jar -jar /usr/app/back-office-0.0.1-SNAPSHOT.jar
