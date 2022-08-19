"""
This is a simple Sanic web server that will listen for the callback.
You will need to run this on a server that can receive requests from Oxylabs notifiers.

You can find the notifier IPs by calling the get_notifier_ips() method from our PushPull class
 or running the GetNotiferIPList.py.
"""
from pprint import pprint

from PushPull import PushPull
from sanic import Sanic, response

# Create a PushPull object. You can find the definition in the PushPull.py file.
# Replace the values with your own credentials.
pp = PushPull("user", "pass1")

app = Sanic(name="pushpull")

# Define /job_listener endpoint that accepts POST requests.
@app.route("/job_listener", methods=["POST"])
async def job_listener(request):
    res = request.json
    pprint(res)

    # Get job results from provided ID
    job_results = pp.get_job_results(res["id"])
    pprint(job_results)

    return response.json(status=200, body={"status": "ok"})


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8080)
