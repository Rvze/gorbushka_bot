package redis

import (
	"corenotif/config"
	"corenotif/log"
	pb "corenotif/model/proto"
	"corenotif/service"
	"fmt"
	"time"

	"github.com/go-redis/redis"
	"google.golang.org/protobuf/proto"
)

type Redis struct {
	workersNum  int
	service     service.Service
	client      redis.Client
	messageChan chan redis.Message
}

func GetRedis(workersNum, chanCap int, service service.Service) *Redis {
	defer func() {
		log.Log(log.SUCCESS, "Redis Client was set")
	}()
	return &Redis{
		workersNum: workersNum,
		service:    service,
		client: *redis.NewClient(&redis.Options{
			Addr:     config.GetRedisHost() + ":" + config.GetRedisPort(),
			Password: "",
			DB:       0,
		}),
		messageChan: make(chan redis.Message, chanCap),
	}
}

func (r *Redis) Start() {
	go r.consume()
	for i := 0; i < r.workersNum; i++ {
		go r.process(i)
	}
}

func (r *Redis) process(gr int) {
	for {
		message := <-r.messageChan
		log.Log(log.INFO, fmt.Sprintf("Gorutine: %d: Got message", gr))

		value := new(pb.Item)

		if err := proto.Unmarshal([]byte(message.Payload), value); err != nil {
			fmt.Println(log.Err("Can't unmarshal Proto value", err))
			continue
		}
		r.service.Process(value)
	}
}

func (r *Redis) consume() {
	pubsub := r.client.Subscribe(config.GetRedisTopic())
	defer func(pubsub *redis.PubSub) {
		err := pubsub.Close()
		if err != nil {
			fmt.Println(log.Err("Error closing pubsub", err))
		}
	}(pubsub)

	for {
		message, err := pubsub.ReceiveMessage()
		if err != nil {
			fmt.Println(log.Err("Error receiving message from Reddis", err))
			time.Sleep(10 * time.Second)
			continue
		}

		r.messageChan <- *message
	}
}
