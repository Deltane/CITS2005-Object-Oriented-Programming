package test.studentstats;

import studentapi.*;

import studentstats.StudentStats;

import test.Stats;

import java.util.Arrays;
import java.util.Iterator;

public class StudentStatsTest {
    public static Stats run(String prefix) {
        String indent = prefix + "  ";
        System.out.println(prefix + "StudentStatsTest");
        Stats stats = new Stats(prefix, "StudentStatsTest");

        stats.add(testUnitAverage(indent));
        stats.add(testUnitNewestStudents(indent));

        System.out.println(prefix + stats);
        return stats;
    }

    public static Stats testUnitAverage(String prefix) {
        Stats stats = new Stats(prefix, "testUnitAverage");

        try {
            int expected = 55;

            MockStudentList list = new MockStudentList(30);
            int received = StudentStats.unitAverage((StudentList) list, "CITS2005");

            stats.expect(expected, received);
            // Can be done in 8 calls, but allow some leeway
            stats.assertLessThanEqual(list.getCountGetPage(), 10, "too many calls to getPage()");
        } catch (Exception e) {
            stats.exception(e);
        }

        stats.resolveTest();
        stats.awardMarks(1);
        return stats;
    }

    public static Stats testUnitNewestStudents(String prefix) {
        Stats stats = new Stats(prefix, "testUnitNewestStudents");

        try {
            String[] ids =
                    new String[] {
                        "20948760", "20948758", "20948756", "20948755", "20948754", "20948751"
                    };
            Iterator<String> expected = Arrays.asList(ids).iterator();

            MockStudentList list = new MockStudentList(45);
            Iterator<Student> received =
                    StudentStats.unitNewestStudents((StudentList) list, "CITS2005");

            while (expected.hasNext()) {
                stats.expect(true, received.hasNext(), "iterator ends unexpectedly, hasNext");
                stats.expect(expected.next(), received.next().getId());
            }
            // Can be done in 3 calls, but allow some leeway
            stats.assertLessThanEqual(list.getCountGetPage(), 5, "too many calls to getPage()");
        } catch (Exception e) {
            stats.exception(e);
        }

        stats.resolveTest();
        stats.awardMarks(1);
        return stats;
    }
}
