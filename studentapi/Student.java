package studentapi;

/** An interface describing methods for accessing the properties of a student record in the API. */
public interface Student {
    /**
     * Returns the unique student ID for this student.
     *
     * @return The student ID.
     */
    String getId();

    /**
     * Returns the mark this student got in a given unit.
     *
     * <p>Returns `null` if the student has not completed the unit.
     *
     * @param unit The unit code to get the mark for.
     * @return The mark this student got in the given unit, or `null` if the student has not
     *     completed the unit.
     */
    Integer getMark(String unit);
}
