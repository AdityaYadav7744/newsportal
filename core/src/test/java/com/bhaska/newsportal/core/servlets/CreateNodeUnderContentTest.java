package com.bhaska.newsportal.core.servlets;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CreateNodeUnderContentTest {

    private CreateNodeUnderContent servlet;

    private SlingHttpServletRequest request;
    private SlingHttpServletResponse response;
    private ResourceResolver resolver;
    private Resource metadataResource;
    private Resource contentResource;

    @BeforeEach
    void setUp() {

        servlet = new CreateNodeUnderContent();

        request = mock(SlingHttpServletRequest.class);
        response = mock(SlingHttpServletResponse.class);
        resolver = mock(ResourceResolver.class);
        metadataResource = mock(Resource.class);
        contentResource = mock(Resource.class);
    }

    @Test
    void testDoGet_MetadataAlreadyExists() throws Exception {

        when(request.getResourceResolver()).thenReturn(resolver);

        when(resolver.getResource("/content/metadata"))
                .thenReturn(metadataResource);

        // No employee nodes exist
        when(metadataResource.getChild(anyString())).thenReturn(null);

        when(resolver.create(eq(metadataResource),
                anyString(),
                anyMap()))
                .thenReturn(mock(Resource.class));

        StringWriter stringWriter = new StringWriter();

        when(response.getWriter())
                .thenReturn(new PrintWriter(stringWriter));

        servlet.doGet(request, response);

        verify(resolver, times(100))
                .create(eq(metadataResource), anyString(), anyMap());

        verify(resolver).commit();

        assertTrue(stringWriter.toString()
                .contains("100  nodes are created"));
    }

    @Test
    void testDoGet_MetadataCreated() throws Exception {

        when(request.getResourceResolver()).thenReturn(resolver);

        when(resolver.getResource("/content/metadata"))
                .thenReturn(null);

        when(resolver.getResource("/content"))
                .thenReturn(contentResource);

        when(resolver.create(eq(contentResource),
                eq("metadata"),
                anyMap()))
                .thenReturn(metadataResource);

        when(metadataResource.getChild(anyString()))
                .thenReturn(null);

        when(resolver.create(eq(metadataResource),
                anyString(),
                anyMap()))
                .thenReturn(mock(Resource.class));

        StringWriter stringWriter = new StringWriter();

        when(response.getWriter())
                .thenReturn(new PrintWriter(stringWriter));

        servlet.doGet(request, response);

        verify(resolver).create(eq(contentResource),
                eq("metadata"),
                anyMap());

        verify(resolver, times(100))
                .create(eq(metadataResource), anyString(), anyMap());

        verify(resolver).commit();

        assertTrue(stringWriter.toString()
                .contains("100  nodes are created"));
    }

    @Test
    void testDoGet_WhenEmployeeAlreadyExists() throws Exception {

        when(request.getResourceResolver()).thenReturn(resolver);

        when(resolver.getResource("/content/metadata"))
                .thenReturn(metadataResource);

        // Employee nodes already exist
        when(metadataResource.getChild(anyString()))
                .thenReturn(mock(Resource.class));

        StringWriter stringWriter = new StringWriter();

        when(response.getWriter())
                .thenReturn(new PrintWriter(stringWriter));

        servlet.doGet(request, response);

        verify(resolver, never())
                .create(eq(metadataResource), anyString(), anyMap());

        verify(resolver).commit();

        assertTrue(stringWriter.toString()
                .contains("100  nodes are created"));
    }

    @Test
    void testDoGet_Exception() throws Exception {

        when(request.getResourceResolver()).thenReturn(resolver);

        when(resolver.getResource("/content/metadata"))
                .thenThrow(new RuntimeException("Test Exception"));

        StringWriter stringWriter = new StringWriter();

        when(response.getWriter())
                .thenReturn(new PrintWriter(stringWriter));

        servlet.doGet(request, response);

        assertTrue(stringWriter.toString().contains("Test Exception"));
    }
}