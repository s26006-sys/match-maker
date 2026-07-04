# 1단계: 빌드 환경 설정 (가장 안정적인 공식 Gradle 이미지 사용)
FROM gradle:8-jdk17 AS build
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew build -x test

# 2단계: 실행 환경 설정
FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]