package processor

import (
	"corenotif/log"
	"corenotif/telegram"
	"corenotif/telegram/client"
)

type TgProcessor struct {
	tg *client.Client
}

func New(client *client.Client) *TgProcessor {
	defer func() {
		log.Log(log.INFO, "telegram processor inited")
	}()
	return &TgProcessor{
		tg: client,
	}
}

func (p *TgProcessor) Process(event telegram.Event) error {
	return p.processMessage(event)
}

func (p *TgProcessor) processMessage(event telegram.Event) error {
	return p.tg.SendMessage(event.ChatID, event.Text)
}
