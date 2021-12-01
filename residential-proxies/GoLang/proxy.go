package main

import (
	"fmt"
	"strings"
)

func createProxyByUrl(url string) (string, string) {
	urlParts := strings.Split(url, ";")
	if len(urlParts) == 2 {
		return urlParts[0], fmt.Sprintf(
			"http://customer-%s-cc-%s:%s@%s",
			Username,
			urlParts[1],
			Password,
			ProxyAddress,
		)
	}

	return url, fmt.Sprintf(
		"http://customer-%s:%s@%s",
		Username,
		Password,
		ProxyAddress,
	)
}
