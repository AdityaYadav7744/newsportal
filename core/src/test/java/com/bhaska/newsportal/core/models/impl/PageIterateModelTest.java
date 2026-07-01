package com.bhaska.newsportal.core.models.impl;

import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PageIterateModelTest {

    private PageIterateModel model;

    private ResourceResolver resourceResolver;
    private Resource resource;

    @BeforeEach
    void setUp() throws Exception {

        model = new PageIterateModel();

        resourceResolver = mock(ResourceResolver.class);
        resource = mock(Resource.class);

        Field field = PageIterateModel.class.getDeclaredField("resourceResolver");
        field.setAccessible(true);
        field.set(model, resourceResolver);

        when(resourceResolver.getResource("/content/fruitables"))
                .thenReturn(resource);

        when(resource.listChildren())
                .thenReturn(Collections.emptyIterator());
    }

    @Test
    void testInit() throws Exception {

        Method method = PageIterateModel.class.getDeclaredMethod("inti");
        method.setAccessible(true);
        method.invoke(model);

        verify(resourceResolver).getResource("/content/fruitables");
        verify(resource).listChildren();
    }
}