import requests
from pprint import pprint

# Get response from the callback IPs endpoint.
response = requests.request(
    method='GET',
    url='https://data.oxylabs.io/v1/info/callbacker_ips',
    auth=('user', 'pass1'),
)

# Print prettified JSON response to stdout.
pprint(response.json())