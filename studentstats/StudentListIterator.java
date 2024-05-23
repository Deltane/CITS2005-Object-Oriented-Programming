package studentstats;

import itertools.DoubleEndedIterator;
import studentapi.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;

public class StudentListIterator implements DoubleEndedIterator<Student> {
    private StudentList list;
    private int retries;
    private List<Student> currentPage;
    private int currentIndex;
    private int currentPageNumber;

    public StudentListIterator(StudentList list, int retries) {
        this.list = list;
        this.retries = retries;
        this.currentPage = new ArrayList<>();
        this.currentIndex = 0;
        this.currentPageNumber = 0;
    }

    public StudentListIterator(StudentList list) {
        this(list, 3);
    }

    private void loadPage(int pageNumber, boolean reverse) {
        int attempts = 0;
        while (attempts < retries) {
            try {
                currentPage = new ArrayList<>(List.of(list.getPage(pageNumber)));
                if (reverse) {
                    Collections.reverse(currentPage);
                }
                currentIndex = reverse ? currentPage.size() - 1 : 0;
                currentPageNumber = pageNumber;
                return;
            } catch (QueryTimedOutException e) {
                attempts++;
            }
        }
        throw new ApiUnreachableException();
    }

    @Override
    public boolean hasNext() {
        if (currentIndex < currentPage.size()) {
            return true;
        }
        if (currentPageNumber + 1 < list.getNumPages()) {
            loadPage(currentPageNumber + 1, false);
            return true;
        }
        return false;
    }

    @Override
    public Student next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return currentPage.get(currentIndex++);
    }

    @Override
    public Student reverseNext() {
        if (currentIndex > 0) {
            return currentPage.get(--currentIndex);
        }
        if (currentPageNumber > 0) {
            loadPage(currentPageNumber - 1, true);
            return currentPage.get(--currentIndex);
        }
        throw new NoSuchElementException();
    }
}