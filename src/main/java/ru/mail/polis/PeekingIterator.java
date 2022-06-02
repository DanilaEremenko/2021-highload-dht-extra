package ru.mail.polis;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class PeekingIterator<R> implements Iterator<Record> {
    private final Iterator<Record> delegate;
    private Record lastPeek;

    PeekingIterator(Iterator<Record> delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean hasNext() {
        return delegate.hasNext() || this.lastPeek != null;
    }

    @Override
    public Record next() {
        if (!hasNext())
            throw new NoSuchElementException("No elements in the delegate iterator");

        Record currPeek = peek();
        lastPeek = null;
        return currPeek;
    }

    Record peek() {
        if (lastPeek != null)
            return lastPeek;

        if (!delegate.hasNext())
            return null;

        lastPeek = delegate.next();
        return lastPeek;
    }
}
