#!/bin/bash
# shellcheck disable=SC2164

cd backend-go/core-notifiactions/
docker build -t notifications:latest .

cd ..
cd ..
cd backend-kotlin

# BUILD library-recognition service
cd library-recognition
gradle clean
gradle build
cd ..

# BUILD core-client service
cd core-client
gradle clean
gradle build
docker build -t core-client:latest .
cd ..

# BUILD core-supplier service
cd core-supplier
gradle clean
gradle build
docker build -t core-supplier:latest .
cd ..

# BUILD docker compose
docker-compose build
docker-compose down
docker-compose up -d
