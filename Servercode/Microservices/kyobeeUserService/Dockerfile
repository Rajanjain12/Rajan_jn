FROM maven:3.6.3-jdk-11 AS MAVEN_BUILD
MAINTAINER Vruddhi Shah
#RUN mkdir -p ~/build
COPY pom.xml /build/
COPY src /build/src/
WORKDIR /build/
RUN mvn package
FROM openjdk:11-jre-slim
EXPOSE 8081
WORKDIR /app
COPY --from=MAVEN_BUILD /build/target/kyobeeUserService-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["java", "-jar", "kyobeeUserService-0.0.1-SNAPSHOT.jar"]
