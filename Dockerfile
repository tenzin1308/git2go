## Stage 1: Use a base image with JDK support
FROM artifactory.aexp.com/dockerproxy/azul/zulu-openjdk@sha256:6d01d86257747b869eb1fbc8ff723b912c76277d9ac3a266640250d626233647

# Set the working directory
WORKDIR /app

# Install required tools
RUN \
    apt-get update -o Acquire::https::artifactory.aexp.com::Verify-Peer=false && \
    apt-get install -y -o Acquire::https::artifactory.aexp.com::Verify-Peer=false ca-certificates curl jq && \
    apt-get clean

## Define build-time arguments for GitHub token and repository information
#ARG GH_TOKEN
#ARG GH_ACTION_REPOSITORY
#ARG GH_ACTION_REF

# Fetch the release asset dynamically during Docker build
RUN RELEASE_JSON=$(curl -s -H "Authorization: token $GITHUB_TOKEN" \
    "https://github.aexp.com/api/v3/repos/$GITHUB_ACTION_REPOSITORY/releases/tags/$GITHUB_ACTION_REF") && \
    RELEASE_ASSET_URL=$(echo $RELEASE_JSON | jq -r '.assets[0].browser_download_url') && \
    curl -L -H "Authorization: token $GITHUB_TOKEN" -o git2go.jar "$RELEASE_ASSET_URL"

# Copy the JAR file to the container's working directory
COPY git2go.jar /app/git2go.jar

# Command to run the application using Java
CMD ["java", "-jar", "/app/git2go.jar"]
