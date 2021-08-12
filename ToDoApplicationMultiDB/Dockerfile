FROM openjdk:8
ARG jarToCopy
COPY /target/$jarToCopy /app/app.jar

CMD ["java", "-jar", "/app/app.jar"]