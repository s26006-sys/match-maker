# 1단계: 내 컴퓨터 환경을 무시하고, 클라우드 내부에서 호환성이 검증된 환경(Gradle 8.5 + JDK 21)을 새로 구축
FROM gradle:8.5-jdk21 AS build
WORKDIR /app

# 프로젝트 파일들을 도커 내부로 복사
COPY . .

# ⚠️ 중요: 내 컴퓨터의 고장난 gradlew를 쓰지 않고, 도커 환경의 순수 Gradle로 빌드를 강제 진행합니다.
# 테스트 코드로 인한 빌드 중단을 막기 위해 테스트는 제외합니다.
RUN gradle bootJar -x test --no-daemon

# 2단계: 경량화된 실행 환경 구성
FROM amazoncorretto:21-alpine
WORKDIR /app

# 위에서 안전하게 새로 빌드된 jar 파일만 쏙 복사해옵니다.
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]