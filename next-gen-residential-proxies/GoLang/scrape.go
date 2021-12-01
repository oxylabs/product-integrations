package main

import "net/http"

type Scraper struct {
	apiClient *ApiClient
}

func NewScraper(apiClient *ApiClient) *Scraper {
	return &Scraper{apiClient}
}

func (s *Scraper) scrape(position int, proxy string, url string, country *string) {
	var response *http.Response
	var err error
	retry := 0

	for {
		response, err = s.apiClient.fetchPage(proxy, url, country)
		if response != nil && response.StatusCode != 200 {
			writeErrorToFile("%s - Response code %d", url, response.StatusCode)
		} else if err != nil {
			writeErrorToFile("%s - Response error %s", url, err)
			writeErrorToStdout("%s failed with error %s", url, err)
		}

		if response != nil && response.StatusCode == 200 {
			writeSuccessToFile(position, response.Body)
			break
		}

		retry += 1
		if retry >= RetriesNum {
			break
		}
	}
}
