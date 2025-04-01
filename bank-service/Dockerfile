FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app

COPY . .

RUN mvn clean install -N -DskipTests

RUN mvn clean package -DskipTests -pl bank-service -am

FROM eclipse-temurin:17-alpine
COPY --from=build /app/bank-service/target/*.jar app.jar
CMD ["java", "-jar", "app.jar"]