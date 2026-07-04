package com.bhaska.newsportal.core.listeners;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Collections;

import org.apache.sling.api.resource.*;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PageValidatoinListnerTest {

    private PageValidatoinListner listener;

    private ResourceResolverFactory resolverFactory;
    private ResourceResolver resolver;
    private Resource resource;
    private ResourceChange change;
    private ValueMap valueMap;
    private ModifiableValueMap modifiableValueMap;

    @BeforeEach
    void setup() throws Exception {

        listener = new PageValidatoinListner();
        resolverFactory = mock(ResourceResolverFactory.class);
        resolver = mock(ResourceResolver.class);
        resource = mock(Resource.class);
        change = mock(ResourceChange.class);
        valueMap = mock(ValueMap.class);
        modifiableValueMap = mock(ModifiableValueMap.class);
        Field field =
                PageValidatoinListner.class
                        .getDeclaredField("resourceResolverFactory");

        field.setAccessible(true);
        field.set(listener, resolverFactory);

        when(resolverFactory.getServiceResourceResolver(anyMap()))
                .thenReturn(resolver);

        when(change.getPath())
                .thenReturn("/content/newsportal/page1");
    }

    @Test
    void testValidPage() throws Exception {

        when(resolver.getResource(anyString()))
                .thenReturn(resource);
        when(resource.getValueMap())
                .thenReturn(valueMap);
        when(valueMap.get("jcr:title", String.class))
                .thenReturn("Title");
        when(valueMap.get("pageDescription", String.class))
                .thenReturn("Description");
        when(valueMap.get("authorName", String.class))
                .thenReturn("Aditya");
        listener.onChange(Collections.singletonList(change));
        verify(resolver, never()).commit();
    }


    @Test
    void testResourceNull() throws PersistenceException {

        when(resolver.getResource(anyString()))
                .thenReturn(null);

        listener.onChange(Collections.singletonList(change));

        verify(resolver, never()).commit();
    }

    @Test
    void testModifiableValueMapNull() throws PersistenceException {

        when(resolver.getResource(anyString()))
                .thenReturn(resource);

        when(resource.getValueMap())
                .thenReturn(valueMap);

        when(valueMap.get("jcr:title", String.class))
                .thenReturn("");

        when(valueMap.get("pageDescription", String.class))
                .thenReturn("Desc");

        when(valueMap.get("authorName", String.class))
                .thenReturn("Author");

        when(resource.adaptTo(ModifiableValueMap.class))
                .thenReturn(null);

        listener.onChange(Collections.singletonList(change));

        verify(resolver, never()).commit();
    }



    @Test
    void testException() throws Exception {

        when(resolverFactory.getServiceResourceResolver(anyMap()))
                .thenThrow(new RuntimeException());

        listener.onChange(Collections.singletonList(change));
    }

    @Test
    void testEmptyList() {

        listener.onChange(Collections.emptyList());

        verify(resolver, never()).getResource(anyString());
    }
}