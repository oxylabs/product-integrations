# Shared Datacenter Proxies Python Example

This example demonstrates how to use [shared datacenter proxy API](https://developers.oxylabs.io/shared-dc/#quick-start) 

## Global variables

Set up the script using the following constants

* USERNAME (String) - Username of a proxy user
* PASSWORD (String) - Password of a proxy user
* TIMEOUT (Integer) - Seconds to wait for a connection and data retrieval until timing out
* REQUESTS_RATE (Integer) - Number of requests to make per one second
* RETRIES_NUM (Integer) - Number of times to retry if initial request was unsuccessful
* URL_LIST_NAME (String) - Filename of a txt file with the URLs that needs to scraped
* PROXY_LIST_NAME (String) - Filename of a txt file that contains country-specific proxy servers

## Prerequisites

The following tools need to be present on your system
* pipenv

## How to run the script

Install dependencies
```
$ pipenv install
```

Run the script inside the virtualenv
```
$ pipenv run python shared_dc.py
```
