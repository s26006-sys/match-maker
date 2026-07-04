# 1단계: 빌드 환경 설정
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN ./gradlew build -x test

# 2단계: 실행 환경 설정
FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]