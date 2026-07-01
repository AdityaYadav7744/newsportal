package com.bhaska.newsportal.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.bhaska.newsportal.core.config.CAConfig;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomCAConfigModelTest {

    private CustomCAConfigModel model;

    private Page currentPage;
    private ResourceResolver resolver;
    private Resource resource;
    private ConfigurationBuilder builder;
    private CAConfig config;

    @BeforeEach
    void setUp() throws Exception {

        model = new CustomCAConfigModel();

        currentPage = mock(Page.class);
        resolver = mock(ResourceResolver.class);
        resource = mock(Resource.class);
        builder = mock(ConfigurationBuilder.class);
        config = mock(CAConfig.class);

        Field pageField =
                CustomCAConfigModel.class.getDeclaredField("currentPage");
        pageField.setAccessible(true);
        pageField.set(model, currentPage);

        Field resolverField =
                CustomCAConfigModel.class.getDeclaredField("resourceResolver");
        resolverField.setAccessible(true);
        resolverField.set(model, resolver);

        when(currentPage.getPath()).thenReturn("/content/newsportal");

        when(resolver.getResource("/content/newsportal"))
                .thenReturn(resource);

        when(resource.adaptTo(ConfigurationBuilder.class))
                .thenReturn(builder);

        when(builder.as(CAConfig.class))
                .thenReturn(config);

        when(config.siteName()).thenReturn("News Portal");
        when(config.language()).thenReturn("English");
    }

    @Test
    void testInit() throws Exception {

        Method init =
                CustomCAConfigModel.class.getDeclaredMethod("init");

        init.setAccessible(true);
        init.invoke(model);

        assertEquals("News Portal", model.getSiteName());
        assertEquals("English", model.getLanguage());
    }

    @Test
    void testBuilderNull() throws Exception {

        when(resource.adaptTo(ConfigurationBuilder.class))
                .thenReturn(null);

        Method init =
                CustomCAConfigModel.class.getDeclaredMethod("init");

        init.setAccessible(true);
        init.invoke(model);

        assertNull(model.getSiteName());
        assertNull(model.getLanguage());
    }

    @Test
    void testResourceNull() throws Exception {

        when(resolver.getResource("/content/newsportal"))
                .thenReturn(null);

        Method method =
                CustomCAConfigModel.class.getDeclaredMethod("getConfig",
                        String.class,
                        ResourceResolver.class);

        method.setAccessible(true);

        Object result =
                method.invoke(model,
                        "/content/newsportal",
                        resolver);

        assertNull(result);
    }
}