<?php
$username = 'USERNAME';
$password = 'PASSWORD';
$country = 'US';
$proxy = 'pr.oxylabs.io:7777';
$query = curl_init('https://ip.oxylabs.io');
curl_setopt($query, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($query, CURLOPT_PROXY, "http://$proxy");
curl_setopt($query, CURLOPT_PROXYUSERPWD, "customer-$username-cc-$country:$password");
$output = curl_exec($query);
curl_close($query);
if ($output)
    echo $output;
?>
