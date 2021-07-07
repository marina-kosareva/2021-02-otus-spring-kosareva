# syntax = docker/dockerfile:experimental
FROM maven:3.8.1-jdk-11-slim as builder
WORKDIR /src
COPY ./pom.xml /src
RUN -- mount=type=cache,target=/root/.m2 mvn dependency:go-offline
COPY . /src
RUN mvn --no-transfer-progress package -DskipTests=true -Dskip.unit.tests=true -Ddependency-check.skip=true

FROM openjdk:11.0
COPY --from=builder /src/target/library.jar /home/library.jar
EXPOSE 8080
CMD ["java", "-jar", "/home/library.jar"]
