package main

import (
	"context"
	"golang.org/x/time/rate"
	"net/http"
	"net/url"
	"time"
)

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
