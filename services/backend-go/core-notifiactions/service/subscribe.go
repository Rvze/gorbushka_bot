package service

import (
	"corenotif/database"
	"corenotif/model/entity"
)

type SubscribeService interface {
	Subscribe(obj entity.Subscribe) error
	UnSubscribe(chatId int) error
}

type subscribeService struct {
	db database.Repository
}

func GetSubscribeService(db database.Repository) SubscribeService {
	return &subscribeService{
		db: db,
	}
}

func (s *subscribeService) Subscribe(obj entity.Subscribe) error {
	err := s.db.Insert(obj.ChatId, obj.ModelType, obj.ModelId)

	if err != nil {
		return err
	}

	return nil
}

func (s *subscribeService) UnSubscribe(chatId int) error {
	err := s.db.Delete(chatId)

	if err != nil {
		return err
	}

	return err
}
