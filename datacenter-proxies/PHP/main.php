<?php

declare(strict_types=1);

require_once __DIR__ . '/vendor/autoload.php';

use GuzzleHttp\Client;
use GuzzleHttp\HandlerStack;
use GuzzleHttp\Promise\Utils;
use Oxylabs\DatacenterApi\ConsoleWriter;
use Oxylabs\DatacenterApi\DatacenterApiClient;
use Oxylabs\DatacenterApi\FileManager;
use Oxylabs\DatacenterApi\RoundRobinArrayWrapper;
use Oxylabs\DatacenterApi\WebsiteScraper;
use Spatie\GuzzleRateLimiterMiddleware\RateLimiterMiddleware;

const USERNAME = '';
const PASSWORD = '';
const TIMEOUT = 5;
const REQUESTS_RATE = 10;
const RETRIES_NUM = 3;
const URL_LIST_NAME = 'url_list_dc.txt';

$executionStart = microtime(true);

$consoleWriter = new ConsoleWriter();

$stack = HandlerStack::create();
$stack->push(RateLimiterMiddleware::perSecond(REQUESTS_RATE));

$client = new Client([
    'handler' => $stack,
    'timeout' => TIMEOUT,
]);

$fileManager = new FileManager(
    __DIR__,
    __DIR__,
    $consoleWriter,
);

$datacenterApiClient = new DatacenterApiClient(
    $client,
    $consoleWriter,
    USERNAME,
    PASSWORD,
);
$websiteScraper = new WebsiteScraper(
    $client,
    $fileManager,
);

$promises = [];
$urlList = $fileManager->readUrlList();

$proxyList = $datacenterApiClient->getProxyList();
$proxyListRoundRobin = new RoundRobinArrayWrapper($proxyList);

$consoleWriter->writeln('Gathering results...');
foreach ($urlList as $position => $url) {
    $proxy = $proxyListRoundRobin->fetchNext();
    $promises[] = $websiteScraper->scrapeAsync($position + 1, $proxy, trim($url));
}

Utils::settle($promises)->wait();

$timeElapsedInSeconds = microtime(true) - $executionStart;
$consoleWriter->writeln(sprintf('Script finished after %.2f', $timeElapsedInSeconds));
