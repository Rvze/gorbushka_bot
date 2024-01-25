package service

import (
	"corenotif/database"
	"corenotif/log"
	"corenotif/message"
	"corenotif/model/entity"
	"fmt"
	"sync"
)

type NotificationService interface {
	ProcessStuffUpdate(event entity.StuffUpdateEventsBatch)
}

type service struct {
	db        database.Repository
	processor *message.Processor
}

func newService(db database.Repository, processor *message.Processor) *service {
	return &service{
		db:        db,
		processor: processor,
	}
}

var (
	singleton *service
	once      sync.Once
)

func GetService(db database.Repository, processor *message.Processor) NotificationService {
	defer func() {
		log.Log(log.SUCCESS, "NotificationService was set")
	}()

	once.Do(func() {
		singleton = newService(db, processor)
	})

	return singleton
}

func (s *service) ProcessStuffUpdate(event entity.StuffUpdateEventsBatch) {
	log.Log(log.INFO, fmt.Sprintf("Processing stuff update events: %s", event.Events))

	for _, ev := range event.Events {
		subscriptions, err := s.db.GetAllByModelId(ev.ModelId)
		if err != nil {
			fmt.Println(log.Err("Can't get subscribers", err))
			continue
		}

		log.Log(log.INFO, fmt.Sprintf("Found subscriptions: %s", subscriptions))

		if len(subscriptions) == 0 {
			continue
		}

		var msg string
		switch ev.StuffUpdateType {
		case entity.UPDATE:
			msg = s.buildUpdateMessage(ev)
		case entity.CREATE:
			msg = s.buildCreateMessage(ev)
		case entity.DELETE:
			msg = s.buildDeleteMessage(ev)
		}

		for _, sub := range subscriptions {
			toSend := entity.NewNotification(sub.UserId, &sub.Id, msg)
			err = s.db.Insert(toSend)
			if err != nil {
				fmt.Println(log.Err("Can't create stuff update notification", err))
				continue
			}

			log.Log(log.INFO, fmt.Sprintf("Stuff update notification created: userId=%d, subId=%d", sub.UserId, sub.Id))
		}
	}
}

func (s *service) buildUpdateMessage(ev entity.StuffUpdateEvent) string {
	text := "У товара обновилась цена!\n"
	if ev.Payload != nil {
		text += fmt.Sprintf("%s: %d₽ -> %d₽\n", ev.ModelId, ev.Payload.OldPrice, ev.Payload.NewPrice)
	} else {
		log.Log(log.WARNING, "Received price update event without payload!")
	}
	return text
}

func (s *service) buildCreateMessage(ev entity.StuffUpdateEvent) string {
	text := "Поставщик добавил новый товар!\n"
	text += fmt.Sprintf("%s", ev.ModelId)
	return text
}

func (s *service) buildDeleteMessage(ev entity.StuffUpdateEvent) string {
	text := "Поставщик удалил свой товар!\n"
	text += fmt.Sprintf("%s", ev.ModelId)
	return text
}