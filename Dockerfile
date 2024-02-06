FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
EXPOSE 8080
ARG APP_NAME=ms-mqdesk.jar
ARG JAR_FILE=target/ms-mqdesk.jar

ADD  ${JAR_FILE} ms-mqdesk.jar
ENTRYPOINT ["java", "-jar","--add-opens", "java.base/java.time=ALL-UNNAMED", "-Dspring.profiles.active=prod", "/ms-mqdesk.jar"]
