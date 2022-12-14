import requests
from pprint import pprint


def get_result(job_id: int) -> dict:
    # Get response from results endpoint.
    response = requests.request(
        method="GET",
        url=f"https://data.oxylabs.io/v1/queries/{job_id}/results",
        auth=("user", "pass"),
    )

    # Return the response JSON.
    return response.json()


if __name__ == "__main__":
    result = get_result(job_id=1234567890987654321)
    # Print the prettified JSON response to stdout.
    pprint(result)
