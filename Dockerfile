FROM maven:3.6.0-jdk-8-alpine
RUN mkdir -p /oddsservice
ADD . /oddsservice
WORKDIR /oddsservice
RUN mvn clean compile package
RUN ls /oddsservice/target
FROM maven:3.6.0-jdk-8-alpine
RUN mkdir -p /oddsservice
WORKDIR /oddsservice
COPY --from=0 /oddsservice/target/demo-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/oddsservice/app.jar"]