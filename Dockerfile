FROM eclipse-temurin:11-alpine
COPY api/build/libs/api.jar /app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]