FROM openjdk:19-jdk-slim

WORKDIR /app

COPY target/finance-navigator-1.0.0.jar finance-navigator-1.0.0.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "finance-navigator-1.0.0.jar"]
