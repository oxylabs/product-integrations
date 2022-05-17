<?php

declare(strict_types=1);

namespace Oxylabs\SharedDcApi;

class ProxyFormatter
{
    public function formatByUrl(string $url): array
    {
        $country = null;
        $parsedUrl = trim($url);

        $template = 'http://customer-{USERNAME}:{PASSWORD}@{PROXY_ADDRESS}';

        return [$parsedUrl, strtr($template, [
            '{USERNAME}' => USERNAME,
            '{PASSWORD}' => PASSWORD,
            '{PROXY_ADDRESS}' => PROXY_ADDRESS,
        ])];
    }
}
