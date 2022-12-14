"""
This example submits a job request to E-Commerce Scraper API.
The job will deliver parsed product data in JSON for multiple Amazon searches
from United States geo-location.
"""
import requests
from pprint import pprint


def submit_batch() -> dict:
    # If you wish to get content in HTML, you can set the `parse` to `False`.
    payload = {
        "query": [
            "kettle",
            "fridge",
            "microwave",
        ],
        "source": "amazon_search",
        "geo_location": "10005",
        "parse": True,
    }

    response = requests.request(
        "POST",
        "https://data.oxylabs.io/v1/queries/batch",
        auth=("user", "pass"),  # Don't forget to fill in user credentials.
        json=payload,
    )

    # Return the response JSON.
    # To retrieve parsed or raw content from the webpage, use `_links` from the
    # response dictionary and check `retrieve_job_content.py` file.
    return response.json()


if __name__ == "__main__":
    job_infos = submit_batch()
    # Print the prettified JSON response to stdout.
    pprint(job_infos)
