package app

import (
	"context"
	"corenotif/config"
	"corenotif/controller"
	"corenotif/database"
	"corenotif/redis"
	"corenotif/service"
	"corenotif/telegram/client"
	"corenotif/telegram/processor"
	"fmt"
	"os"

	"github.com/jackc/pgx/v4/pgxpool"
)

const tgBotHost = "api.telegram.org"

func Start() {
	config.InitConfig()
	tgClient := client.New(tgBotHost, config.GetTgToken())
	tgProcessor := processor.New(tgClient)
	repository := database.NewRepository(initDB())
	serviceInstance := service.GetService(tgProcessor, repository)
	subscriber := service.GetSubscribeService(repository)
	controllerInstance := controller.GetController(subscriber)
	redisInstance := redis.GetRedis(1, 100, serviceInstance)

	controllerInstance.Start()
	redisInstance.Start()
	serviceInstance.Start()
}

func initDB() *pgxpool.Pool {
	dbUrl := "postgres://" + config.GetDBUser() + ":" + config.GetDBPassword() + "@" + config.GetDBHost() + ":" + config.GetDBPort() + "/tsypk"
	pool, err := pgxpool.Connect(context.Background(), dbUrl)
	if err != nil {
		_, err := fmt.Fprintf(os.Stderr, "Unnable to connect to database: %v\n", err)
		if err != nil {
		}
		os.Exit(1)
	}

	return pool
}
