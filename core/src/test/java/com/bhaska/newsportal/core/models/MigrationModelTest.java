package com.bhaska.newsportal.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MigrationModelTest {

    private MigrationModel model;

    private Page currentPage;
    private ValueMap valueMap;

    @BeforeEach
    void setUp() throws Exception {

        model = new MigrationModel();

        currentPage = mock(Page.class);
        valueMap = mock(ValueMap.class);

        Field field = MigrationModel.class.getDeclaredField("currentPage");
        field.setAccessible(true);
        field.set(model, currentPage);

        when(currentPage.getProperties()).thenReturn(valueMap);

        when(valueMap.get("jcr:title", String.class)).thenReturn("News Portal");
        when(valueMap.get("jcr:description", String.class)).thenReturn("News Description");
        when(valueMap.get("bodyHtml", String.class)).thenReturn("<p>Welcome</p>");
        when(valueMap.get("image", String.class)).thenReturn("/content/dam/newsportal/image.png");
    }

    @Test
    void testInit() throws Exception {

        Method method = MigrationModel.class.getDeclaredMethod("init");
        method.setAccessible(true);
        method.invoke(model);

        assertEquals("News Portal", model.getTitle());
        assertEquals("News Description", model.getDescription());
        assertEquals("<p>Welcome</p>", model.getBodyHtml());
        assertEquals("/content/dam/newsportal/image.png", model.getImage());
    }

    @Test
    void testInitWhenCurrentPageIsNull() throws Exception {

        MigrationModel model = new MigrationModel();

        Method method = MigrationModel.class.getDeclaredMethod("init");
        method.setAccessible(true);
        method.invoke(model);

        assertNull(model.getTitle());
        assertNull(model.getDescription());
        assertNull(model.getBodyHtml());
        assertNull(model.getImage());
    }
}