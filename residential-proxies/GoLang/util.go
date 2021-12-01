package main

import (
	"fmt"
	"math/rand"
	"os"
	"reflect"
)

func printAndExit(message string) {
	fmt.Println(message)
	os.Exit(1)
}

func getRandomMapKey(mapVariable interface{}) interface{} {
	keys := reflect.ValueOf(mapVariable).MapKeys()

	return keys[rand.Intn(len(keys))].Interface()
}
