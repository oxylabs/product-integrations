#!/usr/bin/env python

#Full Next-gen residential proxies documentation: https://docs.oxylabs.io/next-gen-residential-proxies/index.html?python#introduction

import requests
from pprint import pprint

# Define proxy dict. Don't forget to put your real user and pass here as well.
proxies = {
  'http': 'http://USERNAME:PASSWORD@ngrp.oxylabs.io:60000',
  'https': 'https://USERNAME:PASSWORD@ngrp.oxylabs.io:60000',
}

headers = {
    "X-Oxylabs-Session-Id": "123randomString", # Header for session control
    "X-Oxylabs-Geo-Location": "Germany", # Header to choose proxy location. Full country list: https://docs.oxylabs.io/resources/universal-supported-geo_location-values.csv
    "Your-Custom-Header": "Custom header value", # Custom headers will be forwarded to the website
    "User-Agent": "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/73.0.3683.86 Chrome/73.0.3683.86 Safari/537.36", # User-Angents can specified by the user. If no user-agents will be specified, system will send one automatically
    "Cookie": "NID=1234567890; 1P_JAR=0987654321", # Cookie headers
    "X-Oxylabs-Status-Code": "500,501,502,503", # Specify response codes that would not trigger auto-retry if target returns custom codes
    "X-Oxylabs-Render": "html", # Website Javascript will be rendered. Change to "png" in order to receive a screenshot of rendered page.
}

response = requests.get(
    'https://ip.oxylabs.io',
    verify=False,  # It is required to ignore certificate
    proxies=proxies,
    headers=headers
)

# Print result page to stdout
pprint(response.text)