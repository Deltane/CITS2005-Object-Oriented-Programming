package test.studentstats;

import studentapi.*;

import java.util.Random;

public class MockStudentList implements StudentList {
    private String[] ids;
    private int pageSize;
    private int failLength;
    private int failPeriod;
    private int failCounter;
    private int countGetPage;

    public MockStudentList(int numStudents, long seed) {
        this.pageSize = 4;
        this.failLength = 1;
        this.failPeriod = 4;
        this.failCounter = 1;
        this.countGetPage = 0;

        Random random = new Random(seed);
        ids = new String[numStudents];
        int i = 0;
        long id = 20948678;
        while (i < ids.length) {
            if (random.nextBoolean()) {
                ids[i++] = Long.toString(id);
            }
            id++;
        }
    }

    public MockStudentList(int numStudents) {
        this(numStudents, 2005);
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setFail(int failLength, int failPeriod, int failCounter) {
        this.failLength = failLength;
        this.failPeriod = failPeriod;
        this.failCounter = failCounter;
    }

    public void setFail(int failLength, int failPeriod) {
        setFail(failLength, failPeriod, failLength);
    }

    @Override
    public int getNumStudents() {
        return ids.length;
    }

    @Override
    public int getNumPages() {
        return (ids.length + pageSize - 1) / pageSize;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public Student[] getPage(int pageNum) throws QueryTimedOutException {
        boolean fail = failCounter < failLength;
        failCounter = (failCounter + 1) % failPeriod;
        if (fail) throw new QueryTimedOutException();

        countGetPage++;

        int lower = pageNum * pageSize;
        int upper = Math.min(ids.length, lower + pageSize);
        Student[] page = new Student[upper - lower];
        for (int i = lower; i < upper; i++) {
            page[i - lower] = new MockStudent(ids[i]);
        }
        return page;
    }

    public int getCountGetPage() {
        return countGetPage;
    }
}
