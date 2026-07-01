package com.bhaska.newsportal.core.servlets;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class FetchApiServletPOJOTest {

    @Test
    void testGettersAndSetters() {

        FetchApiServletPOJO pojo = new FetchApiServletPOJO();

        pojo.setId(101);
        pojo.setTitle("Laptop");
        pojo.setPrice(59999.99);
        pojo.setImage("/content/dam/laptop.png");

        assertEquals(101, pojo.getId());
        assertEquals("Laptop", pojo.getTitle());
        assertEquals(59999.99, pojo.getPrice());
        assertEquals("/content/dam/laptop.png", pojo.getImage());
    }
}