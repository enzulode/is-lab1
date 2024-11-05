# Build stage
FROM gradle:8.10.2-jdk21-alpine AS backend-builder
WORKDIR /sources-build
COPY --chown=gradle:gradle . ./
RUN gradle build --no-daemon -x test

# Run stage
FROM openjdk:21-jdk-slim
WORKDIR /application
COPY --from=backend-builder /sources-build/build/libs/is-lab1-1.0.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
