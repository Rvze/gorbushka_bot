package entity

import "time"

type StuffUpdateType string

const (
	DELETE StuffUpdateType = "DELETE"
	CREATE StuffUpdateType = "CREATE"
	UPDATE StuffUpdateType = "UPDATE"
)

type StuffType string

const (
	IPHONE  StuffType = "IPHONE"
	AIRPODS StuffType = "AIRPODS"
	MACBOOK StuffType = "MACBOOK"
)

type Country string

const (
	USA    = "USA"
	RUSSIA = "RUSSIA"
)

type PriceUpdate struct {
	OldPrice int64 `json:"old_price"`
	NewPrice int64 `json:"new_price"`
}

type StuffUpdateEvent struct {
	StuffUpdateType StuffUpdateType `json:"type"`
	ModelId         string          `json:"model_id"`
	Payload         *PriceUpdate    `json:"payload"`
}

type StuffUpdateEventsBatch struct {
	Events []StuffUpdateEvent `json:"updates"`
}

type NotificationSubscription struct {
	Id               int64
	UserId           int64
	ModelId          string
	SubscriptionType string
}

type Notification struct {
	Id             int64
	UserId         int64
	SubscriptionId *int64
	Text           string
	Status         string
	ReadAt         *time.Time
	CreatedAt      time.Time
}

func NewNotification(userId int64, subscriptionId *int64, text string) *Notification {
	return &Notification{
		Id:             0,
		UserId:         userId,
		SubscriptionId: subscriptionId,
		Text:           text,
		Status:         "NEW",
		ReadAt:         nil,
		CreatedAt:      time.Now(),
	}
}