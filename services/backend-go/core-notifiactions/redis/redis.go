package redis

import (
	"corenotif/log"
	"corenotif/model/entity"
	"corenotif/service"
	"encoding/json"
	"fmt"
	"time"

	"github.com/go-redis/redis"
)

type Redis struct {
	stuffUpdatesWorkers int
	buyRequestsWorkers  int
	service             service.NotificationService
	client              redis.Client
	stuffUpdates        chan redis.Message
	buyRequests         chan redis.Message
}

func GetRedis(stuffUpdatesWorkers, stuffUpdatesCap int, buyRequestsWorkers, buyRequestsCap int, service service.NotificationService) *Redis {
	defer func() {
		log.Log(log.SUCCESS, "Redis Client was set")
	}()

	return &Redis{
		stuffUpdatesWorkers: stuffUpdatesWorkers,
		buyRequestsWorkers:  buyRequestsWorkers,
		service:             service,
		client: *redis.NewClient(&redis.Options{
			//Addr: "localhost:6379",
			Addr:     "rc1a-85pgddbeqm81683g.mdb.yandexcloud.net:26379",
			Password: "password",
			DB:       0,
		}),
		stuffUpdates: make(chan redis.Message, stuffUpdatesCap),
		buyRequests:  make(chan redis.Message, buyRequestsCap),
	}
}

func (r *Redis) Start() {
	for i := 0; i < r.stuffUpdatesWorkers; i++ {
		go r.processStuffUpdates(i)
	}

	r.consume()
}

func (r *Redis) consume() {
	redisPubSub := r.client.Subscribe("stuff_update_channel")

	defer func(redisPubSub *redis.PubSub) {
		err := redisPubSub.Close()
		if err != nil {
			fmt.Println(log.Err("Error closing redisPubSub", err))
		}
	}(redisPubSub)

	for {
		msg, err := redisPubSub.ReceiveMessage()
		if err != nil {
			fmt.Println(log.Err(fmt.Sprintf("Error receiving msg from"), err))
			time.Sleep(100 * time.Millisecond)
			continue
		}

		switch msg.Channel {
		case "stuff_update_channel":
			r.stuffUpdates <- *msg
		}
	}
}

func (r *Redis) processStuffUpdates(gr int) {
	for {
		message := <-r.stuffUpdates
		log.Log(log.INFO, fmt.Sprintf("Gorutine: %d: Got stuff updates message", gr))

		value := entity.StuffUpdateEventsBatch{}

		if err := json.Unmarshal([]byte(message.Payload), &value); err != nil {
			fmt.Println(log.Err("Can't unmarshal json value", err))
			continue
		}
		r.service.ProcessStuffUpdate(value)
	}
}
