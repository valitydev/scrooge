package dev.vality.scrooge.service.impl;

import dev.vality.scrooge.service.DurationInspector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RenewalDurationInspector.class})
@TestPropertySource(properties = {
        "service.renewal.first-threshold=60", "service.renewal.second-threshold=1440"})
class RenewalDurationInspectorTest {

    @Autowired
    private DurationInspector durationInspector;

    @Test
    void validTimeLessFirstThreshold() {
        assertTrue(durationInspector.isValid(30));
    }

    @Test
    void validTimeGreaterAndMultipleFirstThreshold() {
        assertTrue(durationInspector.isValid(120));
    }

    @Test
    void validTimeGreaterAndMultipleSecondThreshold() {
        assertTrue(durationInspector.isValid(2880));
    }

    @Test
    void notValidTimeGreaterAndNotMultipleFirstThreshold() {
        assertFalse(durationInspector.isValid(80));
    }

    @Test
    void notValidTimeGreaterAndNotMultipleSecondThreshold() {
        assertFalse(durationInspector.isValid(2800));
    }
}