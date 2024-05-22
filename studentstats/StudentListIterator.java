package studentstats;

import itertools.DoubleEndedIterator;

import studentapi.*;

/**
 * A (double ended) iterator over student records pulled from the student API.
 *
 * <p>This does not load the whole student list immediately, but rather queries the API ({@link
 * StudentList#getPage}) only as needed.
 */
public class StudentListIterator implements DoubleEndedIterator<Student> {
    // TASK(8): Implement StudentListIterator: Add any fields you require

    /**
     * Construct an iterator over the given {@link StudentList} with the specified retry quota.
     *
     * @param list The API interface.
     * @param retries The number of times to retry a query after getting {@link
     *     QueryTimedOutException} before declaring the API unreachable and throwing an {@link
     *     ApiUnreachableException}.
     */
    public StudentListIterator(StudentList list, int retries) {
        // TASK(8): Implement StudentListIterator
    }

    /**
     * Construct an iterator over the given {@link StudentList} with a default retry quota of 3.
     *
     * @param list The API interface.
     */
    public StudentListIterator(StudentList list) {
        // TASK(8): Implement StudentListIterator
    }

    @Override
    public boolean hasNext() {
        // TASK(8): Implement StudentListIterator
        return false;
    }

    @Override
    public Student next() {
        // TASK(8): Implement StudentListIterator
        return null;
    }

    @Override
    public Student reverseNext() {
        // TASK(8): Implement StudentListIterator
        return null;
    }
}
