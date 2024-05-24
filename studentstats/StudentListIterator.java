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

    private final StudentList list;

    // current element index within a certain page
    private int currentIndex;
    private int currentPageIndex;
    private List<Student> currentPage;
    private final int retries;
    // keep track of the elements that are already visited
    private final Set<Integer> visitedElementIndices;
    // keep track of the pages that are already visited
    private final Set<Integer> visitedPageIndices;
    //made retries final
    //made visitedElementIndices final
    //made visitedPageIndices final and works as expected

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
        this.visitedElementIndices = new HashSet<>();
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
                visitedElementIndices.clear();  // Clear visited indices for new page
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
        return visitedElementIndices.size() < currentPage.size() || visitedPageIndices.size() < list.getNumPages();
    }

    @Override
    public Student next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        // move to the next unvisited page if the current one is exhausted
        while (visitedPageIndices.contains(currentPageIndex)) {
            currentPageIndex = (currentPageIndex + 1) % list.getNumPages();
            loadPage(currentPageIndex);
        }
        // skip already visited elements on the current page
        while (visitedElementIndices.contains(currentIndex)) {
            currentIndex = (currentIndex + 1) % currentPage.size();
        }
        // mark the current element as visited
        visitedElementIndices.add(currentIndex);
        if (visitedElementIndices.size() >= currentPage.size()) {
            visitedPageIndices.add(currentPageIndex);
        }
        // return the current student and advance to the next one
        Student student = currentPage.get(currentIndex);
        currentIndex = (currentIndex + 1) % currentPage.size();
        return student;
    }

    @Override
    public Student reverseNext() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        // navigate through pages if the current page is completely visited
        while (visitedPageIndices.contains(currentPageIndex)) {
            currentPageIndex = (currentPageIndex + 1) % list.getNumPages();
            loadPage(currentPageIndex);
        }
        // adjust index to check the previous element
        currentIndex = (currentIndex - 1 + currentPage.size()) % currentPage.size();

        // check previous elements in the circular manner
        while (visitedElementIndices.contains(currentIndex)) {
            currentIndex = (currentIndex - 1 + currentPage.size()) % currentPage.size();
        }

        // mark this element as visited once identified
        visitedElementIndices.add(currentIndex);

        // if all elements on this page are visited, mark the page
        if (visitedElementIndices.size() >= currentPage.size()) {
            visitedPageIndices.add(currentPageIndex);
        }
        // return the student at the current index
        return currentPage.get(currentIndex);
    }

}
