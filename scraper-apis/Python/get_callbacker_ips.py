import requests
from pprint import pprint


def get_callbacker_ips() -> dict:
    # Get response from the callbacker IPs endpoint.
    response = requests.request(
        method="GET",
        url="https://data.oxylabs.io/v1/info/callbacker_ips",
        auth=("user", "pass"),
    )

    # Return the response JSON.
    return response.json()


if __name__ == "__main__":
    callbacker_ips = get_callbacker_ips()
    # Print the prettified JSON response to stdout.
    pprint(callbacker_ips)
