package main

import (
	"fmt"
	"golang.org/x/time/rate"
	"sync"
	"time"
)

func main() {
	start := time.Now()

	fmt.Println("Reading from the url list...")
	urls, err := readLines(UrlListName)
	if err != nil {
		printAndExit("Failed to read the url input file")
	}

	fmt.Println("Reading from the proxy list...")
	proxyMap, err := readProxyMap(ProxyListName)
	if err != nil {
		printAndExit("Failed to read the proxy input file")
	}

	fmt.Println("Retrieving proxy list...")
	apiRateLimit := rate.NewLimiter(rate.Every(time.Second), RequestsRate)
	apiClient := NewClient(apiRateLimit)
	if err != nil {
		printAndExit("Failed to download proxy list")
	}

	wc := sync.WaitGroup{}

	scraper := NewScraper(apiClient)

	fmt.Println("Gathering results...")
	for index, url := range urls {
		wc.Add(1)
		go func(url string, position int) {
			parsedUrl, formattedProxy := createProxyByUrl(proxyMap, url)
			scraper.scrape(position, formattedProxy, parsedUrl)
			wc.Done()
		}(url, index+1)
	}

	wc.Wait()

	elapsed := time.Since(start)
	fmt.Printf("Script finished after %.2fs\n", elapsed.Seconds())
}
