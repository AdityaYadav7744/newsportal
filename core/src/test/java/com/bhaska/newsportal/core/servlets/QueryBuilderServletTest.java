package com.bhaska.newsportal.core.servlets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

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
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QueryBuilderServletTest {

    private QueryBuilderServlet servlet;

    private QueryBuilder queryBuilder;
    private SlingHttpServletRequest request;
    private SlingHttpServletResponse response;
    private ResourceResolver resolver;
    private Session session;
    private Query query;
    private SearchResult searchResult;
    private Hit hit;
    private Resource resource;
    private Resource contentResource;
    private ValueMap valueMap;

    @BeforeEach
    void setUp() throws Exception {

        servlet = new QueryBuilderServlet();

        queryBuilder = mock(QueryBuilder.class);
        request = mock(SlingHttpServletRequest.class);
        response = mock(SlingHttpServletResponse.class);
        resolver = mock(ResourceResolver.class);
        session = mock(Session.class);
        query = mock(Query.class);
        searchResult = mock(SearchResult.class);
        hit = mock(Hit.class);
        resource = mock(Resource.class);
        contentResource = mock(Resource.class);
        valueMap = mock(ValueMap.class);

        Field field =
                QueryBuilderServlet.class.getDeclaredField("queryBuilder");
        field.setAccessible(true);
        field.set(servlet, queryBuilder);
    }

    @Test
    void testDoGet() throws Exception {

        when(request.getResourceResolver()).thenReturn(resolver);
        when(resolver.adaptTo(Session.class)).thenReturn(session);

        when(queryBuilder.createQuery(any(PredicateGroup.class), eq(session)))
                .thenReturn(query);

        when(query.getResult()).thenReturn(searchResult);

        when(searchResult.getHits())
                .thenReturn(Collections.singletonList(hit));

        when(hit.getResource()).thenReturn(resource);

        when(resource.getChild("jcr:content"))
                .thenReturn(contentResource);

        when(contentResource.adaptTo(ValueMap.class))
                .thenReturn(valueMap);

        when(resource.getPath())
                .thenReturn("/content/newsportal/page1");

        when(valueMap.get("cq:template", ""))
                .thenReturn("/conf/template");

        when(valueMap.get("jcr:title", ""))
                .thenReturn("Home Page");

        StringWriter writer = new StringWriter();

        when(response.getWriter())
                .thenReturn(new PrintWriter(writer));

        servlet.doGet(request, response);

        verify(response).setContentType("application/json");

        String json = writer.toString();

        assertTrue(json.contains("Home Page"));
        assertTrue(json.contains("/content/newsportal/page1"));
        assertTrue(json.contains("/conf/template"));
    }

    @Test
    void testDoGet_RepositoryException() throws Exception {

        when(request.getResourceResolver()).thenReturn(resolver);
        when(resolver.adaptTo(Session.class)).thenReturn(session);

        when(queryBuilder.createQuery(any(PredicateGroup.class), eq(session)))
                .thenReturn(query);

        when(query.getResult()).thenReturn(searchResult);

        when(searchResult.getHits())
                .thenReturn(Collections.singletonList(hit));

        when(hit.getResource())
                .thenThrow(new javax.jcr.RepositoryException());

        StringWriter writer = new StringWriter();

        when(response.getWriter())
                .thenReturn(new PrintWriter(writer));

        assertThrows(RuntimeException.class,
                () -> servlet.doGet(request, response));
    }
}