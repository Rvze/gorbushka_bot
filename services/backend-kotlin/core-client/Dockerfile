FROM openjdk:17-jdk-slim-buster
WORKDIR /app

# install certificates
#RUN apt-get update --fix-missing \
#    && apt install wget -y --allow-unauthenticated \
#
#RUN wget "https://storage.yandexcloud.net/cloud-certs/CA.pem" ~/.postgresql/root.crt && chmod 0600 ~/.postgresql/root.crt

COPY /build/libs/core-client-1.0.jar build/
WORKDIR /app/build
ENTRYPOINT java -jar core-client-1.0.jar
