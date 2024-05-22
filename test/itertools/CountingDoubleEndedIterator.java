package test.itertools;

import itertools.DoubleEndedIterator;

public class CountingDoubleEndedIterator<T> implements DoubleEndedIterator<T> {
    private DoubleEndedIterator<T> it;
    private int nextCount = 0;
    private int reverseNextCount = 0;

    public CountingDoubleEndedIterator(DoubleEndedIterator<T> it) {
        this.it = it;
    }

    @Override
    public boolean hasNext() {
        return it.hasNext();
    }

    @Override
    public T next() {
        nextCount++;
        return it.next();
    }

    @Override
    public T reverseNext() {
        reverseNextCount++;
        return it.reverseNext();
    }

    public int getNextCount() {
        return nextCount;
    }

    public int getReverseNextCount() {
        return reverseNextCount;
    }

    public int getCount() {
        return nextCount + reverseNextCount;
    }
}
