package com.bhaska.newsportal.core.models;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class ProductItemDetailsTest {

    // Utility to set private field
    private void setField(Object obj, String fieldName, Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    // =========================
    // ✅ CASE 1: NULL (skip if)
    // =========================
    @Test
    void testInit_NullExpiry() throws Exception {

        ProductItemDetails model = new ProductItemDetails();

        setField(model, "productExpiry", null);

        model.init();

        assertFalse(model.isExpired());
    }

    // =========================
    // ✅ CASE 2: PAST DATE
    // =========================
    @Test
    void testInit_PastDate() throws Exception {

        ProductItemDetails model = new ProductItemDetails();

        setField(model, "productExpiry", "2000-01-01");

        model.init();

        assertTrue(model.isExpired()); // expired
    }

    // =========================
    // ✅ CASE 3: FUTURE DATE
    // =========================
    @Test
    void testInit_FutureDate() throws Exception {

        ProductItemDetails model = new ProductItemDetails();

        setField(model, "productExpiry", "2099-12-31");

        model.init();

        assertFalse(model.isExpired()); // not expired
    }

    // =========================
    // ✅ CASE 4: INVALID FORMAT (catch block)
    // =========================
    @Test
    void testInit_InvalidDate() throws Exception {

        ProductItemDetails model = new ProductItemDetails();

        setField(model, "productExpiry", "wrong-date");

        model.init();

        assertFalse(model.isExpired()); // from catch block
    }
}