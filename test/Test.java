package test;

public class Test {
    public static void main(String[] args) {
        run("");
    }

    public static Stats run(String prefix) {
        String indent = prefix + "  ";
        System.out.println(prefix + "Running tests");
        Stats stats = new Stats(prefix, "root");

        stats.add(test.itertools.Test.run(indent));
        stats.add(test.studentstats.Test.run(indent));

        System.out.println(prefix + stats);
        return stats;
    }
}
