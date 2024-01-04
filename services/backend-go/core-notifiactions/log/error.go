package log

import "fmt"

type exception struct {
	error
	*stack
	msg string
}

func Err(msg string, err error) error {
	return &exception{
		error: err,
		stack: callers(),
		msg:   msg,
	}
}

func (e *exception) Error() string {
	sttr := e.stack.StackTrace()
	res := fmt.Sprintf("[ERROR]: %v\n\n[message]: %s\n\n", e.error, e.msg)
	for _, item := range sttr {
		res += fmt.Sprintf("%s\n\t%s: %d\n\n", item.name(), item.file(), item.line())
	}
	return res
}

func WrapIfErr(msg string, err error) error {
	if err != nil {
		return Err(msg, err)
	}
	return nil
}
