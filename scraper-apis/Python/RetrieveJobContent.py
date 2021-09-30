import requests
from pprint import pprint

# Get response from stats endpoint.
response = requests.request(
    method='GET',
    url='http://data.oxylabs.io/v1/queries/12345678900987654321/results',
    auth=('user', 'pass1'),
)

# Print prettified JSON response to stdout.
pprint(response.json())