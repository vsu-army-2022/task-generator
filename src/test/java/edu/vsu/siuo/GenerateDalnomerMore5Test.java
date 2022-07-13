package edu.vsu.siuo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;

class GenerateDalnomerMore5Test {

    @RepeatedTest(10000)
    @Disabled
    void repeatedTest() {
        GenerateDalnomerMore5.generateTask();
    }
}