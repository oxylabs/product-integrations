<?php

$ch = curl_init();

curl_setopt($ch, CURLOPT_URL, 'https://data.oxylabs.io/v1/info/callbacker_ips');
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'GET');
curl_setopt($ch, CURLOPT_USERPWD, 'user' . ':' . 'pass1');  //Don't forget to fill in user credentials

$result = curl_exec($ch);
echo $result;

if (curl_errno($ch)) {
    echo 'Error:' . curl_error($ch);
}
curl_close($ch);
