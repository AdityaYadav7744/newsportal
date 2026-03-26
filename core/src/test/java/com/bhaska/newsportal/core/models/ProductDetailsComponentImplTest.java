package com.bhaska.newsportal.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class ProductDetailsComponentImplTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        // Register model classes
        context.addModelsForClasses(ProductDetailsComponentImpl.class, MultiFieldsProd.class);

        // Load JSON content for both normal and empty cases
        context.load().json("/productDetails.json", "/content");
    }

    @Test
    void testAllProperties() {
        context.currentResource("/content/productDetailsComponent");
        ProductDetailsComponentImpl model = context.currentResource().adaptTo(ProductDetailsComponentImpl.class);
        assertNotNull(model);

        // Test simple getters
        assertEquals("Smartphone", model.getTitle());
        assertEquals("Latest 5G smartphone", model.getDescription());
        assertTrue(model.getProdStatus());
        assertEquals("Electronics", model.getCategory());

        // Test child resources
        List<MultiFieldsProd> cards = model.getProductCard();
        assertNotNull(cards);
        assertEquals(2, cards.size());

        MultiFieldsProd card1 = cards.get(0);
        assertEquals(null, card1.getProdTitle());
        assertEquals(null, card1.getProdPrice());
        assertEquals(null, card1.getProdImg());
        assertEquals(null, card1.getProdColor());
        assertNull(card1.getProductExpiryDate());
        assertFalse(card1.getProductExpired() instanceof Boolean);

        MultiFieldsProd card2 = cards.get(1);
        assertEquals(null, card2.getProdTitle());
        assertEquals(null, card2.getProdPrice());
        assertEquals(null, card2.getProdImg());
        assertEquals(null, card2.getProdColor());
        assertNull(card2.getProductExpiryDate());
        assertFalse(card2.getProductExpired() instanceof Boolean);
    }

    @Test
    void testEmptyProperties() {
        context.currentResource("/content/productDetailsComponentEmpty");
        ProductDetailsComponentImpl model = context.currentResource().adaptTo(ProductDetailsComponentImpl.class);
        assertNotNull(model);

        // All values should be null for optional fields
        assertNull(model.getTitle());
        assertNull(model.getDescription());
        assertNull(model.getProdStatus());
        assertNull(model.getCategory());

        // Child resource should be null
        assertNull(model.getProductCard());
    }

    @Test
    void testChildExpiryDateNull() {

        // Create resource manually (no expiry field)
        context.create().resource("/content/productDetailsComponentWithNullExpiry",
                "title", "Test Product"
        );

        context.create().resource("/content/productDetailsComponentWithNullExpiry/productCard/item0",
                "prodTitle", "Test",
                "prodPrice", "100",
                "prodImg", "img.jpg",
                "prodColor", "Red"
                // expiry NOT added → null case
        );

        context.currentResource("/content/productDetailsComponentWithNullExpiry");

        ProductDetailsComponentImpl model =
                context.currentResource().adaptTo(ProductDetailsComponentImpl.class);

        assertNotNull(model);

        List<MultiFieldsProd> cards = model.getProductCard();
        assertNotNull(cards);

        MultiFieldsProd card = cards.get(0);

        assertNull(card.getProductExpiryDate());
        assertNull(card.getProductExpired()); // covers null branch
    }
    @Test
    void testGettersCoverage() {

        // Create resource manually to guarantee values
        context.create().resource("/content/testProduct",
                "title", "Phone",
                "description", "Nice phone",
                "prodStatus", true,
                "category", "Electronics"
        );

        context.currentResource("/content/testProduct");

        ProductDetailsComponentImpl model =
                context.currentResource().adaptTo(ProductDetailsComponentImpl.class);

        assertNotNull(model);

        // ✅ Cover all getters
        assertEquals("Phone", model.getTitle());
        assertEquals("Nice phone", model.getDescription());
        assertTrue(model.getProdStatus());
        assertEquals("Electronics", model.getCategory());
    }
}