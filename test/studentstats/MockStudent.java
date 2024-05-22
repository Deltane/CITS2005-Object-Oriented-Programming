package test.studentstats;

import studentapi.*;

import java.util.Objects;
import java.util.Random;

public class MockStudent implements Student {
    private String id;

    public MockStudent(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Integer getMark(String unit) {
        // Pseudorandom result based on hash of id and unit code
        Random random = new Random(Objects.hash(id, unit));

        // 50/50 chance this student has done this unit
        if (random.nextBoolean()) return null;

        // Pseudorandom mark 0..100
        return random.nextInt(101);
    }
}
