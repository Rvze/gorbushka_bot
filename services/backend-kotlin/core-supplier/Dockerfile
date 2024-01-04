FROM openjdk:17-jdk-slim-buster
WORKDIR /app

COPY /build/libs/core-supplier-1.0.jar build/

WORKDIR /app/build
ENTRYPOINT java -jar core-supplier-1.0.jar
