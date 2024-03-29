# Web Unblocker Java Example

This example demonstrates how to use [Web Unblocker API](https://developers.oxylabs.io/advanced-proxy-solutions/web-unblocker)

## Global variables

Set up the script using the following constants (see Settings.java)

* USERNAME (String) - Username of a proxy user
* PASSWORD (String) - Password of a proxy user
* TIMEOUT (Integer) - Seconds to wait for a connection and data retrieval until timing out
* REQUESTS_RATE (Integer) - Number of requests to make per one second
* RETRIES_NUM (Integer) - Number of times to retry if initial request was unsuccessful
* URL_LIST_NAME (String) - Filename of a txt file with the URLs that needs to scraped
* PROXY_ADDRESS (String) - Web Unblocker proxy endpoint.

## Prerequisites

The following tools need to be present on your system
* java 11.0.11

## How to run the script

Compile and execute the script:
```
$ mvm package
$ java -jar target/residential-1.0-SNAPSHOT-jar-with-dependencies.jar
```
