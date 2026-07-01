package com.bhaska.newsportal.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import com.day.cq.wcm.api.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OpenGraphModelTest {

    private OpenGraphModel model;
    private Page currentPage;

    @BeforeEach
    void setUp() throws Exception {

        model = new OpenGraphModel();

        currentPage = mock(Page.class);

        when(currentPage.getTitle()).thenReturn("Current Page");

        setField("currentPage", currentPage);
    }

    private void setField(String fieldName, Object value) throws Exception {

        Field field = OpenGraphModel.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(model, value);
    }

    @Test
    void testGetOgTitle_WhenOgTitleExists() throws Exception {

        setField("ogTitle", "OG Title");

        assertEquals("OG Title", model.getOgTitle());
    }

    @Test
    void testGetOgTitle_WhenMetaTitleExists() throws Exception {

        setField("metaTitle", "Meta Title");

        assertEquals("Meta Title", model.getOgTitle());
    }

    @Test
    void testGetOgTitle_WhenBothNull() {

        assertEquals("Current Page", model.getOgTitle());
    }

    @Test
    void testGetOgDescription_WhenOgDescriptionExists() throws Exception {

        setField("ogDescription", "OG Description");

        assertEquals("OG Description", model.getOgDescription());
    }

    @Test
    void testGetOgDescription_WhenOgDescriptionMissing() throws Exception {

        setField("metaDescription", "Meta Description");

        assertEquals("Meta Description", model.getOgDescription());
    }

    @Test
    void testGetOgImage() throws Exception {

        setField("ogImage", "/content/dam/image.jpg");

        assertEquals("/content/dam/image.jpg", model.getOgImage());
    }

    @Test
    void testGetOgType_WhenPresent() throws Exception {

        setField("ogType", "article");

        assertEquals("article", model.getOgType());
    }

    @Test
    void testGetOgType_DefaultValue() {

        assertEquals("website", model.getOgType());
    }
}