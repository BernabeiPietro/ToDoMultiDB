FROM openjdk:8
ARG jarToCopy
COPY ./target/$jarToCopy /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
CMD ["--logging.file.name=app.log"]
