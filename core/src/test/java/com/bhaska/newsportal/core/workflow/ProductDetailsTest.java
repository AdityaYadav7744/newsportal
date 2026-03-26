package com.bhaska.newsportal.core.workflow;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductDetailsTest {

    // ✅ Test constructor + all getters
    @Test
    void testAllGetters() {

        ProductDetails product = new ProductDetails(
                "Laptop",
                "High performance laptop",
                "50000",
                "image.png"
        );

        assertEquals("Laptop", product.getProductName());
        assertEquals("High performance laptop", product.getProductDescription());
        assertEquals("50000", product.getProductPrice());
        assertEquals("image.png", product.getProductImage());
    }

    // ✅ Edge case: null values (extra coverage safety)
    @Test
    void testNullValues() {

        ProductDetails product = new ProductDetails(
                null,
                null,
                null,
                null
        );

        assertNull(product.getProductName());
        assertNull(product.getProductDescription());
        assertNull(product.getProductPrice());
        assertNull(product.getProductImage());
    }

    // ✅ Edge case: empty values
    @Test
    void testEmptyValues() {

        ProductDetails product = new ProductDetails(
                "",
                "",
                "",
                ""
        );

        assertEquals("", product.getProductName());
        assertEquals("", product.getProductDescription());
        assertEquals("", product.getProductPrice());
        assertEquals("", product.getProductImage());
    }
}