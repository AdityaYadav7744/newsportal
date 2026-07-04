package com.bhaska.newsportal.core.servlets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Collections;

import javax.jcr.Session;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PropertyIndexingQuerybuilderServletTest {

    private PropertyIndexingQuerybuilderServlet servlet;

    private QueryBuilder queryBuilder;
    private SlingHttpServletRequest request;
    private SlingHttpServletResponse response;
    private ResourceResolver resolver;
    private Session session;
    private Query query;
    private SearchResult searchResult;
    private Hit hit;
    private Resource resource;

    @BeforeEach
    void setUp() throws Exception {

        servlet = new PropertyIndexingQuerybuilderServlet();

        queryBuilder = mock(QueryBuilder.class);
        request = mock(SlingHttpServletRequest.class);
        response = mock(SlingHttpServletResponse.class);
        resolver = mock(ResourceResolver.class);
        session = mock(Session.class);
        query = mock(Query.class);
        searchResult = mock(SearchResult.class);
        hit = mock(Hit.class);
        resource = mock(Resource.class);

        Field field = PropertyIndexingQuerybuilderServlet.class
                .getDeclaredField("queryBuilder");
        field.setAccessible(true);
        field.set(servlet, queryBuilder);
    }

    @Test
    void testDoGet_Success() throws Exception {

        when(request.getParameter("category")).thenReturn("sports");
        when(request.getParameter("author")).thenReturn("admin");

        when(request.getResourceResolver()).thenReturn(resolver);
        when(resolver.adaptTo(Session.class)).thenReturn(session);

        when(queryBuilder.createQuery(any(PredicateGroup.class), eq(session)))
                .thenReturn(query);

        when(query.getResult()).thenReturn(searchResult);

        when(searchResult.getHits())
                .thenReturn(Collections.singletonList(hit));

        when(hit.getResource()).thenReturn(resource);

        when(resource.getPath()).thenReturn("/content/newsportal/page1");

        StringWriter sw = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(sw));

        servlet.doGet(request, response);

        verify(response).setContentType("application/json");

        assertEquals(
                "[{\"path\":\"/content/newsportal/page1\"}]",
                sw.toString());
    }


}