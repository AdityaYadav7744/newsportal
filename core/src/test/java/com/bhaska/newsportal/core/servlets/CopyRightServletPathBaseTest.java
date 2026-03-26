package com.bhaska.newsportal.core.servlets;


import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class CopyRightServletPathBaseTest {

    private final AemContext context = new AemContext();

    private CopyRightServletPathBase servlet;

    @BeforeEach
    void setUp() {
        servlet = new CopyRightServletPathBase();

        // Create mock resource
        context.create().resource("/content/test",
                "component-Text", "Hello Path",
                "copyright-text", "© 2026");
    }


    @Test
    void testDoGet_WithValidPath() throws Exception {

        context.request().setParameterMap(
                java.util.Collections.singletonMap("path", "/content/test")
        );

        servlet.doGet(context.request(), context.response());

        String result = context.response().getOutputAsString();

        assertTrue(result.contains("Hello Path"));
        assertTrue(result.contains("© 2026"));
    }


    @Test
    void testDoGet_PathMissing() throws Exception {

        servlet.doGet(context.request(), context.response());

        String result = context.response().getOutputAsString();

        assertTrue(result.contains("Path parameter is missing"));
    }


    @Test
    void testDoGet_ResourceNotFound() throws Exception {

        context.request().setParameterMap(
                java.util.Collections.singletonMap("path", "/content/invalid")
        );

        servlet.doGet(context.request(), context.response());

        String result = context.response().getOutputAsString();

        assertTrue(result.contains("Resource not found"));
    }


    @Test
    void testDoGet_EmptyProperties() throws Exception {

        context.create().resource("/content/empty");

        context.request().setParameterMap(
                java.util.Collections.singletonMap("path", "/content/empty")
        );

        servlet.doGet(context.request(), context.response());

        String result = context.response().getOutputAsString();

        assertTrue(result.contains("\"componentText\":\"\""));
        assertTrue(result.contains("\"copyrightText\":\"\""));
    }


    @Test
    void testContentType() throws Exception {

        context.request().setParameterMap(
                java.util.Collections.singletonMap("path", "/content/test")
        );

        servlet.doGet(context.request(), context.response());

        assertEquals("application/json;charset=UTF-8", context.response().getContentType());
    }
}