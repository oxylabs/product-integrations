from pprint import pprint

from client import PushPullScraperAPIsClient


def run_example() -> dict:
    # Instantiate a PushPullScraperAPIsClient.
    # Replace the values with your own credentials.
    pp = PushPullScraperAPIsClient("user", "pass")

    # Specify the payload for the batch of the jobs.
    payload = {
        "query": [
            "kettle",
            "fridge",
            "microwave",
        ],
        "source": "amazon_search",
        "geo_location": "10005",
        "parse": True,
        # Replace with your own callback URL:
        # "callback_url": "https://your.callback.url",
    }

    # Create the jobs.
    batch_query = pp.create_jobs_batch(payload)

    # Extract results for each job.
    # For simplicity's sake, we are not using asynchronous execution.
    batch_results = [
        pp.wait_for_and_get_job_results(query["id"]) for query in batch_query["queries"]
    ]

    return batch_results


if __name__ == "__main__":
    result = run_example()
    pprint(result)
