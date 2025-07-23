package com.bezkoder.spring.restapi;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.fail;

public class FailingTest {

    @Test
    void thisTestFails() {
        fail("This test is meant to fail CI pipeline!");
    }
}
