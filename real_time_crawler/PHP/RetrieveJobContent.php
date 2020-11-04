<?php

$ch = curl_init();

curl_setopt($ch, CURLOPT_URL, "http://data.oxylabs.io/v1/queries/12345678900987654321/results");
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "GET");
curl_setopt($ch, CURLOPT_USERPWD, "user" . ":" . "pass1");

$result = curl_exec($ch);
echo $result;

if (curl_errno($ch)) {
    echo 'Error:' . curl_error($ch);
}
curl_close ($ch);
?>