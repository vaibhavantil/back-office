##### Setup Maven ####
FROM maven:3.6.3-amazoncorretto-11 AS maven

# Resolve dependencies and cache them
COPY pom.xml /usr/src/app/
RUN mvn -f /usr/src/app/pom.xml dependency:go-offline


##### Compile stage #####
FROM maven AS compile

COPY src/main /usr/src/app/src/main
RUN mvn -f /usr/src/app/pom.xml clean compile


##### Test stage #####
FROM maven AS test

COPY src/test /usr/src/app/src/test
COPY --from=compile /usr/src/app/target /usr/src/app/target
RUN mvn -f /usr/src/app/pom.xml test


##### Package stage #####
FROM maven AS package

COPY --from=compile /usr/src/app/target /usr/src/app/target
RUN mvn -f /usr/src/app/pom.xml package


##### Package artifact as runnable image #####
FROM amazoncorretto:11 AS make_image

# Not sure what this one does
RUN curl -o dd-java-agent.jar -L 'https://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&g=com.datadoghq&a=dd-java-agent&v=LATEST'

# Copy the jar from build stage to this one
COPY --from=package /usr/src/app/target/back-office-0.0.1-SNAPSHOT.jar /usr/app/

# Define entry point
ENTRYPOINT java -javaagent:/dd-java-agent.jar -jar /usr/app/back-office-0.0.1-SNAPSHOT.jar
