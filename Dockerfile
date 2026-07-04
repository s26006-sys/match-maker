FROM gradle:8-jdk17 AS build
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
# 빌드할 때 'prod' 프로필을 적용하고, 테스트는 건너뜁니다.
RUN ./gradlew build -x test -Dspring.profiles.active=prod

FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
# 실행할 때도 'prod' 설정을 먹여서 실행합니다.
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]