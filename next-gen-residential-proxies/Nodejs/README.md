# Web Unblocker NodeJs Example

This example demonstrates how to use [Web Unblocker API](https://developers.oxylabs.io/advanced-proxy-solutions/web-unblocker)

## Global variables

Setup the script using the following constants (see settings.js)

* Username (String) - Username of a proxy user
* Password (String) - Password of a proxy user
* Timeout (Integer) - Seconds to wait for a connection and data retrieval until timing out
* RequestsRate (Integer) - Number of requests to make per one second
* RetriesNum (Integer) - Number of times to retry if initial request was unsuccessful
* UrlListName (String) - Filename of a txt file with the URLs that needs to scraped
* ProxyAddress (String) - Web Unblocker proxy endpoint.

## Prerequisites

The following tools need to be present on your system
* node >= 14.18.1 
* npm >= 6.14.15 or yarn >= 1.22.17

## How to run the script

### Install dependencies
Using yarn
```
yarn install
```

Or by using npm
```
npm install
```

### Run the script
```
node main.js
```
