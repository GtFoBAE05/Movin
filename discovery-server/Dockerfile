FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app

COPY . .

RUN mvn clean install -N -DskipTests

RUN mvn clean package -DskipTests -pl discovery-server -am

FROM eclipse-temurin:17
COPY --from=build /app/discovery-server/target/*.jar app.jar
CMD ["java", "-jar", "app.jar"]