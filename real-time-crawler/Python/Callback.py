# This is a simple Sanic web server with a route listening for callbacks on localhost:8080.
# It will print job results to stdout.
import requests
from pprint import pprint
from sanic import Sanic, response


AUTH_TUPLE = ('user', 'pass1')

app = Sanic()


# Define /job_listener endpoint that accepts POST requests.
@app.route('/job_listener', methods=['POST'])
async def job_listener(request):
    try:
        res = request.json
        links = res.get('_links', [])
        for link in links:
            if link['rel'] == 'results':
                # Sanic is async, but requests are synchronous, to fully take
                # advantage of Sanic, use aiohttp.
                res_response = requests.request(
                    method='GET',
                    url=link['href'],
                    auth=AUTH_TUPLE,
                )
                pprint(res_response.json())
                break
    except Exception as e:
        print("Listener exception: {}".format(e))
    return response.json(status=200, body={'status': 'ok'})


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8080)