# NextGen Residential Proxies Golang Example

This example demonstrates how to use [nextgen residential proxy API](https://developers.oxylabs.io/next-gen-residential-proxies/index.html#quick-start) 

## Global variables

Setup the script using the following constants

* Username (String) - Username of a proxy user
* Password (String) - Password of a proxy user
* Timeout (Integer) - Seconds to wait for a connection and data retrieval until timing out
* RequestsRate (Integer) - Number of requests to make per one second
* RetriesNum (Integer) - Number of times to retry if initial request was unsuccessful
* UrlListName (String) - Filename of a txt file with the URLs that needs to scraped
* ProxyAddress (String) - NextGen residential proxy endpoint.

## Prerequisites

The following tools need to be present on your system
* go 1.16

## How to run the script

Install dependencies
```
go get ./...
```

Compile and execute the script:
```
go run *.go
```

