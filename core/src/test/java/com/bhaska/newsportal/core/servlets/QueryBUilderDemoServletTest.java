package com.bhaska.newsportal.core.servlets;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Collections;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QueryBUilderDemoServletTest {

    private QueryBUilderDemoServlet servlet;

    private QueryBuilder queryBuilder;
    private SlingHttpServletRequest request;
    private SlingHttpServletResponse response;
    private ResourceResolver resolver;
    private Session session;
    private Query query;
    private SearchResult searchResult;
    private Hit hit;
    private Resource resource;
    private PageManager pageManager;
    private Page page;

    @BeforeEach
    void setUp() throws Exception {

        servlet = new QueryBUilderDemoServlet();

        queryBuilder = mock(QueryBuilder.class);
        request = mock(SlingHttpServletRequest.class);
        response = mock(SlingHttpServletResponse.class);
        resolver = mock(ResourceResolver.class);
        session = mock(Session.class);
        query = mock(Query.class);
        searchResult = mock(SearchResult.class);
        hit = mock(Hit.class);
        resource = mock(Resource.class);
        pageManager = mock(PageManager.class);
        page = mock(Page.class);

        Field field =
                QueryBUilderDemoServlet.class.getDeclaredField("queryBuilder");
        field.setAccessible(true);
        field.set(servlet, queryBuilder);
    }

    @Test
    void testDoGet() throws Exception {

        when(request.getResourceResolver()).thenReturn(resolver);
        when(resolver.adaptTo(Session.class)).thenReturn(session);
        when(resolver.adaptTo(PageManager.class)).thenReturn(pageManager);

        when(queryBuilder.createQuery(any(PredicateGroup.class), eq(session)))
                .thenReturn(query);

        when(query.getResult()).thenReturn(searchResult);
        when(searchResult.getHits())
                .thenReturn(Collections.singletonList(hit));

        when(hit.getResource()).thenReturn(resource);
        when(pageManager.getContainingPage(resource)).thenReturn(page);
        when(page.getPath()).thenReturn("/content/page1");

        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doGet(request, response);

        verify(response).getWriter();
        verify(pageManager).getContainingPage(resource);
        verify(page).getPath();
    }

    @Test
    void testDoGet_RepositoryException() throws Exception {

        when(request.getResourceResolver()).thenReturn(resolver);
        when(resolver.adaptTo(Session.class)).thenReturn(session);
        when(resolver.adaptTo(PageManager.class)).thenReturn(pageManager);

        when(queryBuilder.createQuery(any(PredicateGroup.class), eq(session)))
                .thenReturn(query);

        when(query.getResult()).thenReturn(searchResult);
        when(searchResult.getHits())
                .thenReturn(Collections.singletonList(hit));

        when(hit.getResource()).thenThrow(new RepositoryException());

        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        assertThrows(RuntimeException.class,
                () -> servlet.doGet(request, response));
    }
}