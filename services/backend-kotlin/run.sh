#!/bin/bash
# shellcheck disable=SC2164

# BUILD library-recognition service
cd library-recognition
gradle clean
gradle build
cd ..

# BUILD core-client service
cd core-client
gradle clean
gradle build
cd ..
#docker build .

# BUILD core-supplier service
cd core-supplier
gradle clean
gradle build
cd ..
#docker build .

# BUILD docker compose
docker-compose build
docker-compose down
docker-compose up
