package service

import (
	"corenotif/model/entity"
	"sync"
)

type info struct {
	country entity.Country
	price   string
}

type cache struct {
	data  map[string]map[string][]info
	lock  sync.Mutex
	locks map[string]*sync.RWMutex
}

func (c *cache) Set(outer, inner string, value info) {
	c.getLock(outer).Lock()
	defer c.getLock(outer).Unlock()

	innerMap, ok := c.data[outer]
	if !ok {
		innerMap = make(map[string][]info)
		c.data[outer] = innerMap
	}

	for _, inf := range innerMap[inner] {
		if inf.country == value.country {
			return
		}
	}

	innerMap[inner] = append(innerMap[inner], value)
}

func (c *cache) Update(outer, inner string, value info) {
	c.getLock(outer).Lock()
	defer c.getLock(outer).Unlock()

	if innerMap, ok := c.data[outer]; ok {
		if values, ok := innerMap[inner]; ok {
			for i := range values {
				if values[i].country == value.country {
					values[i].price = value.price
					return
				}
			}
		}
	}
}

func (c *cache) Delete(outer, inner string, value info) {
	c.getLock(outer).Lock()
	defer c.getLock(outer).Unlock()

	if c.data == nil {
		return
	}

	innerMap, ok := c.data[outer]
	if !ok {
		return
	}

	if arr, ok := innerMap[inner]; ok {
		if len(arr) == 1 {
			delete(innerMap, inner)
		} else {
			var idx int
			for i, v := range arr {
				if v.country == value.country {
					idx = i
					break
				}
			}

			arr = append(arr[:idx], arr[idx+1:]...)
			innerMap[inner] = arr
		}

		if len(innerMap) == 0 {
			delete(c.data, outer)
		}
	}
}

func (c *cache) Get(outer string) *map[string][]info {
	c.getLock(outer).RLock()
	defer c.getLock(outer).RUnlock()

	if innerMap, ok := c.data[outer]; ok {
		return &innerMap
	}

	return nil
}

func (c *cache) getLock(key string) *sync.RWMutex {
	c.lock.Lock()
	defer c.lock.Unlock()

	l, ok := c.locks[key]
	if !ok {
		l = &sync.RWMutex{}
		c.locks[key] = l
	}

	return l
}
