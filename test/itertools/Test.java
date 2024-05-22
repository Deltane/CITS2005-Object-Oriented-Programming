package test.itertools;

import test.Stats;

public class Test {
    public static Stats run(String prefix) {
        String indent = prefix + "  ";
        System.out.println(prefix + "itertools");
        Stats stats = new Stats(prefix, "itertools");

        stats.add(ItertoolsTest.run(indent));

        System.out.println(prefix + stats);
        return stats;
    }
}
