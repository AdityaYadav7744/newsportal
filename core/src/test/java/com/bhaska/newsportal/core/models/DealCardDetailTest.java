package com.bhaska.newsportal.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DealCardDetailTest {

    @Test
    void testSettersAndGetters() {

        DealCardDetail dealCard = new DealCardDetail();

        dealCard.setCardTitle("Summer Deal");
        dealCard.setCardDescription("Flat 50% discount");
        dealCard.setCardImage("/content/dam/newsportal/summer.png");
        dealCard.setCardLink("/content/newsportal/deals/summer.html");

        assertEquals("Summer Deal", dealCard.getCardTitle());
        assertEquals("Flat 50% discount", dealCard.getCardDescription());
        assertEquals("/content/dam/newsportal/summer.png", dealCard.getCardImage());
        assertEquals("/content/newsportal/deals/summer.html", dealCard.getCardLink());
    }
}