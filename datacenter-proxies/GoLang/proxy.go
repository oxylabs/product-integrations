package main

import "fmt"

func formatProxy(proxy string) string {
	return fmt.Sprintf("http://%s:%s@%s", Username, Password, proxy)
}
