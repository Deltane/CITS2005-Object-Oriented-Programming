package test.studentstats;

import test.Stats;

public class Test {
    public static Stats run(String prefix) {
        String indent = prefix + "  ";
        System.out.println(prefix + "studentstats");
        Stats stats = new Stats(prefix, "studentstats");

        stats.add(StudentListIteratorTest.run(indent));
        stats.add(StudentStatsTest.run(indent));

        System.out.println(prefix + stats);
        return stats;
    }
}
