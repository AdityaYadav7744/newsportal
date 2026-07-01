package com.bhaska.newsportal.core.servlets;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import javax.jcr.query.Query;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SQL2DemoTest {

    private SQL2Demo servlet;

    private SlingHttpServletRequest request;
    private SlingHttpServletResponse response;

    private ResourceResolver resolver;

    @BeforeEach
    void setUp() {

        servlet = new SQL2Demo();

        request = mock(SlingHttpServletRequest.class);
        response = mock(SlingHttpServletResponse.class);
        resolver = mock(ResourceResolver.class);

        when(request.getResourceResolver()).thenReturn(resolver);
    }

    @Test
    void testDoGet_WithContentResource() throws Exception {

        Resource page = mock(Resource.class);
        Resource content = mock(Resource.class);

        ValueMap valueMap = mock(ValueMap.class);

        Iterator<Resource> iterator =
                Arrays.asList(page).iterator();

        when(resolver.findResources(anyString(), eq(Query.JCR_SQL2)))
                .thenReturn(iterator);

        when(page.getChild("jcr:content"))
                .thenReturn(content);

        when(content.getValueMap())
                .thenReturn(valueMap);

        when(valueMap.get("firstName", ""))
                .thenReturn("Aditya");

        when(valueMap.get("lastName", ""))
                .thenReturn("Yadav");

        when(valueMap.get("jcr:title", ""))
                .thenReturn("Developer");

        StringWriter sw = new StringWriter();

        when(response.getWriter())
                .thenReturn(new PrintWriter(sw));

        servlet.doGet(request, response);

        verify(response).setContentType("application/json");

        String json = sw.toString();

        assertTrue(json.contains("Aditya"));
        assertTrue(json.contains("Yadav"));
        assertTrue(json.contains("Developer"));
    }

    @Test
    void testDoGet_ContentResourceNull() throws Exception {

        Resource page = mock(Resource.class);

        Iterator<Resource> iterator =
                Collections.singletonList(page).iterator();

        when(resolver.findResources(anyString(), eq(Query.JCR_SQL2)))
                .thenReturn(iterator);

        when(page.getChild("jcr:content"))
                .thenReturn(null);

        StringWriter sw = new StringWriter();

        when(response.getWriter())
                .thenReturn(new PrintWriter(sw));

        servlet.doGet(request, response);

        verify(response).setContentType("application/json");

        assertEquals("[]", sw.toString());
    }

    @Test
    void testDoGet_NoResources() throws Exception {

        when(resolver.findResources(anyString(), eq(Query.JCR_SQL2)))
                .thenReturn(Collections.emptyIterator());

        StringWriter sw = new StringWriter();

        when(response.getWriter())
                .thenReturn(new PrintWriter(sw));

        servlet.doGet(request, response);

        verify(response).setContentType("application/json");

        assertEquals("[]", sw.toString());
    }
}