FROM amazoncorretto:17-alpine
WORKDIR /app
# 인텔리제이가 생성한 jar 파일을 바로 복사합니다.
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]