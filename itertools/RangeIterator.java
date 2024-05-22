package itertools;

import java.util.NoSuchElementException;

/** An iterator over a sequence of evenly spaced integers in a particular range. */
public class RangeIterator implements DoubleEndedIterator<Integer> {
    /** The difference between each element in the sequence and the next. */
    private int step;

    /** The frontmost element in the range that has not yet been returned. */
    private int front;

    /** The backmost element in the range that has not yet been returned. */
    private int back;

    /**
     * Constructs an iterator over a range with the given spacing between elements.
     *
     * @param lower The lower bound of the range (inclusive), and the first value in the sequence.
     * @param upper The upper bound of the range (exclusive).
     * @param step The difference between each element in the sequence and the next.
     */
    public RangeIterator(int lower, int upper, int step) {
        this.step = step;
        front = lower;
        back = lower + step * ((upper - lower - 1) / step);
    }

    /**
     * Constructs an iterator over a contiguous range of integers between the given bounds.
     *
     * @param lower The lower bound of the range (inclusive), and the first value in the sequence.
     * @param upper The upper bound of the range (exclusive).
     */
    public RangeIterator(int lower, int upper) {
        this(lower, upper, 1);
    }

    /**
     * Constructs an iterator over a contiguous range of integers starting at 0 and less than the
     * given bound.
     *
     * @param upper The upper bound of the range (exclusive).
     */
    public RangeIterator(int upper) {
        this(0, upper);
    }

    @Override
    public boolean hasNext() {
        // There remain elements to return so long as the backmost is not before the frontmost.
        return front <= back;
    }

    @Override
    public Integer next() {
        if (!hasNext()) throw new NoSuchElementException();
        // Increment front and return the old value.
        int result = front;
        front += step;
        return result;
    }

    @Override
    public Integer reverseNext() {
        if (!hasNext()) throw new NoSuchElementException();
        // Decrement back and return the old value.
        int result = back;
        back -= step;
        return result;
    }
}
