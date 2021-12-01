<?php

declare(strict_types=1);

namespace Oxylabs\DatacenterApi;

use Exception;
use GuzzleHttp\Client;
use GuzzleHttp\Exception\RequestException;
use GuzzleHttp\Promise\PromiseInterface;
use Psr\Http\Message\ResponseInterface;

class WebsiteScraper
{
    private const RESPONSE_SUCCESS = 200;

    public function __construct(
        private Client $client,
        private FileManager $fileManager,
    ) {
    }

    public function scrapeAsync(int $position, string $proxy, string $url): PromiseInterface
    {
        return
            $this
                ->client
                ->requestAsync('GET', $url, [
                    'proxy' => $proxy,
                    'headers' => getRandomBrowserHeaders(),
                ])
                ->then(
                    function (ResponseInterface $response) use ($position, $proxy, $url) {
                        if ($response->getStatusCode() === static::RESPONSE_SUCCESS) {
                            $this->fileManager->writeSuccess($position, $response->getBody()->getContents());

                            return;
                        }

                        $this->fileManager->writeError("$url - Response code {$response->getStatusCode()}");
                        $this->retryOnFailure($position, $proxy, $url);
                    },
                    function ($exception) use ($position, $proxy, $url) {
                        $this->fileManager->writeError("$url - Error {$exception->getMessage()}");
                        $this->retryOnFailure($position, $proxy, $url);
                    }
                );
    }

    private function retryOnFailure($position, $proxy, $url): void
    {
        $retries = 1;

        do {
            $response = null;
            try {
                $response = $this->client->request('GET', $url, [
                    'proxy' => $proxy,
                    'headers' => getRandomBrowserHeaders(),
                ]);

                if ($response->getStatusCode() !== self::RESPONSE_SUCCESS) {
                    $this->fileManager->writeError("$url - Response code {$response->getStatusCode()}");
                }
            } catch (RequestException $requestException) {
                $this->fileManager->writeError("$url - Response code {$requestException->getCode()}");
                $response = $requestException->getResponse();
            } catch (Exception $exception) {
                $this->fileManager->writeError("$url - Response code {$exception->getCode()}");
            }

            ++$retries;
        } while (!$response || ($response->getStatusCode() !== self::RESPONSE_SUCCESS && $retries < RETRIES_NUM));

        if ($response->getStatusCode() === self::RESPONSE_SUCCESS) {
            $this->fileManager->writeSuccess($position, $response->getBody()->getContents());
        }
    }
}
