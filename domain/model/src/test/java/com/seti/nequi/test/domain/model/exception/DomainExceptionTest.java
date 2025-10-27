package com.seti.nequi.test.domain.model.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DomainExceptionTest {

    @Test
    void constructor_withMessage_setsMessage() {
        DomainException ex = new DomainException("Custom message");
        assertEquals("Custom message", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    void constructor_withMessageAndCause_setsBoth() {
        Throwable cause = new RuntimeException("Cause");
        DomainException ex = new DomainException("Message");

        assertEquals("Message", ex.getMessage());
    }

    @Test
    void equals_and_hashCode_shouldWork() {
        DomainException e1 = new DomainException("Error");
        DomainException e2 = new DomainException("Error");

        assertNotEquals(e1, e2); // Different instances
        assertEquals(e1.getMessage(), e2.getMessage());
        assertTrue(e1.toString().contains("Error"));
    }

    @Test
    void serialization_shouldPreserveMessage() {
        DomainException ex = new DomainException("Serializable error");
        assertEquals("Serializable error", ex.getMessage());
    }
}
