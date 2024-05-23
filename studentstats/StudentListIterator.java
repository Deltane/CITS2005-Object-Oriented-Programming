package studentstats;

import itertools.DoubleEndedIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import studentapi.*;

/**
 * A (double ended) iterator over student records pulled from the student API.
 *
 * <p>
 * This does not load the whole student list immediately, but rather queries the
 * API ({@link StudentList#getPage}) only as needed.
 */
public class StudentListIterator implements DoubleEndedIterator<Student> {

    private StudentList list;

    // current element index within a certain page
    private int currentIndex;
    private int currentPageIndex;
    private List<Student> currentPage;
    private int retries;

    // keep track of the elements that are already visited
    private Set<Integer> visitedEleIndices;
    // keep track of the pages that are already visited
    private Set<Integer> visitedPageIndices;

    /**
     * Construct an iterator over the given {@link StudentList} with the
     * specified retry quota.
     *
     * @param list The API interface.
     * @param retries The number of times to retry a query after getting
     * {@link QueryTimedOutException} before declaring the API unreachable and
     * throwing an {@link ApiUnreachableException}.
     */
    public StudentListIterator(StudentList list, int retries) {
        this.list = list;
        this.currentIndex = 0;
        this.currentPageIndex = 0;
        this.currentPage = new ArrayList<>();
        this.retries = retries;
        this.visitedEleIndices = new HashSet<>();
        this.visitedPageIndices = new HashSet<>();
        loadPage(currentPageIndex);
    }

    /**
     * Construct an iterator over the given {@link StudentList} with a default
     * retry quota of 3.
     *
     * @param list The API interface.
     */
    public StudentListIterator(StudentList list) {
        this(list, 3);
    }

    private void loadPage(int pageNum) {
        int attempts = 0;
        while (attempts < retries) {
            try {
                currentPage = Arrays.asList(list.getPage(pageNum));
                currentIndex = 0;
                visitedEleIndices.clear();  // Clear visited indices for new page
                return;
            } catch (QueryTimedOutException e) {
                attempts++;
            }
        }
        throw new ApiUnreachableException();
    }

    @Override
    public boolean hasNext() {
        /**
         * return true if one of the following is true: 1. current page is not
         * finished 2. there is another page to go
         */
        return visitedEleIndices.size() < currentPage.size() || visitedPageIndices.size() < list.getNumPages();
    }

    @Override
    public Student next() {
        /**
         * If visitedPageIndices.size() >= list.getNumPages(), throw
         * NoSuchElementException(), otherwise, continue if currentPageIndex is
         * in visitedPageIndices, move to next page. else, if currentIndex is in
         * visitedEleIndices, currentIndex move forward (in a circular manner)
         * if currentIndex is not in visitedEleIndices, return current element,
         * add currentIndex to visitedEleIndices, currentIndex move forward
         */
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        // while current page has been finished
        while (visitedPageIndices.contains(currentPageIndex)) {
            // move forward to the next page in a circular manner
            currentPageIndex = (currentPageIndex + 1) % list.getNumPages();
            loadPage(currentPageIndex);
        }
        // while current element has been finished
        while (visitedEleIndices.contains(currentIndex)) {
            // move forward to the next element in a circular manner
            currentIndex = (currentIndex + 1) % currentPage.size();
        }

        // once found an unvisited element, do the following:
        // mark current element as visited
        visitedEleIndices.add(currentIndex);

        // if all the elements are visited within current page
        if (visitedEleIndices.size() >= currentPage.size()) {
            // mark current page as visited
            visitedPageIndices.add(currentPageIndex);
        }

        // note the current element
        Student student = currentPage.get(currentIndex);
        // move forward within current page in a circular manner
        currentIndex = (currentIndex + 1) % currentPage.size();

        return student;
    }

    @Override
    public Student reverseNext() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        // while current page has been finished
        while (visitedPageIndices.contains(currentPageIndex)) {
            // move forward to the next page in a circular manner
            currentPageIndex = (currentPageIndex + 1) % list.getNumPages();
            loadPage(currentPageIndex);
        }
        // set the current index backwards by 1
        currentIndex = (currentIndex - 1 + currentPage.size()) % currentPage.size();

        // while current element has been finished
        while (visitedEleIndices.contains(currentIndex)) {
            // move backward to the next element in a circular manner
            currentIndex = (currentIndex - 1 + currentPage.size()) % currentPage.size();
        }

        // once found an unvisited element, do the following:
        // mark current element as visited
        visitedEleIndices.add(currentIndex);

        // if all the elements are visited within current page
        if (visitedEleIndices.size() >= currentPage.size()) {
            // mark current page as visited
            visitedPageIndices.add(currentPageIndex);
        }

        // note the current element
        Student student = currentPage.get(currentIndex);

        return student;
    }
}
