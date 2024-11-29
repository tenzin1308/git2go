# Stage 1: Build the JAR file
FROM maven:3.8.5-openjdk-17 AS builder
# Set the working directory
WORKDIR /app

# Copy the pom.xml to the container
COPY pom.xml .

# Copy the source code to the container
COPY src/ /app/src/

# Copy the .github to the container
COPY .github/ /app/.github/

# Build the project using Maven (package only to avoid tests)
RUN mvn clean package -DskipTests

# Stage 2: Build the runtime image
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar git2go.jar
ENTRYPOINT ["java", "-cp", "git2go.jar", "com.aexp.acq.go2.Git2Go"]
