<?php

declare(strict_types=1);

namespace Oxylabs\ResidentialApi;

class ConsoleWriter
{
    public function writeln(string $output): void
    {
        print $output . PHP_EOL;
    }

    public function writelnError(string $output)
    {
        printf('ERROR: %s'. PHP_EOL, $output);
    }
}
