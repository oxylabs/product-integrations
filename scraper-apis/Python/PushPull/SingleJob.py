from pprint import pprint

from PushPull import PushPull

if __name__ == "__main__":
    # Create a PushPull object. You can find the definition in the PushPull.py file.
    # Replace the values with your own credentials.
    pp = PushPull("user", "pass1")

    # Specify the payload for our job.
    payload = {
        "source": "google_search",
        "query": "large shoes",
        "geo_location": "United States",
        "parse": True,
        # "callback_url": "https://your.callback.url",  # Replace with your own callback URL
    }

    # Create a job and extract the results using the job ID.
    job = pp.create_job(payload)
    job_results = pp.extract_job_results(job["id"])
    pprint(job_results)
