<?php

declare(strict_types=1);

namespace Oxylabs\ResidentialApi;

class ProxyFormatter
{
    public function formatByUrl(string $url): array
    {
        $country = null;
        $parsedUrl = trim($url);

        $urlParts = $this->parseUrl($parsedUrl);
        if (count($urlParts) === 2) {
            $parsedUrl = trim($urlParts[0]);
            $country = trim($urlParts[1]);
        }

        $trimmedCountry = $country ? trim($country) : null;
        $template = 'http://customer-{USERNAME}:{PASSWORD}@{PROXY_ADDRESS}';
        if ($trimmedCountry) {
            $template = 'http://customer-{USERNAME}-cc-{COUNTRY}:{PASSWORD}@{PROXY_ADDRESS}';
        }

        return [$parsedUrl, strtr($template, [
            '{USERNAME}' => USERNAME,
            '{PASSWORD}' => PASSWORD,
            '{PROXY_ADDRESS}' => PROXY_ADDRESS,
            '{COUNTRY}' => $trimmedCountry,
        ])];
    }

    private function parseUrl(string $url): array
    {
        return explode(';', $url);
    }
}
