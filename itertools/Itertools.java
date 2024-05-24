package itertools;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

//progress updated on git repo clone via git
//https://github.com/Deltane/CITS2005.git

/**
 * A collection of useful methods for working with iterators.
 *
 * @see Iterator
 * @see DoubleEndedIterator
 */
public class Itertools {
    /**
     * Given an iterator and a number of elements, returns an iterator over that number of elements
     * taken from the iterator (or as many as it contains, if less than that number).
     *
     * <p>Elements are consumed from the given iterator only as needed.
     *
     * @param <T> The type of elements in the iterator.
     * @param it The iterator from which to take elements.
     * @param count The maximum number of elements to take.
     * @return An iterator over the first `count` elements of `it`.
     */
    // TASK(1): Implement the take method done 18/5
    public static <T> Iterator<T> take(Iterator<T> it, int count) {
        // create a new iterator that only gives back 'count' elements
        return new Iterator<T>() {
            private int remaining = count;  // track how many we still need to give

            @Override
            public boolean hasNext() {
                // there's a next item if we haven't reached the count and the original iterator has a next item
                return remaining > 0 && it.hasNext();
            }

            @Override
            public T next() {
                // if we've already given 'count' items, we shouldn't give anymore
                if (remaining <= 0) {
                    throw new NoSuchElementException();
                }
                remaining--;  // we're giving one item, so reduce the remaining count
                return it.next();  // get the next item from the original iterator
            }
        };
    }

    /**
     * Returns a (double ended) iterator in the reverse order of the one given.
     *
     * <p>Elements are consumed from the given iterator only as needed.
     *
     * @param <T> The type of elements in the iterator.
     * @param it The (double ended) iterator to reverse.
     * @return The reverse of the given iterator.
     */
    // TASK(2): Implement the reversed method done 18/5
    public static <T> Iterator<T> reversed(DoubleEndedIterator<T> it) {
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return it.hasNext(); // Checks if there are more elements to iterate over in reverse
            }

