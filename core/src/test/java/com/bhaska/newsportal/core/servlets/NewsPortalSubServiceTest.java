package com.bhaska.newsportal.core.servlets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NewsPortalSubServiceTest {

    private NewsPortalSubService service;

    private ResourceResolverFactory resourceResolverFactory;
    private ResourceResolver resourceResolver;

    @BeforeEach
    void setUp() throws Exception {

        service = new NewsPortalSubService();

        resourceResolverFactory = mock(ResourceResolverFactory.class);
        resourceResolver = mock(ResourceResolver.class);

        Field field = NewsPortalSubService.class
                .getDeclaredField("resourceResolverFactory");
        field.setAccessible(true);
        field.set(service, resourceResolverFactory);
    }

    @Test
    void testGetResourceResolverSuccess() throws Exception {

        when(resourceResolverFactory.getServiceResourceResolver(anyMap()))
                .thenReturn(resourceResolver);

        ResourceResolver result = service.getResourceResolver();

        assertNotNull(result);
        assertEquals(resourceResolver, result);

        verify(resourceResolverFactory, times(1))
                .getServiceResourceResolver(anyMap());
    }

    @Test
    void testGetResourceResolverException() throws Exception {

        when(resourceResolverFactory.getServiceResourceResolver(anyMap()))
                .thenThrow(new LoginException("Login Failed"));

        ResourceResolver result = service.getResourceResolver();

        assertNull(result);

        verify(resourceResolverFactory, times(1))
                .getServiceResourceResolver(anyMap());
    }
}