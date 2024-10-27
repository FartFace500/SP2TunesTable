# Start with Amazon Corretto 17 Alpine base image
FROM amazoncorretto:17-alpine

# Install curl on Alpine
RUN apk update && apk add --no-cache curl

# Copy the jar file into the image
COPY target/app.jar /dat.jar

# Expose the port your dat runs on
EXPOSE 7071

# Command to run your dat
CMD ["java", "-jar", "/dat.jar"]
