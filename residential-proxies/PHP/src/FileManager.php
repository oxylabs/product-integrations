<?php

declare(strict_types=1);

namespace Oxylabs\ResidentialApi;

class FileManager
{
    public function __construct(
        private string $inputDirectory,
        private string $outputDirectory,
        private ConsoleWriter $consoleWriter
    ) {
    }

    public function readUrlList(): array
    {
        $this->consoleWriter->writeln('Reading from the list...');

        $urlList = @file(sprintf('%s/%s', $this->inputDirectory, URL_LIST_NAME));
        if (!$urlList) {
            $this->consoleWriter->writelnError('Failed to read input file');
            exit(1);
        }

        return array_filter($urlList);
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
