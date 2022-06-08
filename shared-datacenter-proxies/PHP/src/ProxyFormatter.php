<?php

declare(strict_types=1);

namespace Oxylabs\SharedDcApi;

class ProxyFormatter
{
    public function formatByUrl(array $proxyMap, string $url): array
    {
        $country = null;
        $parsedUrl = trim($url);

        $urlParts = $this->parseUrl($parsedUrl);
        if (count($urlParts) === 2) {
            $parsedUrl = trim($urlParts[0]);
            $country = trim($urlParts[1]);
        }

        $proxyAddress = $proxyMap[$country] ?? $proxyMap[DEFAULT_PROXY_INDEX_NAME];
        $template = 'http://customer-{USERNAME}:{PASSWORD}@{PROXY_ADDRESS}';

        return [$parsedUrl, strtr($template, [
            '{USERNAME}' => USERNAME,
            '{PASSWORD}' => PASSWORD,
            '{PROXY_ADDRESS}' => $proxyAddress,
        ])];
    }

    private function parseUrl(string $url): array
    {
        return explode(';', $url);
    }
}
