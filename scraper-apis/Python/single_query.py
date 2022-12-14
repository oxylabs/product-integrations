"""
# This example will submit a job request to the E-Commerce Scraper API.
# The job will deliver parsed product data in a JSON for Amazon searches
# from United States geo-location.
"""
import requests
from pprint import pprint


def submit() -> dict:
    # Compose a Job request payload.
    # If you wish to get content in HTML, you can set the `parse` to `False`.
    payload = {
        "source": "amazon_search",
        "query": "kettle",
        "geo_location": "10005",
        "parse": True,
    }

    # Get response.
    response = requests.request(
        "POST",
        "https://data.oxylabs.io/v1/queries",
        auth=("user", "pass"),  # Don't forget to fill in user credentials.
        json=payload,
    )

    # Return the response JSON.
    # To retrieve parsed or raw content from the webpage, use `_links` from the
    # response dictionary and check `retrieve_job_content.py` file.
    return response.json()


if __name__ == "__main__":
    job_info = submit()
    # Print the prettified JSON response to stdout.
    pprint(job_info)
