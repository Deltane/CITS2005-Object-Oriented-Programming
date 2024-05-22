package test.itertools;

import itertools.*;

import test.Stats;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

class IntegerIsEven implements Predicate<Integer> {
    public boolean test(Integer x) {
        return x % 2 == 0;
    }
}

class IntegerSquare implements UnaryOperator<Integer> {
    public Integer apply(Integer x) {
        return x * x;
    }
}

class IntegerSub implements BinaryOperator<Integer> {
    public Integer apply(Integer lhs, Integer rhs) {
        return lhs - rhs;
    }
}

class StringIntegerAppend implements BiFunction<String, Integer, String> {
    public String apply(String lhs, Integer rhs) {
        return lhs + rhs;
    }
}

public class ItertoolsTest {
    public static Stats run(String prefix) {
        String indent = prefix + "  ";
        System.out.println(prefix + "ItertoolsTest");
        Stats stats = new Stats(prefix, "ItertoolsTest");

        stats.add(testTake(indent));
        stats.add(testReversed(indent));
        stats.add(testFilter(indent));
        stats.add(testMap(indent));
        stats.add(testMapBackwards(indent));
        stats.add(testZip(indent));
        stats.add(testReduce(indent));

        System.out.println(prefix + stats);
        return stats;
    }

    public static Stats testTake(String prefix) {
        Stats stats = new Stats(prefix, "testTake");

        try {
            Iterator<Integer> expected = Arrays.asList(2, 5, 8).iterator();

            CountingIterator<Integer> countit = new CountingIterator<>(new RangeIterator(2, 20, 3));
            Iterator<Integer> received = Itertools.take((Iterator<Integer>) countit, 3);

            int count = 0;
            while (expected.hasNext() && received.hasNext()) {
                stats.expect(count, countit.getCount(), "called next too many times or too soon");
                stats.expect(expected.next(), received.next());
                count++;
            }
            stats.expect(
                    expected.hasNext(), received.hasNext(), "iterator does not end when expected");
        } catch (Exception e) {
            stats.exception(e);
        }

        stats.resolveTest();
        stats.awardMarks(1);
        return stats;
    }

    public static Stats testReversed(String prefix) {
        Stats stats = new Stats(prefix, "testReversed");

        try {
            Iterator<Integer> expected = Arrays.asList(17, 14, 11, 8, 5, 2).iterator();

            CountingDoubleEndedIterator<Integer> countit =
                    new CountingDoubleEndedIterator<>(new RangeIterator(2, 20, 3));
            Iterator<Integer> received = Itertools.reversed((DoubleEndedIterator<Integer>) countit);

            int count = 0;
            while (expected.hasNext() && received.hasNext()) {
                stats.expect(
                        count,
                        countit.getCount(),
                        "called next/reverseNext too many times or too soon");
                stats.expect(expected.next(), received.next());
                count++;
            }
            stats.expect(
                    expected.hasNext(), received.hasNext(), "iterator does not end when expected");
        } catch (Exception e) {
            stats.exception(e);
        }

        stats.resolveTest();
        stats.awardMarks(1);
        return stats;
    }

    public static Stats testFilter(String prefix) {
        Stats stats = new Stats(prefix, "testFilter");

        try {
            Iterator<Integer> expected = Arrays.asList(2, 8, 14).iterator();

            CountingIterator<Integer> countit = new CountingIterator<>(new RangeIterator(2, 20, 3));
            Iterator<Integer> received =
                    Itertools.filter((Iterator<Integer>) countit, new IntegerIsEven());

            int count = 1;
            while (expected.hasNext() && received.hasNext()) {
                stats.expect(
                        count,
                        countit.getCount(),
                        "called next/reverseNext too many times or too soon");
                // Extra calls to hasNext() should not affect anything.
                received.hasNext();
                received.hasNext();
                stats.expect(expected.next(), received.next());
                count += 2;
            }
            stats.expect(
                    expected.hasNext(), received.hasNext(), "iterator does not end when expected");
        } catch (Exception e) {
            stats.exception(e);
        }

        try {
            CountingIterator<Integer> countit =
                    new CountingIterator<>(new RangeIterator(40, 50, 2));
            Iterator<Integer> received =
                    Itertools.filter((Iterator<Integer>) countit, new IntegerIsEven().negate());

            stats.expect(false, received.hasNext(), "nothing should match predicate");
            stats.expect(5, countit.getCount(), "should consume entire iterator before ending");
        } catch (Exception e) {
            stats.exception(e);
        }

        stats.resolveTest();
        stats.awardMarks(1);
        return stats;
    }

