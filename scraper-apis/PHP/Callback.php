<?php
if (!isset($_POST)) {
    return;
}

$resultUrl = '';
$callbackPayload = json_decode(file_get_contents('php://input'), true);
foreach ($callbackPayload['_links'] as $link) {
    if ($link['rel'] === 'results') {
        $resultUrl = $link['href'];
        break;
    }
}

$ch = curl_init();

curl_setopt($ch, CURLOPT_URL, $resultUrl);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'GET');
curl_setopt($ch, CURLOPT_USERPWD, 'user' . ':' . 'pass1');  //Don't forget to fill in user credentials

$result = curl_exec($ch);

$stdout = fopen('php://stdout', 'w');
fwrite($stdout, $result);

if (curl_errno($ch)) {
    echo 'Error:' . curl_error($ch);
}
curl_close($ch);
