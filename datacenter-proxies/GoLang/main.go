package main

import (
	"fmt"
	"github.com/sbabiv/roundrobin"
	"golang.org/x/time/rate"
	"sync"
	"time"
)

func main() {
	start := time.Now()

	fmt.Println("Reading from the list...")
	urls, err := readLines(UrlListName)
	if err != nil {
		printAndExit("Failed to read the input file")
	}

	fmt.Println("Retrieving proxy list...")
	apiRateLimit := rate.NewLimiter(rate.Every(time.Second), RequestsRate)
	apiClient := NewClient(apiRateLimit)
	proxies, err := apiClient.fetchProxies()
	if err != nil {
		printAndExit("Failed to download proxy list")
	}

	proxiesRoundRobin := roundrobin.New(proxies)
	wc := sync.WaitGroup{}

	scraper := NewScraper(apiClient)

	fmt.Println("Gathering results...")
	for index, url := range urls {
		wc.Add(1)
		go func(url string, position int) {
			proxy, _ := proxiesRoundRobin.Pick()
			formattedProxy := formatProxy(proxy.(string))
			scraper.scrape(position, formattedProxy, url)
			wc.Done()
		}(url, index+1)
	}

	wc.Wait()

	elapsed := time.Since(start)
	fmt.Printf("Script finished after %.2fs\n", elapsed.Seconds())
}