    public static Stats testMap(String prefix) {
        Stats stats = new Stats(prefix, "testMap");

        try {
            Iterator<Integer> expected = Arrays.asList(4, 25, 64, 121, 196, 289).iterator();

            CountingIterator<Integer> countit = new CountingIterator<>(new RangeIterator(2, 20, 3));
            Iterator<Integer> received =
                    Itertools.map((Iterator<Integer>) countit, new IntegerSquare());

            int count = 0;
            while (expected.hasNext() && received.hasNext()) {
                stats.expect(count, countit.getCount(), "called next too many times or too soon");
                stats.expect(expected.next(), received.next());
                count++;
            }
            stats.expect(
                    expected.hasNext(), received.hasNext(), "iterator does not end when expected");
        } catch (Exception e) {
            stats.exception(e);
        }

        stats.resolveTest();
        stats.awardMarks(1);
        return stats;
    }

    public static Stats testMapBackwards(String prefix) {
        Stats stats = new Stats(prefix, "testMapBackwards");

        try {
            Iterator<Integer> expected = Arrays.asList(289, 196, 121, 64, 25, 4).iterator();

            CountingDoubleEndedIterator<Integer> countit =
                    new CountingDoubleEndedIterator<>(new RangeIterator(2, 20, 3));
            DoubleEndedIterator<Integer> received =
                    Itertools.map((DoubleEndedIterator<Integer>) countit, new IntegerSquare());

            int count = 0;
            while (expected.hasNext() && received.hasNext()) {
                stats.expect(
                        count,
                        countit.getCount(),
                        "called next/reverseNext too many times or too soon");
                stats.expect(expected.next(), received.reverseNext());
                count++;
            }
            stats.expect(
                    expected.hasNext(), received.hasNext(), "iterator does not end when expected");
        } catch (Exception e) {
            stats.exception(e);
        }

        stats.resolveTest();
        stats.awardMarks(1);
        return stats;
    }

    public static Stats testZip(String prefix) {
        Stats stats = new Stats(prefix, "testZip");

        try {
            CountingIterator<Integer> lhs =
                    new CountingIterator<>(Arrays.asList(49, 51, 67, 22, 93, 73, 84).iterator());
            CountingIterator<Integer> rhs =
                    new CountingIterator<>(Arrays.asList(25, 82, 63, 31, 61).iterator());

            Iterator<Integer> expected = Arrays.asList(24, -31, 4, -9, 32).iterator();

            Iterator<Integer> received =
                    Itertools.zip(
                            (Iterator<Integer>) lhs, (Iterator<Integer>) rhs, new IntegerSub());

            int count = 0;
            while (expected.hasNext() && received.hasNext()) {
                stats.expect(count, lhs.getCount(), "called next too many times or too soon");
                stats.expect(count, rhs.getCount(), "called next too many times or too soon");
                stats.expect(expected.next(), received.next());
                count++;
            }
            stats.expect(
                    expected.hasNext(), received.hasNext(), "iterator does not end when expected");
        } catch (Exception e) {
            stats.exception(e);
        }

        stats.resolveTest();
        stats.awardMarks(1);
        return stats;
    }

    public static Stats testReduce(String prefix) {
        Stats stats = new Stats(prefix, "testReduce");

        try {
            String expected = "init258111417";

            String received =
                    Itertools.reduce(
                            new RangeIterator(2, 20, 3), "init", new StringIntegerAppend());

            stats.expect(expected, received);
        } catch (Exception e) {
            stats.exception(e);
        }

        stats.resolveTest();
        stats.awardMarks(1);
        return stats;
    }
}
