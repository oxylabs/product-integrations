package main

import (
	"regexp"
)

var compiledProxyRegex = regexp.MustCompile(ProxyRegex)

func matchProxyUrl(proxy string) map[string]string {
	match := compiledProxyRegex.FindStringSubmatch(proxy)
	result := make(map[string]string)
	if match == nil {
		return result
	}

	for i, name := range compiledProxyRegex.SubexpNames() {
		if i != 0 && name != "" {
			result[name] = match[i]
		}
	}

	return result
}
