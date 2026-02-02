FROM artifactory.aexp.com/paas-registry/buildpacks/rhel-jdk-21-builder:21.x.latest

WORKDIR /app

# Build-time argument
ARG JAR_FILE

# Copy jar into container with a stable name
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
