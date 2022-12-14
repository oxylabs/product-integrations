/* This example will submit a job request to Scraper API.
The job will deliver parsed product data in JSON for multiple Amazon searches
from United States geo-location*/

const axios = require('axios');

//If you wish to get content in HTML you can delete parser_type and parse parameters
const payload = {
  'query': [
    'kettle',
    'fridge',
    'microwave'
  ],
  'source': 'amazon_search',
  'geo_location': '10005',
  'parse': 'true',
}

axios.post('https://data.oxylabs.io/v1/queries/batch', payload, {
  auth: {
    username: 'user', //Don't forget to fill in user credentials
    password: 'pass1'
  },
  ContentType: 'application/json'
})
.then(({ data }) => console.log(data))
.catch(err => console.log(err))
//To retrieve parsed or raw content from the webpage, use _links from the response dictionary and check RetrieveJobContent.js file
