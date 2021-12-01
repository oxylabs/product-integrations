package main

import (
	"bufio"
	"context"
	"errors"
	"fmt"
	"golang.org/x/time/rate"
	"net/http"
	"net/url"
	"time"
)

const ApiUrl = "https://proxy.oxylabs.io/all"

type ApiClient struct {
	client      *http.Client
	rateLimiter *rate.Limiter
}

func NewClient(apiRateLimit *rate.Limiter) *ApiClient {
	return &ApiClient{
		client:      &http.Client{Timeout: Timeout * time.Second},
		rateLimiter: apiRateLimit,
	}
}

func (c *ApiClient) request(req *http.Request) (*http.Response, error) {
	return c.requestWithClient(req, c.client)
}

func (c *ApiClient) requestWithClient(req *http.Request, httpClient *http.Client) (*http.Response, error) {
	ctx := context.Background()
	err := c.rateLimiter.Wait(ctx)
	if err != nil {
		return nil, err
	}
	resp, err := httpClient.Do(req)
	if err != nil {
		return nil, err
	}

	return resp, nil
}

func (c *ApiClient) fetchPage(proxy string, targetUrl string) (*http.Response, error) {
	proxyUrl, err := url.Parse(proxy)
	if err != nil {
		return nil, err
	}

	httpClient := &http.Client{
		Transport: &http.Transport{Proxy: http.ProxyURL(proxyUrl)},
		Timeout:   Timeout * time.Second,
	}
	apiRequest, _ := http.NewRequest("GET", targetUrl, nil)

	browserHeaders := getRandomBrowserHeaders()
	for headerKey, headerValue := range browserHeaders {
		apiRequest.Header.Set(headerKey, headerValue.(string))
	}

	return c.requestWithClient(apiRequest, httpClient)
}

func (c ApiClient) fetchProxies() ([]interface{}, error) {
	apiRequest, _ := http.NewRequest("GET", ApiUrl, nil)
	apiRequest.SetBasicAuth(Username, Password)

	apiResponse, err := c.request(apiRequest)
	if err != nil {
		return nil, err
	}

	if apiResponse.StatusCode != 200 {
		return nil, errors.New(
			fmt.Sprintf(
				"Invalid status code, expected 200; got %v",
				apiResponse.StatusCode,
			),
		)
	}

	var lines []interface{}
	scanner := bufio.NewScanner(apiResponse.Body)
	for scanner.Scan() {
		lines = append(lines, scanner.Text())
	}

	return lines, scanner.Err()
}
