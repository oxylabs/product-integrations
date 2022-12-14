from pprint import pprint

from client import PushPullScraperAPIsClient


def run_example() -> dict:
    # Instantiate a PushPullScraperAPIsClient.
    # Replace the values with your own credentials.
    pp = PushPullScraperAPIsClient("user", "pass")

    # Specify the payload for our job.
    payload = {
        "source": "google_search",
        "query": "large shoes",
        "geo_location": "United States",
        "parse": True,
        # Replace with your own callback URL:
        # "callback_url": "https://your.callback.url",
    }

    # Create a job and extract the results using the ID from the `job_info`.
    job_info = pp.create_job(payload)
    job_result = pp.wait_for_and_get_job_results(job_info["id"])
    return job_result


if __name__ == "__main__":
    result = run_example()
    pprint(result)
