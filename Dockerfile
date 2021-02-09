##### Setup Maven ####
FROM maven:3.6.3-amazoncorretto-11 AS dependencies

# Resolve dependencies and cache them
COPY pom.xml /usr/src/app/
RUN mvn -f /usr/src/app/pom.xml dependency:go-offline


##### Compile stage #####
FROM dependencies AS build

COPY src/main /usr/src/app/src/main
RUN mvn -f /usr/src/app/pom.xml clean package


##### Test stage #####
FROM dependencies AS test

COPY src/test /usr/src/app/src/test
COPY --from=build /usr/src/app/target /usr/src/app/target
RUN mvn -f /usr/src/app/pom.xml test


##### Assemble artifact #####
FROM amazoncorretto:11 AS assemble

# Not sure what this one does
RUN curl -o dd-java-agent.jar -L 'https://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&g=com.datadoghq&a=dd-java-agent&v=LATEST'

# Copy the jar from build stage to this one
COPY --from=build /usr/src/app/target/back-office-0.0.1-SNAPSHOT.jar /usr/app/

# Define entry point
ENTRYPOINT java -javaagent:/dd-java-agent.jar -jar /usr/app/back-office-0.0.1-SNAPSHOT.jar
