<?php

/* This example will submit a job request to Scraper API.
The job will deliver parsed product data in JSON for Amazon searches
from United States geo-location*/

//If you wish to get content in HTML you can delete parser_type and parse parameters
$params = [
    'source' => 'amazon_search',
    'query' => 'kettle',
    'geo_location' => '10005',
    'parse' => true,
];

$ch = curl_init();

curl_setopt($ch, CURLOPT_URL, 'https://data.oxylabs.io/v1/queries');
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($params));
curl_setopt($ch, CURLOPT_POST, 1);
curl_setopt($ch, CURLOPT_USERPWD, 'user' . ':' . 'pass1');  //Don't forget to fill in user credentials

$headers = ['Content-Type: application/json'];
curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);

$result = curl_exec($ch);
//To retrieve parsed or raw content from the webpage, use _links from the response JSON and check RetrieveJobContent.php file
echo $result;

if (curl_errno($ch)) {
    echo 'Error:' . curl_error($ch);
}
curl_close($ch);
