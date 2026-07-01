package com.bhaska.newsportal.core.models;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class ProductItemDetailsTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(ProductItemDetails.class);
        context.load().json("/productitemdetails.json", "/content");
    }

    @Test
    void testFutureExpiryProduct() {

        Resource resource = context.resourceResolver()
                .getResource("/content/product");

        ProductItemDetails model = resource.adaptTo(ProductItemDetails.class);
        assertNotNull(model);
        assertEquals("Samsung Galaxy S25", model.getProductTitle());
        assertEquals("2099-12-31", model.getProductExpiry());
        assertEquals(85000, model.getProductprice());
        assertEquals("/content/dam/newsportal/mobile.jpg", model.getProductImage());
        assertEquals("Black", model.getProductColor());
        assertEquals("Premium", model.getProductTag());
        assertFalse(model.isExpired());
    }

    @Test
    void testExpiredProduct() {

        Resource resource = context.resourceResolver()
                .getResource("/content/expiredProduct");

        ProductItemDetails model = resource.adaptTo(ProductItemDetails.class);

        assertTrue(model.isExpired());
    }

    @Test
    void testInvalidDate() {

        Resource resource = context.resourceResolver()
                .getResource("/content/invalidDate");

        ProductItemDetails model = resource.adaptTo(ProductItemDetails.class);

        assertFalse(model.isExpired());
    }

    @Test
    void testNoExpiryDate() {

        Resource resource = context.resourceResolver()
                .getResource("/content/noExpiry");

        ProductItemDetails model = resource.adaptTo(ProductItemDetails.class);

        assertNull(model.getProductExpiry());

        assertFalse(model.isExpired());
    }
}