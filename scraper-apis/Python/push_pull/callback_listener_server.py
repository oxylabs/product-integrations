"""
This is a simple Sanic web server that will listen for the callback.
You will need to run this on a server that can receive requests from Oxylabs callbackers.

You can find the callbacker IPs by calling the `get_callbacker_ips()` method
from our `PushPullScraperAPIsClient` class or running the `get_callbacker_ips.py`.
"""
from pprint import pprint
from sanic import Sanic, response

from client import PushPullScraperAPIsClient

# Instantiate a PushPullScraperAPIsClient.
# Replace the values with your own credentials.
pp = PushPullScraperAPIsClient("user", "pass")
app = Sanic(name="callback_listener")

# Define /callback_listener endpoint that accepts POST requests.
@app.route("/callback_listener", methods=["POST"])
async def job_listener(request):
    res = request.json
    pprint(res)

    # Get job results from the ID provided in the callback payload.
    job_results = pp.get_job_results(res["id"])
    pprint(job_results)

    return response.json(status=200, body={"status": "ok"})


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8080)
