# Step 1: Build the application
FROM maven:3.8.6-openjdk-17 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download the dependencies
COPY pom.xml .

# Download the dependencies
RUN mvn dependency:go-offline

# Copy the source code
COPY src /app/src

# Package the application
RUN mvn clean package -DskipTests

# Step 2: Create a new image to run the app
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the packaged jar from the build stage
COPY --from=build /app/target/job-management-system-0.0.1-SNAPSHOT.jar /app/job-management-system.jar

# Expose the port that Spring Boot will run on (default is 3000)
EXPOSE 3000

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/job-management-system.jar"]
