package com.bhaska.newsportal.core.servlets;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CopyRightResourceBaseServletTest {

    private CopyRightResourceBaseServlet servlet;

    private SlingHttpServletRequest request;
    private SlingHttpServletResponse response;
    private Resource resource;
    private ValueMap valueMap;

    private StringWriter stringWriter;

    @BeforeEach
    void setUp() throws Exception {

        servlet = new CopyRightResourceBaseServlet();

        request = mock(SlingHttpServletRequest.class);
        response = mock(SlingHttpServletResponse.class);
        resource = mock(Resource.class);
        valueMap = mock(ValueMap.class);

        stringWriter = new StringWriter();

        when(request.getResource()).thenReturn(resource);
        when(resource.getValueMap()).thenReturn(valueMap);
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
    }

    // ✅ 1. Both values present
    @Test
    void testDoGet_AllValuesPresent() throws Exception {

        when(valueMap.get("component-Text", String.class)).thenReturn("Hello");
        when(valueMap.get("copyright-text", String.class)).thenReturn("©2025");

        servlet.doGet(request, response);

        String result = stringWriter.toString();

        assertTrue(result.contains("Hello"));
        assertTrue(result.contains("©2025"));
    }

    // ✅ 2. componentText null → should become ""
    @Test
    void testDoGet_ComponentTextNull() throws Exception {

        when(valueMap.get("component-Text", String.class)).thenReturn(null);
        when(valueMap.get("copyright-text", String.class)).thenReturn("©2025");

        servlet.doGet(request, response);

        String result = stringWriter.toString();

        assertTrue(result.contains("\"componentText\":\"\""));
        assertTrue(result.contains("©2025"));
    }

    // ✅ 3. copyrightText null → ""
    @Test
    void testDoGet_CopyrightTextNull() throws Exception {

        when(valueMap.get("component-Text", String.class)).thenReturn("Hello");
        when(valueMap.get("copyright-text", String.class)).thenReturn(null);

        servlet.doGet(request, response);

        String result = stringWriter.toString();

        assertTrue(result.contains("Hello"));
        assertTrue(result.contains("\"copyrightText\":\"\""));
    }

    // ✅ 4. both null → both ""
    @Test
    void testDoGet_BothNull() throws Exception {

        when(valueMap.get("component-Text", String.class)).thenReturn(null);
        when(valueMap.get("copyright-text", String.class)).thenReturn(null);

        servlet.doGet(request, response);

        String result = stringWriter.toString();

        assertTrue(result.contains("\"componentText\":\"\""));
        assertTrue(result.contains("\"copyrightText\":\"\""));
    }

    // ✅ 5. empty values
    @Test
    void testDoGet_EmptyValues() throws Exception {

        when(valueMap.get("component-Text", String.class)).thenReturn("");
        when(valueMap.get("copyright-text", String.class)).thenReturn("");

        servlet.doGet(request, response);

        String result = stringWriter.toString();

        assertTrue(result.contains("\"componentText\":\"\""));
        assertTrue(result.contains("\"copyrightText\":\"\""));
    }

    // ✅ 6. verify content type
    @Test
    void testContentType() throws Exception {

        when(valueMap.get(anyString(), eq(String.class))).thenReturn("test");

        servlet.doGet(request, response);

        verify(response).setContentType("application/json");
    }
}