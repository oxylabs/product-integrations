# Web Unblocker PHP Example

This example demonstrates how to use [Web Unblocker API](https://developers.oxylabs.io/advanced-proxy-solutions/web-unblocker) 

## Global variables

Setup the script using the following constants

* USERNAME (String) - Username of a proxy user
* PASSWORD (String) - Password of a proxy user
* TIMEOUT (Integer) - Seconds to wait for a connection and data retrieval until timing out
* PROXY_ADDRESS (String) - Web Unblocker proxy endpoint.
* REQUESTS_RATE (Integer) - Number of requests to make per one second
* RETRIES_NUM (Integer) - Number of times to retry if initial request was unsuccessful
* URL_LIST_NAME (String) - Filename of a txt file with the URLs that needs to scraped

## Prerequisites

The following tools need to be present on your system
* composer >= 2
* php >= 8.0

## How to run the script

Execute the main file:
```
$ php main.php
```
