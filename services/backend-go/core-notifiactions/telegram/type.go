package telegram

type Event struct {
	Text   string `json:"text"`
	ChatID int    `json:"chat_id"`
}
