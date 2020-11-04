<?php
$stdout = fopen('php://stdout', 'w');

if (isset($_POST)) {
    $result = array_merge($_POST, (array) json_decode(file_get_contents('php://input')));

    $ch = curl_init();

    curl_setopt($ch, CURLOPT_URL, "https://data.oxylabs.io/v1/queries/".$result['id'].'/results');
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "GET");
    curl_setopt($ch, CURLOPT_USERPWD, "user" . ":" . "pass1");

    $result = curl_exec($ch);
    fwrite($stdout, $result);

    if (curl_errno($ch)) {
        echo 'Error:' . curl_error($ch);
    }
    curl_close ($ch);
}
?>