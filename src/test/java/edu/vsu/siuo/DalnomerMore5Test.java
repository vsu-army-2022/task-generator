package edu.vsu.siuo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;

class DalnomerMore5Test {

    @RepeatedTest(10000)
    @Disabled
    void repeatedTest() {
        GenerateNZRMore5.generateTask();
    }
}