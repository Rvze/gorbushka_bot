package client

import (
	"corenotif/log"
	"fmt"
	"io"
	"net/http"
	"net/url"
	"path"
	"strconv"
)

const SendMessageMethod = "sendMessage"

type Client struct {
	host     string
	basePath string
	client   http.Client
}

func New(host, token string) *Client {
	return &Client{
		host:     host,
		basePath: newBasePath(token),
		client:   http.Client{},
	}
}

func newBasePath(token string) string {
	return "bot" + token
}

func (c *Client) SendMessage(chatID int, text string) error {
	q := url.Values{}
	q.Add("chat_id", strconv.Itoa(chatID))
	q.Add("text", text)
	q.Add("parse_mode", "HTML")

	_, err := c.doRequest(SendMessageMethod, q)
	if err != nil {
		err = log.Err("can't send message", err)
		fmt.Println(err)
		return err
	}

	log.Log(log.SUCCESS, "Message sent: "+text)

	return nil
}

func (c *Client) doRequest(method string, query url.Values) (data []byte, err error) {
	defer func() {
		err = log.WrapIfErr("can't do request", err)
		if err != nil {
			fmt.Println(err)
		}
	}()

	u := url.URL{
		Scheme: "https",
		Host:   c.host,
		Path:   path.Join(c.basePath, method),
	}

	req, err := http.NewRequest(http.MethodGet, u.String(), nil)

	if err != nil {
		return nil, err
	}

	req.URL.RawQuery = query.Encode()
	resp, err := c.client.Do(req)

	if err != nil {
		return nil, err
	}

	defer func() { _ = resp.Body.Close() }()

	body, err := io.ReadAll(resp.Body)
	if err != nil {
		return nil, err
	}

	return body, nil
}
