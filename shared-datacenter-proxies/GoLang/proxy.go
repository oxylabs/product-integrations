package main

import (
	"fmt"
)

func createProxyByUrl(url string) (string, string) {
	return url, fmt.Sprintf(
		"http://customer-%s:%s@%s",
		Username,
		Password,
		ProxyAddress,
	)
}
