package com.bhaska.newsportal.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IterateChildPagesModelTest {

    private IterateChildPagesModel model;

    private Page parentPage;
    private Page child1;
    private Page child2;

    private ValueMap vm1;
    private ValueMap vm2;

    @BeforeEach
    void setUp() throws Exception {

        model = new IterateChildPagesModel();

        parentPage = mock(Page.class);
        child1 = mock(Page.class);
        child2 = mock(Page.class);

        vm1 = mock(ValueMap.class);
        vm2 = mock(ValueMap.class);

        Field field =
                IterateChildPagesModel.class.getDeclaredField("page");

        field.setAccessible(true);
        field.set(model, parentPage);

        Iterator<Page> iterator =
                Arrays.asList(child1, child2).iterator();

        when(parentPage.listChildren()).thenReturn(iterator);

        when(child1.getProperties()).thenReturn(vm1);
        when(child2.getProperties()).thenReturn(vm2);

        when(vm1.get("jcr:title", String.class)).thenReturn("Home");
        when(vm1.get("path", String.class)).thenReturn("/content/home");

        when(vm2.get("jcr:title", String.class)).thenReturn("About");
        when(vm2.get("path", String.class)).thenReturn("/content/about");
    }

    @Test
    void testInit() throws Exception {

        Method method =
                IterateChildPagesModel.class.getDeclaredMethod("init");

        method.setAccessible(true);
        method.invoke(model);

        List<String> pages = model.getListOfPages();

        assertEquals(4, pages.size());

        assertEquals("Home", pages.get(0));
        assertEquals("/content/home", pages.get(1));
        assertEquals("About", pages.get(2));
        assertEquals("/content/about", pages.get(3));
    }

    @Test
    void testEmptyChildren() throws Exception {

        when(parentPage.listChildren())
                .thenReturn(List.<Page>of().iterator());

        Method method =
                IterateChildPagesModel.class.getDeclaredMethod("init");

        method.setAccessible(true);
        method.invoke(model);

        assertEquals(0, model.getListOfPages().size());
    }
}