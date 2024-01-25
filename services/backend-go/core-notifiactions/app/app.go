package app

import (
	"context"
	"corenotif/config"
	"corenotif/database"
	"corenotif/message"
	"corenotif/redis"
	"corenotif/service"
	"fmt"
	"os"

	"github.com/jackc/pgx/v4/pgxpool"
)

func Start() {
	config.InitConfig()
	messageProcessor := message.New()
	db := database.NewRepository(initDB())
	notificationService := service.GetService(db, messageProcessor)
	redisInstance := redis.GetRedis(
		1, 100,
		1, 100,
		notificationService,
	)
	redisInstance.Start()
}

func initDB() *pgxpool.Pool {
	dbUrl := "postgres://" + "nmakarov" + ":" + "makarovnurgun" + "@" + "rc1b-a91wke62kjgj13zf.mdb.yandexcloud.net" + ":" + "6432" + "/makarov-n?search_path=rbdip"
	config, err := pgxpool.ParseConfig(dbUrl)
	if err != nil {
		fmt.Fprintf(os.Stderr, "Unnable to parse config: %v\n", err)
		os.Exit(1)
	}

	config.MaxConns = 1

	pool, err := pgxpool.ConnectConfig(context.Background(), config)
	if err != nil {
		fmt.Fprintf(os.Stderr, "Unnable to connect to database: %v\n", err)
		os.Exit(1)
	}

	return pool
}
