import requests
from pprint import pprint


def get_job_info(job_id: int) -> dict:
    # Get response from the callback IPs endpoint.
    # Get response from stats endpoint.
    response = requests.request(
        method="GET",
        url=f"http://data.oxylabs.io/v1/queries/{job_id}",
        auth=("user", "pass"),
    )

    # Return the response JSON.
    return response.json()


if __name__ == "__main__":
    job_info = get_job_info(job_id=1234567890987654321)
    # Print the prettified JSON response to stdout.
    pprint(job_info)
