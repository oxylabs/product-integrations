package main

import (
	"fmt"
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

	apiRateLimit := rate.NewLimiter(rate.Every(time.Second), RequestsRate)
	apiClient := NewClient(apiRateLimit)
	scraper := NewScraper(apiClient)

	wc := sync.WaitGroup{}

	fmt.Println("Gathering results...")
	for index, url := range urls {
		wc.Add(1)
		go func(url string, position int) {
			parsedUrl, country, formattedProxy := createProxyByUrl(url)
			scraper.scrape(position, formattedProxy, parsedUrl, country)
			wc.Done()
		}(url, index+1)
	}

	wc.Wait()

	elapsed := time.Since(start)
	fmt.Printf("Scraping finished after %.2fs\n", elapsed.Seconds())
}
