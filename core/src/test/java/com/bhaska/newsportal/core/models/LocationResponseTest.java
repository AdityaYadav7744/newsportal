package com.bhaska.newsportal.core.models;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class LocationResponseTest {

    @Test
    void testDefaultValues() {

        LocationResponse response = new LocationResponse();

        assertNull(response.getCity());
        assertNull(response.getRegion());
        assertNull(response.getCountry());
    }

    @Test
    void testSetAndGetCity() {

        LocationResponse response = new LocationResponse();
        response.setCity("Hyderabad");
        assertEquals("Hyderabad", response.getCity());
    }

    @Test
    void testSetAndGetRegion() {

        LocationResponse response = new LocationResponse();

        response.setRegion("Telangana");

        assertEquals("Telangana", response.getRegion());
    }

    @Test
    void testSetAndGetCountry() {

        LocationResponse response = new LocationResponse();

        response.setCountry("India");

        assertEquals("India", response.getCountry());
    }

    @Test
    void testAllProperties() {

        LocationResponse response = new LocationResponse();

        response.setCity("Hyderabad");
        response.setRegion("Telangana");
        response.setCountry("India");

        assertEquals("Hyderabad", response.getCity());
        assertEquals("Telangana", response.getRegion());
        assertEquals("India", response.getCountry());
    }
}