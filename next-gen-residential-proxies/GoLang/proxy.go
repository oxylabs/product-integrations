package main

import (
	"fmt"
	"strings"
)

func createProxyByUrl(url string) (string, *string, string) {
	var country *string
	parsedUrl := url

	urlParts := strings.Split(url, ";")
	if len(urlParts) == 2 {
		parsedUrl = urlParts[0]
		country = &urlParts[1]
	}

	return parsedUrl, country, fmt.Sprintf(
		"http://%s:%s@%s",
		Username,
		Password,
		ProxyAddress,
	)
}
