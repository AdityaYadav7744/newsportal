package com.bhaska.newsportal.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReadChieldPagesTitleModelTest {

    private ReadChieldPagesTitleModel model;

    private ResourceResolver resolver;
    private Resource resource;
    private Page parentPage;
    private Page child1;
    private Page child2;

    @BeforeEach
    void setUp() throws Exception {

        model = new ReadChieldPagesTitleModel();

        resolver = mock(ResourceResolver.class);
        resource = mock(Resource.class);
        parentPage = mock(Page.class);
        child1 = mock(Page.class);
        child2 = mock(Page.class);

        Field field = ReadChieldPagesTitleModel.class
                .getDeclaredField("resourceResolver");
        field.setAccessible(true);
        field.set(model, resolver);

        when(resolver.getResource("/content/newsportal/us/en"))
                .thenReturn(resource);

        when(resource.adaptTo(Page.class))
                .thenReturn(parentPage);

        when(child1.getTitle()).thenReturn("Home");
        when(child2.getTitle()).thenReturn("About");

        Iterator<Page> iterator =
                Arrays.asList(child1, child2).iterator();

        when(parentPage.listChildren()).thenReturn(iterator);
    }

    @Test
    void testInit() throws Exception {

        Method method =
                ReadChieldPagesTitleModel.class.getDeclaredMethod("init");

        method.setAccessible(true);
        method.invoke(model);

        List<String> titles = model.getList();

        assertEquals(2, titles.size());
        assertEquals("Home", titles.get(0));
        assertEquals("About", titles.get(1));
    }

    @Test
    void testNoChildren() throws Exception {

        when(parentPage.listChildren())
                .thenReturn(List.<Page>of().iterator());

        Method method =
                ReadChieldPagesTitleModel.class.getDeclaredMethod("init");

        method.setAccessible(true);
        method.invoke(model);

        assertEquals(0, model.getList().size());
    }
}