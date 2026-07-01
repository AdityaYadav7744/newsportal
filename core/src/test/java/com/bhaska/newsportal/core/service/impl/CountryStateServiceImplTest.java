package com.bhaska.newsportal.core.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CountryStateServiceImplTest {

    private CountryStateServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CountryStateServiceImpl();
    }

    // ================= GET STATES PATHS =================

    @Test
    void testGetStates_ReturnsEmptyList() {
        // The standard logic builds the string and skips straight to returning an empty list
        List<String> states = service.getStates("IN");

        assertNotNull(states);
        assertTrue(states.isEmpty());
    }

    @Test
    void testGetStates_ExceptionTriggersFallback() {
        // Passing null forces an implicit NullPointerException during string concatenation,
        // safely driving execution straight into the catch block to call getFallback(null).
        List<String> states = service.getStates(null);

        assertNotNull(states);
        assertTrue(states.isEmpty());
    }

    // ================= FALLBACK METHOD PATHS =================

    @Test
    void testGetFallback_India() {
        List<String> states = service.getFallback("IN");

        assertNotNull(states);
        assertEquals(3, states.size());
        assertTrue(states.contains("Maharashtra"));
        assertTrue(states.contains("Karnataka"));
        assertTrue(states.contains("Delhi"));
    }

    @Test
    void testGetFallback_UnitedStates() {
        List<String> states = service.getFallback("US");

        assertNotNull(states);
        assertEquals(2, states.size());
        assertTrue(states.contains("California"));
        assertTrue(states.contains("Texas"));
    }

    @Test
    void testGetFallback_UnknownCountry() {
        // Tests the 'getOrDefault' fallback logic when a country is missing from your map
        List<String> states = service.getFallback("FR");

        assertNotNull(states);
        assertTrue(states.isEmpty());
    }
}