<?php

declare(strict_types=1);

namespace Oxylabs\NextGenApi;

class ProxyFormatter
{
    private const TEMPLATE = 'http://{USERNAME}:{PASSWORD}@{PROXY_ADDRESS}';

    public function formatByUrl(string $url): array
    {
        $country = null;
        $parsedUrl = trim($url);

        $urlParts = $this->parseUrl($url);
        if (count($urlParts) == 2) {
            $parsedUrl = trim($urlParts[0]);
            $country = trim($urlParts[1]);
        }

        return [$parsedUrl, $country, strtr(self::TEMPLATE, [
            '{USERNAME}' => USERNAME,
            '{PASSWORD}' => PASSWORD,
            '{PROXY_ADDRESS}' => PROXY_ADDRESS,
        ])];
    }

    private function parseUrl(string $url): array
    {
        return explode(';', $url);
    }
}
