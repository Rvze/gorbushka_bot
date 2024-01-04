package entity

type ModelType int
type Country int
type EventType int

const (
	IPHONE  ModelType = 0
	AIRPODS ModelType = 1
)

const (
	USA    Country = 0
	RUSSIA Country = 1
)

const (
	DELETE EventType = 0
	INSERT EventType = 1
	UPDATE EventType = 2
	GET    EventType = 3
)

type Event struct {
	ModelType
	ModelId  string
	Supplier string
	Country
	Price string
	EventType
}

type Subscribe struct {
	ChatId int
	ModelType
	ModelId string
}
