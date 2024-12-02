## Stage 1: Use a base image with JDK support
FROM openjdk:17-jdk-slim AS builder

# Set the working directory
WORKDIR /app

# Install required tools
RUN apt-get update && apt-get install -y jq curl

# Define build-time arguments for GitHub token and repository information
ARG GH_TOKEN
ARG GH_ACTION_REPOSITORY
ARG GH_ACTION_REF

# Fetch the release asset dynamically during Docker build
RUN RELEASE_JSON=$(curl -s -H "Authorization: token $GH_TOKEN" \
    "https://github.aexp.com/api/v3/repos/$GH_ACTION_REPOSITORY/releases/tags/$GH_ACTION_REF") && \
    RELEASE_ASSET_URL=$(echo $RELEASE_JSON | jq -r '.assets[0].browser_download_url') && \
    curl -L -H "Authorization: token $GH_TOKEN" -o git2go.jar "$RELEASE_ASSET_URL"

# Copy the JAR file to the container's working directory
COPY git2go.jar /app/git2go.jar

# Command to run the application using Java
CMD ["java", "-jar", "/app/git2go.jar"]
