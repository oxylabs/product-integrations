<?php

declare(strict_types=1);

require_once __DIR__ . '/vendor/autoload.php';

use GuzzleHttp\Client;
use GuzzleHttp\HandlerStack;
use GuzzleHttp\Promise\Utils;
use Oxylabs\SharedDcApi\ConsoleWriter;
use Oxylabs\SharedDcApi\FileManager;
use Oxylabs\SharedDcApi\ProxyFormatter;
use Oxylabs\SharedDcApi\WebsiteScraper;
use Spatie\GuzzleRateLimiterMiddleware\RateLimiterMiddleware;

const USERNAME = '';
const PASSWORD = '';
const TIMEOUT = 5;
const REQUESTS_RATE = 5;
const RETRIES_NUM = 3;
const URL_LIST_NAME = 'url_list_shared_dc.txt';
const PROXY_LIST_NAME = 'proxy_list_shared_dc.txt';

const PROXY_REGEX = '/^dc\.(?<country>\w{2})-?pr\.oxylabs\.io:\d+$/';
const DEFAULT_PROXY_INDEX_NAME = 'DEFAULT';

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

$websiteScraper = new WebsiteScraper(
    $client,
    $fileManager,
);

$promises = [];
$urlList = $fileManager->readUrlList();
$proxyMap = $fileManager->readProxyMap();
$proxyFormatter = new ProxyFormatter();

$consoleWriter->writeln('Gathering results...');
foreach ($urlList as $position => $url) {
    [$parsedUrl, $proxy] = $proxyFormatter->formatByUrl($proxyMap, $url);
    $promises[] = $websiteScraper->scrapeAsync($position + 1, $proxy, $parsedUrl);
}

Utils::settle($promises)->wait();

$timeElapsedInSeconds = microtime(true) - $executionStart;
$consoleWriter->writeln(sprintf('Script finished after %.2f', $timeElapsedInSeconds));
