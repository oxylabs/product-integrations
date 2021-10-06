/* This example will submit a job request to E-commerce Universal Scraper API.
The job will deliver parsed product data in JSON from multiple books.toscrape.com product pages
from United States geo-location*/

const axios = require('axios');
const fs = require('fs');

//If you wish to get content in HTML you can delete parser_type and parse parameters
const payload = {  
  "url":[  
     "https://books.toscrape.com/catalogue/a-light-in-the-attic_1000/index.html",
     "https://books.toscrape.com/catalogue/tipping-the-velvet_999/index.html",
     "https://books.toscrape.com/catalogue/soumission_998/index.html"
  ],
  "source": "universal_ecommerce",
  "geo_location": "United States",
  "parser_type": "ecommerce_product",
  "parse": "true",
}

axios.post('https://data.oxylabs.io/v1/queries/batch', payload, {
  auth: {
    username: 'user', //Don't forget to fill in user credentials
    password: 'pass1'
  },
  ContentType: "application/json"
})
.then(({ data }) => console.log(data))
.catch(err => console.log(err))
//To retrieve parsed or raw content from the webpage, use _links from the response dictionary and check RetrieveJobContent.js file