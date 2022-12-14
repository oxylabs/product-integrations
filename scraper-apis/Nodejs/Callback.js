// Simple express server with an endpoint listening for POST requests
const axios = require('axios');
const express = require('express');
const bodyParser = require('body-parser');

const credentials = {
  auth: {
    username: 'user',
    password: 'pass1'
  },
  headers: {
    'Content-Type': 'application/json',
    'Accept-Encoding': 'gzip,deflate,compress',
  }
}

const app = express();
const port = 8080;

app.use(bodyParser.json());

app.post('/job_listener', (req, res) => {
  const links = req.body._links
  const resultLinks = links.filter(link => link.rel === 'results');

  // create an array of promises from filtered links
  const requests = resultLinks.map(({ href }) => axios.get(href, {...credentials}));

  Promise.all(requests).then(values => {
    console.log(values);
    res.status(200).json({ status: 'ok '});
  })
  .catch(errors => console.log(errors));
});

app.listen(port);
