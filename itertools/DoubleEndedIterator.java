package itertools;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A double ended iterator over a collection. Can be used to iterate over the elements of a
 * collection from either end. Each element is still returned at most once.
 *
 * @param <T> The type of elements returned by the double ended iterator.
 */
public interface DoubleEndedIterator<T> extends Iterator<T> {
    /**
     * Returns the next element from the back in the iteration. Will not return an element that has
     * already been returned by {@link #next()}.
     *
     * @return The next element from the back in the iteration.
     * @throws NoSuchElementException If the iteration has no more elements.
     */
    T reverseNext() throws NoSuchElementException;
}
