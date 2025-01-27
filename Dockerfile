# Step 1: Build the application
FROM maven:3.8.1ZZZZZZ-openjdk-17-slim AS build


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

# Copy the jar file from the build image
COPY --from=build /app/target/*.jar /app/app.jar

# Run the application
CMD ["java", "-jar", "app.jar"]