package log

import "fmt"

const (
	SUCCESS = iota
	INFO
	WARNING
)

type info struct {
	status  int
	message string
}

func newInfo(status int, message string) *info {
	return &info{
		status:  status,
		message: message,
	}
}

func (i *info) Print() {
	var st string

	switch i.status {
	case 0:
		st = "[SUCESS]"
	case 1:
		st = "[INFO]"
	case 2:
		st = "[WARNING]"
	}

	fmt.Printf("%s: %s\n", st, i.message)
}

func Log(status int, message string) {
	newInfo(status, message).Print()
}
