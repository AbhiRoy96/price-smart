FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/price-smart-0.0.1.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]