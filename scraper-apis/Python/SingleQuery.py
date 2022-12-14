# This example will submit a job request to Scraper API.
# The job will deliver parsed product data in JSON for Amazon searches
# from United States geo-location

import requests
from pprint import pprint

# Structure payload.
# If you wish to get content in HTML you can delete parser_type and parse parameters
payload = {
    "source": "amazon_search",
    "query": "kettle",
    "geo_location": "10005",
    "parse": "true",
}

# Get response.
response = requests.request(
    'POST',
    'https://data.oxylabs.io/v1/queries',
    auth=('user', 'pass1'), # Don't forget to fill in user credentials
    json=payload,
)

# Print prettified response to stdout.
# To retrieve parsed or raw content from the webpage, use _links from the response dictionary and check RetrieveJobContent.py file
pprint(response.json())
