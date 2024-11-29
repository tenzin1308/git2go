# Stage 1: Build the JAR file
FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Build the runtime image
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar git2go.jar
ENTRYPOINT ["java", "-cp", "git2go.jar", "com.aexp.acq.go2.Git2Go"]
