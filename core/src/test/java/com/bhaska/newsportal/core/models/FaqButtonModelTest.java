package com.bhaska.newsportal.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import com.day.cq.wcm.api.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FaqButtonModelTest {

    private FaqButtonModel model;
    private Page currentPage;
    private Page parentPage;

    @BeforeEach
    void setUp() throws Exception {

        model = new FaqButtonModel();

        currentPage = mock(Page.class);
        parentPage = mock(Page.class);

        Field field = FaqButtonModel.class.getDeclaredField("currentPage");
        field.setAccessible(true);
        field.set(model, currentPage);
    }

    @Test
    void testInit_WithParentPath() {

        when(currentPage.getParent()).thenReturn(parentPage);
        when(parentPage.getPath()).thenReturn("/content/newsportal/us/en");

        model.init();

        assertEquals("/content/newsportal/us/en.html", model.getPath());
    }

    @Test
    void testInit_WithNullParentPath() {

        when(currentPage.getParent()).thenReturn(parentPage);
        when(parentPage.getPath()).thenReturn(null);

        model.init();

        assertEquals("#", model.getPath());
    }
}