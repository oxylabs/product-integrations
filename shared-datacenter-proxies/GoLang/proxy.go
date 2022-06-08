package main

import (
	"fmt"
	"strings"
)

func createProxyByUrl(proxyMap map[string]string, url string) (string, string) {
	urlParts := strings.Split(url, ";")
	proxyAddress, _ := proxyMap[DefaultProxyIndexName]

	if len(urlParts) == 2 {
		url = urlParts[0]
		country := urlParts[1]

		if countrySpecificProxy, ok := proxyMap[country]; ok {
			proxyAddress = countrySpecificProxy
		}
	}

	return url, fmt.Sprintf(
		"http://customer-%s:%s@%s",
		Username,
		Password,
		proxyAddress,
	)
}
