package at.swe1.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RunnerTest {
    @DisplayName("Test TestRunner.get()")
    @Test

    void testGet(){
        assertEquals("Hello JUnit 5", at.swe1.test.Runner.get());
    }
}