# 1단계: 빌드 환경 (자바 21 환경에서 깔끔하게 빌드)
FROM gradle:8.5-jdk21 AS build
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
# 테스트 단계를 제외하여 설정 오류 등으로 인한 빌드 실패를 방지합니다.
RUN ./gradlew build -x test --no-daemon

# 2단계: 실행 환경
FROM amazoncorretto:21-alpine
WORKDIR /app
# 위 빌드 단계에서 새로 생성된 따끈따끈한 jar 파일만 복사합니다.
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]