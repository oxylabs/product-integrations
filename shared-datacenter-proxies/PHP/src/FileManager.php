<?php

declare(strict_types=1);

namespace Oxylabs\SharedDcApi;

class FileManager
{
    public function __construct(
        private string $inputDirectory,
        private string $outputDirectory,
        private ConsoleWriter $consoleWriter
    ) {
    }

    public function readProxyMap(): array
    {
        $fileName = sprintf('%s/%s', $this->inputDirectory, PROXY_LIST_NAME);
        $proxyList = $this->readListFromFile($fileName);

        $proxyMap = [];
        foreach ($proxyList as $proxyUrl) {
            $trimmedProxyUrl = trim($proxyUrl);
            preg_match(PROXY_REGEX, $trimmedProxyUrl, $matches);
            $country = $matches['country'] ?? DEFAULT_PROXY_INDEX_NAME;
            $proxyMap[strtoupper($country)] = $trimmedProxyUrl;
        }

        return $proxyMap;
    }

    public function readUrlList(): array
    {
        $fileName = sprintf('%s/%s', $this->inputDirectory, URL_LIST_NAME);

        return $this->readListFromFile($fileName);
    }

    public function readListFromFile(string $path): array
    {
        $this->consoleWriter->writeln('Reading from the list...');

        $list = @file($path);
        if (!$list) {
            $this->consoleWriter->writelnError('Failed to read input file');
            exit(1);
        }

        $trimmedList = array_map('trim', $list);

        return array_filter($trimmedList);
    }

    public function writeError(string $contents): void
    {
        $this->consoleWriter->writeln('ERROR: ' . $contents);

        $fileName = sprintf('%s/failed_requests.txt', $this->outputDirectory);
        file_put_contents($fileName, $contents . PHP_EOL, FILE_APPEND);
    }

    public function writeSuccess($position, string $contents): void
    {
        $fileName = sprintf( '%s/result_%d.html', $this->outputDirectory, $position);
        file_put_contents($fileName, $contents);
    }
}
