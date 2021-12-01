<?php

declare(strict_types=1);

namespace Oxylabs\DatacenterApi;

use Exception;
use GuzzleHttp\Client;

class DatacenterApiClient
{
    private const API_URI = 'https://proxy.oxylabs.io/all';

    public function __construct(
        private Client $client,
        private ConsoleWriter $consoleWriter,
        private string $username,
        private string $password,
    ) {
    }

    public function getProxyList(): array
    {
        $this->consoleWriter->writeln('Retrieving proxy list...');
        $contents = $this->fetchContents();
        $proxyIps = $this->parseIps($contents);

        return $this->addCredentials($proxyIps);
    }

    protected function fetchContents(): string
    {
        try {
            return $this->client->get(static::API_URI, [
                'auth' => [
                    $this->username,
                    $this->password,
                ],
            ])
                ->getBody()
                ->getContents();
        } catch (Exception) {
            $this->consoleWriter->writelnError('Failed to retrieve proxy list');
            exit(1);
        }
    }

    protected function parseIps(string $contents): array
    {
        return array_filter(explode(PHP_EOL, $contents));
    }

    protected function addCredentials(array $proxyIps): array
    {
        return array_map(
            fn($proxyIp) => sprintf(
                'http://%s:%s@%s',
                $this->username,
                $this->password,
                $proxyIp
            ),
            $proxyIps
        );
    }
}
