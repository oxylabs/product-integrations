"""
This is a simple Sanic web server that will listen for the callback.
You will need to run this on a server that can receive requests from Oxylabs callbackers.

You can find the callbacker IPs by calling the `get_callbacker_ips()` method
from our `PushPullScraperAPIsClient` class or running the `get_callbacker_ips.py`.
"""

import requests
from pprint import pprint
from sanic import Sanic, response

AUTH = ("user", "pass")

app = Sanic(name="callback_listener")


# Define `/callback_listener` endpoint that accepts POST requests.
@app.route("/callback_listener", methods=["POST"])
async def job_listener(request):
    try:
        res = request.json
        links = res.get("_links", [])
        for link in links:
            if link["rel"] == "results":
                # Sanic is async, but `requests` are synchronous, to fully take
                # advantage of Sanic, use `aiohttp` or `httpx.AsyncClient`.
                res_response = requests.request(
                    method="GET",
                    url=link["href"],
                    auth=AUTH,
                )
                pprint(res_response.json())
                break
    except Exception as e:
        print(f"Listener exception: {e!r}")

    return response.json(status=200, body={"status": "ok"})


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8080)
