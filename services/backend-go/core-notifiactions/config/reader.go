package config

import (
	"corenotif/log"
	"os"
	"sync"
)

var configInstance Config
var once sync.Once

func newConfig(c Config) {
	once.Do(func() {
		configInstance = c
	})
}

func InitConfig() {
	var c Config

	c.HttpHost = os.Getenv("HTTP_HOST")
	c.HttpPort = os.Getenv("HTTP_PORT")
	c.RedisHost = os.Getenv("REDIS_HOST")
	c.RedisPort = os.Getenv("REDIS_PORT")
	c.RedisTopic = os.Getenv("REDIS_TOPIC")
	c.DBUser = os.Getenv("DB_USER")
	c.DBPassword = os.Getenv("DB_PASSWORD")
	c.DBHost = os.Getenv("DB_HOST")
	c.DBPort = os.Getenv("DB_PORT")
	c.TgToken = os.Getenv("TG_TOKEN")

	newConfig(c)
	log.Log(log.SUCCESS, "Config was set")
}

func GetHttpHost() string {
	return configInstance.HttpHost
}

func GetHttpPort() string {
	return configInstance.HttpPort
}

func GetRedisHost() string {
	return configInstance.RedisHost
}

func GetRedisPort() string {
	return configInstance.RedisPort
}

func GetRedisTopic() string {
	return configInstance.RedisTopic
}

func GetDBUser() string {
	return configInstance.DBUser
}

func GetDBPassword() string {
	return configInstance.DBPassword
}

func GetDBHost() string {
	return configInstance.DBHost
}

func GetDBPort() string {
	return configInstance.DBPort
}

func GetTgToken() string {
	return configInstance.TgToken
}
