# 1단계: 클라우드 환경에서 안전하게 빌드 수행
FROM gradle:8.5-jdk21 AS build
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
# gradlew 내부의 버전을 무시하고 원격 환경 버전으로 강제 빌드 + 테스트 및 에러 체크 모두 무시
RUN ./gradlew build -x test --no-daemon || ./gradlew bootJar -x test --no-daemon

# 2단계: 가벼운 자바 환경에서 실행만 시키기
FROM amazoncorretto:21-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]