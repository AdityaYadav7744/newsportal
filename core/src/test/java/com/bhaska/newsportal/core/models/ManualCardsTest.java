package com.bhaska.newsportal.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ManualCardsTest {

    @Test
    void testConstructorAndGetters() {

        ManualCards card = new ManualCards(
                "Java Course",
                "/content/dam/java.png",
                "Short Description",
                "Full Description",
                "Read More",
                "/content/newsportal/java"
        );

        assertEquals("Java Course", card.getCardTitle());
        assertEquals("/content/dam/java.png", card.getCardImage());
        assertEquals("Short Description", card.getShortDescription());
        assertEquals("Full Description", card.getFullDescription());
        assertEquals("Read More", card.getCtaText());
        assertEquals("/content/newsportal/java", card.getCtaLink());
    }

    @Test
    void testToString() {

        ManualCards card = new ManualCards(
                "Java Course",
                "/content/dam/java.png",
                "Short Description",
                "Full Description",
                "Read More",
                "/content/newsportal/java"
        );

        String result = card.toString();

        assertTrue(result.contains("Java Course"));
        assertTrue(result.contains("/content/dam/java.png"));
        assertTrue(result.contains("Short Description"));
        assertTrue(result.contains("Full Description"));
        assertTrue(result.contains("Read More"));
        assertTrue(result.contains("/content/newsportal/java"));
    }
}