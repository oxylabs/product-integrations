This is a working example of the Push-Pull method. Use this example to create 
a single job or a batch query. 

You can find Push-Pull documentation here: 
[Push-Pull - Oxylabs Documentation](https://developers.oxylabs.io/scraper-apis/getting-started/api-reference/integration-methods/push-pull)

# Requirements

- Python 3.7+
- requests
- prettyprinter
- sanic

# Installing
Using pip:

```bash
$ pip install requests prettyprinter sanic
```

# Usage
You can edit the payload of these jobs 

For running a single job:
```bash
$ python single_job.py
```

For running a batch job:
```bash
$ python batch_query.py
```

If you specify a `callback_url` in the payload, you need a server that accepts 
requests from Oxylabs servers.
An example of the code that should be running is provided in the `callback.py` file.