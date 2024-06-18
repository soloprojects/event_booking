# Build stage
FROM maven:3.8.7-openjdk-18 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM amazoncorretto:17
ARG APP_VERSION=1.0.0

WORKDIR /app
COPY --from=build /build/target/booking-*.jar /app/

EXPOSE 8088

ENV DB_URL=jdbc:postgresql://event-booking-db:5432/event_booking

ENV JAR_VERSION=${APP_VERSION}

CMD java -jar -Dspring.datasource.url=${DB_URL}  booking-${JAR_VERSION}.jar