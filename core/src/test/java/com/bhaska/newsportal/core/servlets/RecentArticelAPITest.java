package com.bhaska.newsportal.core.servlets;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecentArticelAPITest {

    private RecentArticelAPI servlet;

    private SlingHttpServletRequest request;
    private SlingHttpServletResponse response;
    private ResourceResolver resolver;
    private Resource resource;
    private ValueMap valueMap;

    private StringWriter writer;

    @BeforeEach
    void setUp() throws Exception {

        servlet = new RecentArticelAPI();

        request = mock(SlingHttpServletRequest.class);
        response = mock(SlingHttpServletResponse.class);
        resolver = mock(ResourceResolver.class);
        resource = mock(Resource.class);
        valueMap = mock(ValueMap.class);

        writer = new StringWriter();

        when(request.getResourceResolver()).thenReturn(resolver);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
    }

    // ===================== doGet =====================

    // ✅ 1. resource present + all values present
    @Test
    void testDoGet_AllValuesPresent() throws Exception {

        when(resolver.getResource(anyString())).thenReturn(resource);
        when(resource.getValueMap()).thenReturn(valueMap);

        when(valueMap.get("sling:resourceType", String.class)).thenReturn("type1");
        when(valueMap.get("type", String.class)).thenReturn("news");
        when(valueMap.get("limit", String.class)).thenReturn("10");

        servlet.doGet(request, response);

        String result = writer.toString();

        assertTrue(result.contains("type1"));
        assertTrue(result.contains("news"));
        assertTrue(result.contains("10"));
    }

    // ✅ 2. resource present + null values (branch)
    @Test
    void testDoGet_NullValues() throws Exception {

        when(resolver.getResource(anyString())).thenReturn(resource);
        when(resource.getValueMap()).thenReturn(valueMap);

        when(valueMap.get(anyString(), eq(String.class))).thenReturn(null);

        servlet.doGet(request, response);

        String result = writer.toString();

        // should contain empty values ""
        assertTrue(result.contains("\"\""));
    }

    // ✅ 3. resource null
    @Test
    void testDoGet_ResourceNull() throws Exception {

        when(resolver.getResource(anyString())).thenReturn(null);

        servlet.doGet(request, response);

        String result = writer.toString();

        assertTrue(result.contains("Resource not found"));
    }

    // ===================== doPost =====================

    // ✅ 4. success case
    @Test
    void testDoPost_Success() throws Exception {

        when(resolver.getResource(anyString())).thenReturn(resource);

        when(request.getParameter("userId")).thenReturn("user1");
        when(request.getParameter("email")).thenReturn("test@mail.com");
        when(request.getParameter("firstName")).thenReturn("Adi");
        when(request.getParameter("lastName")).thenReturn("Yadav");
        when(request.getParameter("password")).thenReturn("123");

        servlet.doPost(request, response);

        verify(resolver).create(eq(resource), eq("user1"), any(Map.class));
        verify(resolver).commit();

        assertTrue(writer.toString().contains("Data Save Successfully"));
    }

    // ✅ 5. resource null branch
    @Test
    void testDoPost_ResourceNull() throws Exception {

        when(resolver.getResource(anyString())).thenReturn(null);

        servlet.doPost(request, response);

        assertTrue(writer.toString().contains("missing"));
    }

    // ✅ 6. userId null branch
    @Test
    void testDoPost_UserIdNull() throws Exception {

        when(resolver.getResource(anyString())).thenReturn(resource);
        when(request.getParameter("userId")).thenReturn(null);

        servlet.doPost(request, response);

        assertTrue(writer.toString().contains("missing"));
    }

    // ✅ 7. both resource null + userId null (extra branch safety)
    @Test
    void testDoPost_BothNull() throws Exception {

        when(resolver.getResource(anyString())).thenReturn(null);
        when(request.getParameter("userId")).thenReturn(null);

        servlet.doPost(request, response);

        assertTrue(writer.toString().contains("missing"));
    }
}