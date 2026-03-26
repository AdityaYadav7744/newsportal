package com.bhaska.newsportal.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class MultiFieldsProdTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(MultiFieldsProd.class);
        context.load().json("/multiFieldsProd.json", "/content");
    }

    @Test
    void testFutureExpiry() {
        context.currentResource("/content/productWithFutureExpiry");
        MultiFieldsProd model = context.currentResource().adaptTo(MultiFieldsProd.class);
        assertNotNull(model);

        assertEquals("Laptop", model.getProdTitle());
        assertNotNull(model.getProductExpiryDate());
        assertEquals("$1200", model.getProdPrice());
        assertEquals("/content/dam/laptop.jpg", model.getProdImg());
        assertEquals("Silver", model.getProdColor());

        // branch: future date → productExpired = true
        assertTrue(model.getProductExpired());
    }

    @Test
    void testPastExpiry() {
        context.currentResource("/content/productWithPastExpiry");
        MultiFieldsProd model = context.currentResource().adaptTo(MultiFieldsProd.class);
        assertNotNull(model);

        assertEquals("Old Laptop", model.getProdTitle());

        // branch: past date → productExpired = false
        assertFalse(model.getProductExpired());
    }

    @Test
    void testNullExpiry() {
        context.currentResource("/content/productWithNullExpiry");
        MultiFieldsProd model = context.currentResource().adaptTo(MultiFieldsProd.class);
        assertNotNull(model);

        // branch: productExpiryDate == null → productExpired = null
        assertNull(model.getProductExpired());

        assertEquals("Mystery Laptop", model.getProdTitle());
    }
}