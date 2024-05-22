package test;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Stats {
    public int numPass = 0;
    public int numTest = 0;
    public int numMarks = 0;
    public int maxMarks = 0;
    public boolean pass = true;
    public String prefix = "";
    public String name = null;
    public String message = null;

    public Stats(String prefix, String name) {
        this.prefix = prefix;
        this.name = name;
    }

    public void resolveTest() {
        if (message == null) {
            message = "";
        } else {
            message = ": " + message;
        }
        ++numTest;
        if (pass) {
            ++numPass;
            message = "OK" + message;
        } else {
            message = "ERROR" + message;
        }
        System.out.println(prefix + name + ": " + message);
        pass = true;
        message = null;
    }

    public void fail(String message) {
        if (this.message == null) this.message = message;
        pass = false;
    }

    public void fail() {
        fail(null);
    }

    public void exception(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        fail("unpexpected " + sw);
    }

    public void expect(Object expected, Object received, String message) {
        if (!expected.equals(received)) {
            if (message == null) {
                message = "";
            } else {
                message += ": ";
            }
            message += "expected " + expected + ", received " + received;
            fail(message);
        }
    }

    public void expect(Object expected, Object received) {
        expect(expected, received, null);
    }

    public void assertLessThanEqual(int lhs, int rhs, String message) {
        if (lhs <= rhs) return;
        if (message == null) {
            message = "";
        } else {
            message += ": ";
        }
        message += "assertion " + lhs + " <= " + rhs + " failed";
        fail(message);
    }

    public void awardMarks(int marks) {
        if (numPass == numTest) {
            numMarks += marks;
        }
        maxMarks += marks;
    }

    public void add(Stats other) {
        numPass += other.numPass;
        numTest += other.numTest;
        numMarks += other.numMarks;
        maxMarks += other.maxMarks;
    }

    public String toString() {
        return "Passed "
                + numPass
                + "/"
                + numTest
                + " tests, estimated "
                + numMarks
                + "/"
                + maxMarks
                + " marks";
    }
}
