package service

import (
	"corenotif/database"
	"corenotif/log"
	"corenotif/model/entity"
	pb "corenotif/model/proto"
	"corenotif/telegram"
	"corenotif/telegram/processor"
	"corenotif/utils"
	"errors"
	"fmt"
	"sort"
	"sync"
)

type Service interface {
	Process(ev *pb.Item)
	Start()
}

type service struct {
	cache         *cache
	db            database.Repository
	tg            *processor.TgProcessor
	messageBuffer chan telegram.Event
	countryCodes  map[entity.Country]string
}

func newService(tg *processor.TgProcessor, db database.Repository) *service {
	return &service{
		cache: &cache{
			data:  make(map[string]map[string][]info),
			locks: make(map[string]*sync.RWMutex),
		},
		db:            db,
		tg:            tg,
		messageBuffer: make(chan telegram.Event, 100),
		countryCodes:  getCountryTable(),
	}
}

var (
	singleton *service
	once      sync.Once
)

func GetService(tg *processor.TgProcessor, db database.Repository) Service {
	defer func() {
		log.Log(log.SUCCESS, "Service was set")
	}()

	once.Do(func() {
		singleton = newService(tg, db)
	})

	return singleton
}

func (s *service) Process(ev *pb.Item) {
	obj := utils.Convert(ev)

	switch obj.EventType {

	case entity.INSERT:
		s.processInsert(obj)
	case entity.UPDATE:
		s.processUpdate(obj)
	case entity.DELETE:
		s.processDelete(obj)
	}
}

func (s *service) processInsert(obj *entity.Event) {
	s.cache.Set(obj.ModelId, obj.Supplier, info{
		country: obj.Country,
		price:   obj.Price,
	})
	updated := s.cache.Get(obj.ModelId)

	if updated == nil {
		fmt.Println(log.Err("Can't find this moduleId in cache", errors.New("")))
		return
	}

	text := s.makeInsertMessage(obj, *updated)
	users, err := s.defineUsers(obj)
	if err != nil {
		fmt.Println(log.Err("Can't define users", err))
	}
	tgMessages := s.initTgMessage(users, text)
	for _, tgm := range tgMessages {
		s.messageBuffer <- tgm
	}
}

func (s *service) processUpdate(obj *entity.Event) {

	curr := s.cache.Get(obj.ModelId)
	if curr == nil {
		fmt.Println(log.Err("Can't find this moduleId in cache", errors.New("")))
		return
	}

	var oldPrice string
	for _, inf := range (*curr)[obj.Supplier] {
		if inf.country == obj.Country {
			oldPrice = inf.price
		}
	}

	s.cache.Update(obj.ModelId, obj.Supplier, info{
		country: obj.Country,
		price:   obj.Price,
	})
	updated := s.cache.Get(obj.ModelId)

	if updated == nil {
		fmt.Println(log.Err("Can't find this moduleId in cache", errors.New("")))
		return
	}

	text := s.makeUpdateMessage(obj, *updated, oldPrice)
	users, err := s.defineUsers(obj)
	if err != nil {
		fmt.Println(log.Err("Can't define users", err))
	}

	tgMessages := s.initTgMessage(users, text)
	for _, tgm := range tgMessages {
		s.messageBuffer <- tgm
	}}

func (s *service) processDelete(obj *entity.Event) {
	s.cache.Delete(obj.ModelId, obj.Supplier, info{
		country: obj.Country,
		price:   obj.Price,
	})

	updated := s.cache.Get(obj.ModelId)

	if updated == nil {
		fmt.Println(log.Err("Can't find this moduleId in cache", errors.New("")))
		return
	}

	text := s.makeDeleteMessage(obj, *updated)
	users, err := s.defineUsers(obj)
	if err != nil {
		fmt.Println(log.Err("Can't define users", err))
	}

	tgMessages := s.initTgMessage(users, text)
	for _, tgm := range tgMessages {
		s.messageBuffer <- tgm
	}
}

func (s *service) Start() {
	for {
		x := <-s.messageBuffer
		err := s.tg.Process(x)
		if err != nil {
			log.Log(log.WARNING, "Service-Sender: can't send message")
			continue
		}
	}
}

