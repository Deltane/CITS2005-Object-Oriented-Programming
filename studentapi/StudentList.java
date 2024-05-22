package studentapi;

/**
 * An interface describing methods for accessing the list of all student records through the API.
 *
 * <p>Student IDs are assigned in strictly increasing order, and the list is sorted in ascending
 * order by student ID such that older student records appear at the start of the list and newer
 * student records appear at the end.
 *
 * <p>The API is "paginated", meaning each query ({@link #getPage}) returns not a single student
 * record, but an array of student records called a "page". Pages are numbered starting from 0. The
 * concatenation of all pages would give a list of all student records.
 */
public interface StudentList {
    /**
     * Returns the number of student records in the list.
     *
     * @return The number of student records.
     */
    int getNumStudents();

    /**
     * Returns the number of pages comprising the whole list.
     *
     * @return The number of pages.
     */
    int getNumPages();

    /**
     * Returns the number of student records per page.
     *
     * <p>Note that the final page may have fewer elements than the page size.
     *
     * @return The page size.
     */
    int getPageSize();

    /**
     * Queries the API and returns the requested page.
     *
     * <p>Pages are numbered starting from 0.
     *
     * <p>The final page may be smaller than the page size given by {@link #getPageSize}.
     *
     * @param pageNum The page number to retrieve.
     * @return An array ("page") of student records.
     * @throws QueryTimedOutException If the query timed out without successfully completing.
     */
    Student[] getPage(int pageNum) throws QueryTimedOutException;
}
