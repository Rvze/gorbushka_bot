package controller

import (
	"corenotif/config"
	"corenotif/model/entity"
	"corenotif/service"
	"encoding/json"
	"io/ioutil"
	"net/http"
	"strconv"

	"github.com/gin-gonic/gin"
)

type Controller struct {
	subscribeService service.SubscribeService
}

func GetController(subscribeService service.SubscribeService) Controller {
	return Controller{
		subscribeService: subscribeService,
	}
}

func (c *Controller) Start() {
	router := gin.Default()
	router.POST("/subscribe", c.subscribe)
	router.POST("/unsubscribe/:chatId", c.unsubscribe)

	go router.Run(config.GetHttpHost() + ":" + config.GetHttpPort())
}

func (cr *Controller) subscribe(c *gin.Context) {
	body, err := ioutil.ReadAll(c.Request.Body)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	var obj entity.Subscribe
	err = json.Unmarshal(body, &obj)

	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	err = cr.subscribeService.Subscribe(obj)

	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
}

func (cr *Controller) unsubscribe(c *gin.Context) {

	param := c.Param("chatId")
	chatId, err := strconv.Atoi(param)

	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
	}

	err = cr.subscribeService.UnSubscribe(chatId)

	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
}