func (s *service) makeInsertMessage(obj *entity.Event, mp map[string][]info) string {
	inverseMap := convert(mp)

	text := "<i>*ПОСТАВЩИКИ ДОБАВИЛИ ТОВАР</i>\n\n"
	text += fmt.Sprintf("<u>%s</u>\n\n", obj.ModelId)

	for country, inversArr := range inverseMap {
		for _, inv := range inversArr {
			if country == obj.Country && inv.supplier == obj.Supplier && inv.price == obj.Price {
				continue
			}

			text += fmt.Sprintf("%v  %s\u20BD  &#8212  %s\n", s.countryCodes[country], f(inv.price), inv.supplier)
		}

		text += "\n"
	}

	plus := string('➕')
	text += fmt.Sprintf("%s  %s  %s\u20BD  &#8212  %s\n", plus, s.countryCodes[obj.Country], f(obj.Price), obj.Supplier)

	text += "\n"
	text += fmt.Sprintf("%s  @gorbushka_bot\n\n", "🤖")

	return text
}

func (s *service) makeUpdateMessage(obj *entity.Event, mp map[string][]info, oldPrice string) string {

	inverseMap := convert(mp)

	text := "<i>* ПОСТАВЩИКИ ОБНОВИЛИ ЦЕНУ</i>\n\n"
	text += fmt.Sprintf("<u>%s</u>\n\n", obj.ModelId)

	for country, inversArr := range inverseMap {
		for _, inv := range inversArr {
			if country == obj.Country && inv.supplier == obj.Supplier && inv.price == obj.Price {
				text += fmt.Sprintf("%s  <del style=\"text-decorator: line-through;\">%s\u20BD</del> &#8212&#062\n&#8212&#062%s %s\u20BD &#8212 %s\n",
					s.countryCodes[country], f(oldPrice), s.countryCodes[country], f(inv.price), inv.supplier)
				continue
			}
			text += fmt.Sprintf("%v  %s\u20BD  &#8212  %s\n", s.countryCodes[country], f(inv.price), inv.supplier)
		}
		text += "\n"
	}

	text += fmt.Sprintf("%s  @gorbushka_bot\n\n", "🤖")

	return text
}

func (s *service) makeDeleteMessage(obj *entity.Event, mp map[string][]info) string {
	inverseMap := convert(mp)

	text := "<i>* ПОСТАВЩИКИ УДАЛИЛИ ТОВАР</i>\n\n"
	text += fmt.Sprintf("<u>%s</u>\n\n", obj.ModelId)

	for country, inversArr := range inverseMap {
		for _, inv := range inversArr {
			text += fmt.Sprintf("%v  %s\u20BD  &#8212  %s\n", s.countryCodes[country], f(inv.price), inv.supplier)
		}
		text += "\n"
	}

	text += fmt.Sprintf("<del style=\"text-decorator: line-through;\">%v  %s  &#8212  %s</del>\n",
		s.countryCodes[obj.Country], f(obj.Price), obj.Supplier)

	text += "\n"
	text += fmt.Sprintf("%s  @gorbushka_bot\n\n", "🤖")

	return text
}

func (s *service) initTgMessage(users []int, text string) []telegram.Event {
	messages := make([]telegram.Event, 0)

	for _, user := range users {
		var event telegram.Event
		event.ChatID = user
		event.Text = text
		messages = append(messages, event)
	}

	return messages
}

func (s *service) defineUsers(obj *entity.Event) ([]int, error) {
	users, err := s.db.GetAllByModelTypeAndModelId(obj.ModelType, obj.ModelId)
	if err != nil {
		return nil, err
	}

	return users, nil
}

type inverse struct {
	supplier string
	price    string
}

func convert(mp map[string][]info) map[entity.Country][]inverse {
	newMap := make(map[entity.Country][]inverse)

	for supplier, infoArr := range mp {

		for _, inf := range infoArr {

			inverseArr, ok := newMap[inf.country]
			if !ok {
				inverseArr = []inverse{}
			}

			inverseArr = append(inverseArr, inverse{
				supplier: supplier,
				price:    inf.price,
			})

			newMap[inf.country] = inverseArr
		}
	}

	for _, invArr := range newMap {
		sort.Slice(invArr, func(i, j int) bool {
			return invArr[i].price < invArr[j].price
		})
	}

	return newMap
}

func getCountryTable() map[entity.Country]string {
	mp := make(map[entity.Country]string, 0)
	mp[entity.USA] = "🇺🇸"
	mp[entity.RUSSIA] = "🇷🇺"

	return mp
}

func f(price string) string {

	newString := ""

	if len(price) < 4 {
		return price
	}

	if len(price) >= 4 && len(price) <= 6 {
		part1 := price[:len(price)-3]
		part2 := price[len(price)-3:]

		newString += part1
		newString += "."
		newString += part2

		return newString
	}

	if len(price) == 7 {
		part1 := price[0]
		part2 := price[1:4]
		part3 := price[4:]

		newString += string(part1)
		newString += "."
		newString += part2
		newString += "."
		newString += part3

		return newString
	}

	return price
}
