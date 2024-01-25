package message

import (
	"corenotif/log"
	"corenotif/model/entity"
)

type Processor struct {
}

func New() *Processor {
	defer func() {
		log.Log(log.INFO, "Message processor init")
	}()

	return &Processor{}
}

func (p *Processor) Process(event entity.StuffUpdateEvent) error {
	return p.processMessage(event)
}

func (p *Processor) processMessage(event entity.StuffUpdateEvent) error {
	return nil
}