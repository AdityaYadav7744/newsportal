package com.bhaska.newsportal.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class ProductCardModelTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        // Register the model class
        context.addModelsForClasses(ProductCardModel.class);

        // Load JSON data into /content
        context.load().json("/productCard.json", "/content");

        // Set current resource for adaptation
        context.currentResource("/content/productCardResource");
    }

    @Test
    void testProductCardModelGetters() {
        ProductCardModel model = context.currentResource().adaptTo(ProductCardModel.class);
        assertNotNull(model);

        // Validate all getters
        assertEquals("Smartphone", model.getProductTitle());
        assertEquals("15000", model.getProductPrice());
        assertEquals("Black", model.getProductColor());
        assertEquals("/content/dam/images/smartphone.jpg", model.getProductImage());
        assertEquals("2026-12-31", model.getProductExpiry());
    }
}