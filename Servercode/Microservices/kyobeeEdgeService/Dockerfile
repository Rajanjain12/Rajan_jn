FROM maven:3.6.3-jdk-11 AS MAVEN_BUILD
MAINTAINER Vruddhi Shah
#RUN mkdir -p ~/build
COPY pom.xml /build/
COPY src /build/src/
WORKDIR /build/
RUN mvn package
FROM openjdk:11-jre-slim
EXPOSE 8111
WORKDIR /app
COPY --from=MAVEN_BUILD /build/target/kyobeeEdgeService-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["java", "-jar", "kyobeeEdgeService-0.0.1-SNAPSHOT.jar"]
