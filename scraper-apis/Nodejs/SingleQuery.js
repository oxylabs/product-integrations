/* This example will submit a job request to Scraper API.
The job will deliver parsed product data in JSON for Amazon searches
from United States geo-location*/

const axios = require('axios');

//If you wish to get content in HTML you can delete parser_type and parse parameters
const payload = {
  'source': 'amazon_search',
  'query': 'kettle',
  'geo_location': '10005',
  'parse': 'true',
}

axios.post('https://data.oxylabs.io/v1/queries', payload, {
  auth: {
    username: 'user', //Don't forget to fill in user credentials
    password: 'pass1'
  },
  headers: {
    'Content-Type': 'application/json',
    'Accept-Encoding': 'gzip,deflate,compress',
  },
})
.then(({ data }) => console.log(data))
.catch(err => console.log(err))
//To retrieve parsed or raw content from the webpage, use _links from the response dictionary and check RetrieveJobContent.js file
