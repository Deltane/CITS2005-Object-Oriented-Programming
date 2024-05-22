package test.itertools;

import java.util.Iterator;

public class CountingIterator<T> implements Iterator<T> {
    private Iterator<T> it;
    private int nextCount = 0;

    public CountingIterator(Iterator<T> it) {
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

    public int getNextCount() {
        return nextCount;
    }

    public int getCount() {
        return nextCount;
    }
}
