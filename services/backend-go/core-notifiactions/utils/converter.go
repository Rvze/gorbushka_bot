package utils

import (
	"corenotif/log"
	"corenotif/model/entity"
	pb "corenotif/model/proto"
)

func Convert(obj *pb.Item) *entity.Event {
	defer func() {
		log.Log(log.INFO, "Service: object converted")
	}()

	return &entity.Event{
		ModelType: entity.ModelType(obj.ModelType),
		ModelId:   obj.ModelId,
		Supplier:  obj.Supplier,
		Country:   entity.Country(obj.Country),
		Price:     obj.Price,
		EventType: entity.EventType(obj.EventType),
	}
}
