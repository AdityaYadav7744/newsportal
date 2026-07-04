package com.bhaska.newsportal.core.servlets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Iterator;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PropertyIndexServletTest {

    private PropertyIndexServlet servlet;

    private SlingHttpServletRequest request;
    private SlingHttpServletResponse response;
    private ResourceResolver resolver;
    private Resource resource;

    @BeforeEach
    void setUp() {

        servlet = new PropertyIndexServlet();

        request = mock(SlingHttpServletRequest.class);
        response = mock(SlingHttpServletResponse.class);
        resolver = mock(ResourceResolver.class);
        resource = mock(Resource.class);
    }

    @Test
    void testDoGet_WithResults() throws Exception {

        when(request.getParameter("category")).thenReturn("sports");
        when(request.getParameter("author")).thenReturn("admin");
        when(request.getResourceResolver()).thenReturn(resolver);

        Iterator<Resource> iterator =
                Collections.singletonList(resource).iterator();

        when(resolver.findResources(anyString(), eq("JCR-SQL2")))
                .thenReturn(iterator);

        when(resource.getPath()).thenReturn("/content/newsportal/page1");
        when(resource.getName()).thenReturn("page1");

        StringWriter sw = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(sw));

        servlet.doGet(request, response);

        verify(response).setContentType("application/json");

        String json = sw.toString();

        assertEquals(
                "[{\"path\":\"/content/newsportal/page1\",\"name\":\"page1\"}]",
                json);
    }

    @Test
    void testDoGet_CategoryOrAuthorNull() throws Exception {

        when(request.getParameter("category")).thenReturn(null);
        when(request.getParameter("author")).thenReturn("admin");

        StringWriter sw = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(sw));

        servlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(request, never()).getResourceResolver();

        assertEquals("[]", sw.toString());
    }


}