package com.bhaska.newsportal.core.servlets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Iterator;

import javax.jcr.query.Query;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.Template;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SQL2Test {

    private SQL2 servlet;

    private SlingHttpServletRequest request;
    private SlingHttpServletResponse response;
    private ResourceResolver resolver;
    private Resource resource;
    private Page page;
    private Template template;

    @BeforeEach
    void setUp() {

        servlet = new SQL2();

        request = mock(SlingHttpServletRequest.class);
        response = mock(SlingHttpServletResponse.class);
        resolver = mock(ResourceResolver.class);
        resource = mock(Resource.class);
        page = mock(Page.class);
        template = mock(Template.class);
    }

    @Test
    void testDoGet_WithResults() throws Exception {

        when(request.getResourceResolver()).thenReturn(resolver);

        Iterator<Resource> iterator =
                Collections.singletonList(resource).iterator();

        when(resolver.findResources(anyString(), eq(Query.JCR_SQL2)))
                .thenReturn(iterator);

        when(resource.adaptTo(Page.class)).thenReturn(page);

        when(page.getTitle()).thenReturn("News Portal");
        when(page.getPath()).thenReturn("/content/newsportal");
        when(page.getTemplate()).thenReturn(template);

        when(template.toString())
                .thenReturn("/conf/newsportal/settings/wcm/templates/page-content");

        StringWriter sw = new StringWriter();

        when(response.getWriter())
                .thenReturn(new PrintWriter(sw));

        servlet.doGet(request, response);

        verify(response).setContentType("application/json");

        String json = sw.toString();

        assertTrue(json.contains("News Portal"));
        assertTrue(json.contains("/content/newsportal"));
        assertTrue(json.contains("/conf/newsportal/settings/wcm/templates/page-content"));
    }

    @Test
    void testDoGet_NoResults() throws Exception {

        when(request.getResourceResolver()).thenReturn(resolver);

        when(resolver.findResources(anyString(), eq(Query.JCR_SQL2)))
                .thenReturn(null);

        StringWriter sw = new StringWriter();

        when(response.getWriter())
                .thenReturn(new PrintWriter(sw));

        servlet.doGet(request, response);

        verify(response).setContentType("application/json");

        assertEquals("[]", sw.toString());
    }
}