            @Override
            public T next() {
                return it.reverseNext(); // Fetches the next element from the back
            }
        };
    }


    /**
     * Returns an iterator over the elements of a given iterator with those elements that do not
     * satisfy a given {@link Predicate} dropped.
     *
     * <p>Elements are consumed from the given iterator only as needed (though it may be necessary
     * to consume elements to determine whether there is a next element that satisfies the
     * predicate).
     *
     * <p>Java's {@link Predicate} interface can be used by calling `pred.test(x)`, and will return
     * `true` if and only if `x` satisfies the predicate.
     *
     * @param <T> The type of elements in the iterator.
     * @param it The iterator to filter.
     * @param pred The predicate to use to determine whether to keep or drop an element.
     * @return An iterator over the elements of `it` with elements not satisfying `pred` removed.
     */
    // TASK(3): Implement the filter method done 18/5
    public static <T> Iterator<T> filter(Iterator<T> it, Predicate<T> pred) {
        // create a new iterator that filters elements based on a predicate
        return new Iterator<T>() {
            private T nextElement; // stores the next element if it passes the predicate
            private boolean nextElementReady = false; // flag to check if next element is ready

            @Override
            public boolean hasNext() {
                // if we already found a next element, just return true
                if (nextElementReady) {
                    return true;
                }
                // keep pulling from the original iterator until we find an element that matches the predicate
                while (it.hasNext()) {
                    T elem = it.next();
                    if (pred.test(elem)) {
                        nextElement = elem; // save the next element
                        nextElementReady = true; // set flag true since we have the next element ready
                        return true;
                    }
                }
                // if we get here, no more elements match
                return false;
            }

            @Override
            public T next() {
                // if there's no next element ready and hasNext returns false, we're out of elements
                if (!nextElementReady && !hasNext()) {
                    throw new NoSuchElementException();
                }
                nextElementReady = false; // reset the flag
                return nextElement; // return the saved next element
            }
        };
    }


    /**
     * Returns an iterator over the elements of a given iterator with a given function applied to
     * each element.
     *
     * <p>That is, given a function `f` and an iterator over the elements `a, b, c, ...`, returns an
     * iterator over `f(a), f(b), f(c), ...`.
     *
     * <p>Elements are consumed from the given iterator only as needed.
     *
     * <p>Java's {@link Function} interface can be used by calling `f.apply(x)` and will return
     * `f(x)`.
     *
     * @param <T> The type of elements in the input iterator.
     * @param <R> The type of elements in the result iterator.
     * @param it The iterator to map over.
     * @param f The function to apply to each element.
     * @return An iterator over the results of applying `f` to each element in `it`.
     */
    // TASK(4): Implement the map method done 18/5
    public static <T, R> Iterator<R> map(Iterator<T> it, Function<T, R> f) {
        // create a new iterator that transforms elements from T to R using function f
        return new Iterator<R>() {
            @Override
            public boolean hasNext() {
                // just checks if there's anything left in the original iterator
                return it.hasNext();
            }

            @Override
            public R next() {
                // grab the next item from the original iterator
                T item = it.next();
                // apply the function f to item and return the result
                // f could be anything like getting a specific property or converting the type
                return f.apply(item);
            }
        };
    }

    /**
     * A double-ended overload of {@link #map}.
     *
     * @param <T> The type of elements in the input iterator.
     * @param <R> The type of elements in the result iterator.
     * @param it The iterator to map over.
     * @param f The function to apply to each element.
     * @return An iterator over the results of applying `f` to each element in `it`.
     */

    // TASK(5): Implement the map method done 18/5
    public static <T, R> DoubleEndedIterator<R> map(DoubleEndedIterator<T> it, Function<T, R> f) {
        // creating a new double-ended iterator to apply function f to each item
        return new DoubleEndedIterator<R>() {
            @Override
            public boolean hasNext() {
                // check if we can move forward in the iterator
                return it.hasNext();  // checks for more elements to access from the front
            }

            @Override
            public R next() {
                // throw an error if we're out of elements
                if (!hasNext()) {
                    throw new NoSuchElementException("No more elements at the front.");
                }
                // get the next item from the front
                T item = it.next();
                // apply the function to the item and return the transformed element
                return f.apply(item);
            }

            @Override
            public R reverseNext() throws NoSuchElementException {
                // handle fetching from the back, can throw if out of elements
                T item = it.reverseNext();  // grabs the next element from the back
                // just like next(), but for the reverse direction
                return f.apply(item);  // transforms it using the function f
            }
        };
    }


    /**
     * Returns an iterator over the results of combining each pair of elements from a pair of given
     * iterators using a given function.
     *
     * <p>That is, given a function `f` and iterators over the elements `a, b, c, ...` and `x, y, z,
     * ...` returns an iterator over `f(a, x), f(b, y), f(c, z), ...`.
     *
     * <p>The iterator ends when either input iterator ends.
     *
     * <p>Elements are consumed from the given iterators only as needed.
     *
     * <p>Java's {@link BiFunction} interface can be used by calling `f.apply(x, y)` and will return
     * `f(x, y)`.
     *
     * @param <T> The type of elements in the "left-hand" iterator.
     * @param <U> The type of elements in the "right-hand" iterator.
     * @param <R> The type of elements in the result iterator.
     * @param lit The "left-hand" iterator.
     * @param rit The "right-hand" iterator.
     * @param f A function to use to combine elements from `lit` and `rit`.
     * @return An iterator over the result of combining elements from `lit` and `rit` using `f`.
     */
    // TASK(6): Implement the zip method done 18/5
    public static <T, U, R> Iterator<R> zip(Iterator<T> lit, Iterator<U> rit, BiFunction<T, U, R> f) {
        // creating a new iterator that combines elements from two iterators
        return new Iterator<R>() {
            @Override
            public boolean hasNext() {
                // check if both left and right iterators have more elements
                return lit.hasNext() && rit.hasNext();  // ensures both iterators can provide an element
            }

            @Override
            public R next() {
                // if either iterator is exhausted, we cannot proceed
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                // get one element from each iterator
                T left = lit.next();  // get the next element from the left-hand iterator
                U right = rit.next();  // get the next element from the right-hand iterator
                // apply the bi-function to these elements and return the result
                return f.apply(left, right);  // combines elements from both iterators
            }
        };
    }

    /**
     * Returns the result of combining all the elements from the given iterator using the given
     * function.
     *
     * <p>Each element is combined with the current value using the given function.
     *
     * <p>For example, given a function `f`, an initial value `x`, and an iterator over the elements
     * `a, b, c`, returns `f(f(f(x, a), b), c)`.
     *
     * <p>Java's {@link BiFunction} interface can be used by calling `f.apply(x, y)` and will return
     * `f(x, y)`.
     *
     * @param <T> The type of elements in the "left-hand" iterator.
     * @param <R> The type of the result.
     * @param it The iterator to reduce.
     * @param init The initial value.
     * @param f The function to use to combine each element into the reduction value.
     * @return The value after all elements have been combined.
     */
    // TASK(7): Implement the reduce method done 18/5
    public static <T, R> R reduce(Iterator<T> it, R init, BiFunction<R, T, R> f) {
        // starting off with the initial value for the reduction
        R result = init; // set the initial result to the provided initial value

        // going through each element in the iterator
        while (it.hasNext()) { // keep looping while there are more elements
            T element = it.next(); // grab the next element from the iterator
            // apply the function to the current result and the element, and update the result
            result = f.apply(result, element); // combine the current result with the current element
        }
        // after processing all elements, return the final result of the reduction
        return result; // give back the reduced result after going through all elements
    }
    //tests passed 7/7 done 18/5 do task 8/9
}
