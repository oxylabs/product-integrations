<?php

declare(strict_types=1);

namespace Oxylabs\DatacenterApi;

use ArrayIterator;
use InfiniteIterator;

class RoundRobinArrayWrapper
{
    private InfiniteIterator $iterator;

    public function __construct(
        array $source
    ) {
        $this->iterator = new InfiniteIterator(
            new ArrayIterator(
                $source
            )
        );

        $this->iterator->rewind();

    }

    public function fetchNext(): string
    {
        $nextItem = $this->iterator->current();
        $this->iterator->next();

        return $nextItem;
    }
}
