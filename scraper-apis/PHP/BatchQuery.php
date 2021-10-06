<?php

/* This example will submit a job request to E-commerce Universal Scraper API.
The job will deliver parsed product data in JSON from multiple books.toscrape.com product pages
from United States geo-location*/

//If you wish to get content in HTML you can delete parser_type and parse parameters
$params = [
    'url' => [
        'https://books.toscrape.com/catalogue/a-light-in-the-attic_1000/index.html',
        'https://books.toscrape.com/catalogue/tipping-the-velvet_999/index.html',
        'https://books.toscrape.com/catalogue/soumission_998/index.html'
    ],
    'source' => 'universal_ecommerce',
    'geo_location' => 'United States',
    'parser_type' => 'ecommerce_product',
    'parse' => 'true',
];

$ch = curl_init();

curl_setopt($ch, CURLOPT_URL, "https://data.oxylabs.io/v1/queries/batch");
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($ch, CURLOPT_POSTFIELDS, $params);
curl_setopt($ch, CURLOPT_POST, 1);
curl_setopt($ch, CURLOPT_USERPWD, "user" . ":" . "pass1"); //Don't forget to fill in user credentials

$headers = array();
$headers[] = "Content-Type: application/json";
curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);

$result = curl_exec($ch);
//To retrieve parsed or raw content from the webpage, use _links from the response dictionary and check RetrieveJobContent.php file
echo $result;

if (curl_errno($ch)) {
    echo 'Error:' . curl_error($ch);
}
curl_close ($ch);
?>