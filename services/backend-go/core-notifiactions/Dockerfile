FROM golang:1.18-alpine3.17 AS build

WORKDIR /app

COPY . .

RUN go mod download && \
    CGO_ENABLED=0 GOOS=linux go build -o myapp

FROM alpine:3.17

WORKDIR /app

COPY --from=build /app/myapp .

EXPOSE 8080

CMD ["./myapp"]
