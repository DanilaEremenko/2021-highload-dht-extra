package ru.mail.polis;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class TombstoneFilterIterator<R> implements Iterator<Record> {

    private final MergeIterator mergeIterator;
    private Record lastFoundAlive;

    TombstoneFilterIterator(MergeIterator mergeIterator) {
        this.mergeIterator = mergeIterator;
        lastFoundAlive = peekNextAlive();
    }

    @Override
    public boolean hasNext() {
        return lastFoundAlive != null && !lastFoundAlive.isTombstone();
    }

    @Override
    public Record next() {
        if (!hasNext())
            throw new NoSuchElementException("No elements in the delegate iterator");

        Record lastFoundAliveSaved = lastFoundAlive;
        lastFoundAlive = peekNextAlive();
        return lastFoundAliveSaved;
    }

    Record peekNextAlive() {
        Record nextAlive = null;
        while (mergeIterator.hasNext()) {
            nextAlive = mergeIterator.next();
            if (!nextAlive.isTombstone())
                return nextAlive;
        }
        return nextAlive;
    }
}
