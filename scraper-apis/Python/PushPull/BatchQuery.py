from pprint import pprint

from PushPull import PushPull

if __name__ == "__main__":
    # Create a PushPull object. You can find the definition in the PushPull.py file.
    # Replace the values with your own credentials.
    pp = PushPull("user", "pass1")

    # Specify the payload for our batch job.
    payload = {
        "query": [
            "kettle",
            "fridge",
            "microwave",
        ],
        "source": "amazon_search",
        "geo_location": "10005",
        "parse": "true",
        # "callback_url": "https://your.callback.url",  # Replace with your own callback URL
    }

    # Create a batch job.
    batch_query = pp.create_batch_query(payload)

    # Extract results for each job.
    # For simplicity sake, we are are not using asynchronous execution.
    # Note: if you provided a `callback_url`, these jobs will also be proccessed by your callback endpoint.
    batch_results = [
        pp.extract_job_results(query["id"]) for query in batch_query["queries"]
    ]

    pprint(batch_results)
