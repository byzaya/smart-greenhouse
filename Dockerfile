FROM openjdk:19-slim AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM openjdk:19-slim
WORKDIR /app
COPY --from=build /app/target/smart-greenhouse-1.0-SNAPSHOT.jar .
CMD ["java", "-jar", "smart-greenhouse-1.0-SNAPSHOT.jar"]
