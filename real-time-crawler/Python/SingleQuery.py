import requests
from pprint import pprint

# Structure payload.
payload = {
    'source': 'universal',
    'url': 'http://ip.oxylabs.io',
}

# Get response.
response = requests.request(
    'POST',
    'https://data.oxylabs.io/v1/queries',
    auth=('user', 'pass1'),
    json=payload,
)

# Print prettified response to stdout.
pprint(response.json())