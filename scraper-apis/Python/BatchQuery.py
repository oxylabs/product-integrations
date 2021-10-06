# This example will submit a job request to E-commerce Universal Scraper API.
# The job will deliver parsed product data in JSON from multiple books.toscrape.com product pages
# from United States geo-location

import requests
import json
from pprint import pprint

# If you wish to get content in HTML you can delete parser_type and parse parameters
payload = {  
  "url":[  
     "https://books.toscrape.com/catalogue/a-light-in-the-attic_1000/index.html",
     "https://books.toscrape.com/catalogue/tipping-the-velvet_999/index.html",
     "https://books.toscrape.com/catalogue/soumission_998/index.html"
  ],
  "source": "universal_ecommerce",
  "geo_location": "United States",
  "parser_type": "ecommerce_product",
  "parse": "true",
}

response = requests.request(
    'POST',
    'https://data.oxylabs.io/v1/queries/batch',
    auth=('user', 'pass1'), #Don't forget to fill in user credentials
    json=payload,
)

# Print prettified response.
# To retrieve parsed or raw content from the webpage, use _links from the response dictionary and check RetrieveJobContent.py file
pprint(response.json())