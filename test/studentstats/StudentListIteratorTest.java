package test.studentstats;

import itertools.DoubleEndedIterator;

import studentapi.*;

import studentstats.ApiUnreachableException;
import studentstats.StudentListIterator;

import test.Stats;

import java.util.Arrays;
import java.util.Iterator;

public class StudentListIteratorTest {
    public static Stats run(String prefix) {
        String indent = prefix + "  ";
        System.out.println(prefix + "StudentListIteratorTest");
        Stats stats = new Stats(prefix, "StudentListIteratorTest");

        stats.add(testIteration(indent));
        stats.add(testRetries(indent));

        System.out.println(prefix + stats);
        return stats;
    }

    public static Stats testIteration(String prefix) {
        Stats stats = new Stats(prefix, "testIteration");

        try {
            String[] ids =
                    new String[] {
                        "20948678",
                        "20948680",
                        "20948681",
                        "20948682",
                        "20948683",
                        "20948686",
                        "20948689",
                        "20948692",
                        "20948694",
                        "20948695"
                    };
            Iterator<String> expected = Arrays.asList(ids).iterator();

            MockStudentList list = new MockStudentList(10);
            Iterator<Student> received = new StudentListIterator((StudentList) list);

            while (expected.hasNext()) {
                stats.expect(true, received.hasNext(), "iterator ends unexpectedly, hasNext");
                stats.expect(expected.next(), received.next().getId());
            }
            stats.expect(false, received.hasNext(), "iterator should be finished, hasNext");
            // Can be done in 3 calls, but allow some leeway
            stats.assertLessThanEqual(list.getCountGetPage(), 5, "too many calls to getPage()");
        } catch (Exception e) {
            stats.exception(e);
        }

        try {
            // Test accessing both ends on a single page list
            MockStudentList list = new MockStudentList(4);
            DoubleEndedIterator<Student> received = new StudentListIterator((StudentList) list);

            stats.expect("20948682", received.reverseNext().getId());
            stats.expect("20948678", received.next().getId());
            stats.expect("20948681", received.reverseNext().getId());
            stats.expect("20948680", received.next().getId());
            stats.expect(false, received.hasNext());
            // Can be done in 1 call, but allow some leeway
            stats.assertLessThanEqual(list.getCountGetPage(), 2, "too many calls to getPage()");
        } catch (Exception e) {
            stats.exception(e);
        }

        stats.resolveTest();
        stats.awardMarks(1);
        return stats;
    }

    public static Stats testRetries(String prefix) {
        Stats stats = new Stats(prefix, "testRetries");

        MockStudentList list = new MockStudentList(300);
        list.setFail(3, 5); // Fail 3 in a row out of every 5

        try {
            // 4 retry attempts should work
            Iterator<Student> it = new StudentListIterator((StudentList) list, 4);
            while (it.hasNext()) it.next();
        } catch (Exception e) {
            stats.exception(e);
        }

        boolean unreachable = false;
        try {
            // Default 3 retry attempts should fail
            Iterator<Student> it = new StudentListIterator((StudentList) list);
            while (it.hasNext()) it.next();
        } catch (ApiUnreachableException e) {
            unreachable = true;
        } catch (Exception e) {
            stats.exception(e);
        }

        if (!unreachable) {
            stats.fail("did not receive expected ApiUnreachableException");
        }

        stats.resolveTest();
        stats.awardMarks(1);
        return stats;
    }
}
