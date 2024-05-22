package studentstats;

import itertools.Itertools;

import studentapi.*;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;

/** A class for computing the average of a number of integer samples. */
class IntegerAverage {
    private int total = 0;
    private int count = 0;

    public void addSample(int sample) {
        total += sample;
        count++;
    }

    public int getAverage() {
        return total / count;
    }
}

/** A {@link BiFunction} adding an integer sample to an {@link IntegerAverage}. */
class IntegerAverageReduction implements BiFunction<IntegerAverage, Integer, IntegerAverage> {
    public IntegerAverage apply(IntegerAverage lhs, Integer rhs) {
        if (rhs == null) return lhs;
        lhs.addSample(rhs);
        return lhs;
    }
}

/** A {@link Function} retrieving the mark for a particular unit from a {@link Student} record. */
class GetUnitMark implements Function<Student, Integer> {
    String unit;

    public GetUnitMark(String unit) {
        this.unit = unit;
    }

    public Integer apply(Student student) {
        return student.getMark(unit);
    }
}

// TASK(9): Implement unitNewestStudents: You may want to declare a class here.

/** A collection of statistical and analytical methods for working with the student API. */
public class StudentStats {
    /**
     * Returns the average mark (integer division) across all students who have completed a given
     * unit.
     *
     * @param list The student API interface.
     * @param unit The unit code.
     * @return The average mark for all students who have taken `unit`.
     */
    public static int unitAverage(StudentList list, String unit) {
        return Itertools.reduce(
                        Itertools.map(new StudentListIterator(list), new GetUnitMark(unit)),
                        new IntegerAverage(),
                        new IntegerAverageReduction())
                .getAverage();
    }

    /**
     * Returns an iterator over the students who have taken a given unit, from newest to oldest.
     *
     * @param list The student API interface.
     * @param unit The unit code.
     * @return An iterator over the students who have taken `unit`, from newest to oldest.
     */
    public static Iterator<Student> unitNewestStudents(StudentList list, String unit) {
        // TASK(9): Implement unitNewestStudents
        return null;
    }
}
