package com.bhaska.newsportal.core.servlets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;

import com.adobe.granite.ui.components.ds.DataSource;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DealsCardDatasourceServletTest {

    private DealsCardDatasourceServlet servlet;

    private SlingHttpServletRequest request;
    private SlingHttpServletResponse response;

    private ResourceResolver resolver;

    private Resource root;
    private Resource child;

    private Page page;

    @BeforeEach
    void setUp() {

        servlet = new DealsCardDatasourceServlet();

        request = mock(SlingHttpServletRequest.class);
        response = mock(SlingHttpServletResponse.class);

        resolver = mock(ResourceResolver.class);

        root = mock(Resource.class);
        child = mock(Resource.class);

        page = mock(Page.class);

        when(request.getResourceResolver()).thenReturn(resolver);
    }

    @Test
    void testDoGet_RootNull() throws Exception {

        when(resolver.getResource("/content/newsportal/deals"))
                .thenReturn(null);

        Method method = DealsCardDatasourceServlet.class.getDeclaredMethod(
                "doGet",
                SlingHttpServletRequest.class,
                SlingHttpServletResponse.class);

        method.setAccessible(true);
        method.invoke(servlet, request, response);

        verify(request, never())
                .setAttribute(any(), any());
    }

    @Test
    void testDoGet_PageNull() throws Exception {

        when(resolver.getResource("/content/newsportal/deals"))
                .thenReturn(root);

        Iterator<Resource> iterator =
                Collections.singletonList(child).iterator();

        when(root.listChildren()).thenReturn(iterator);

        when(child.adaptTo(Page.class)).thenReturn(null);

        Method method = DealsCardDatasourceServlet.class.getDeclaredMethod(
                "doGet",
                SlingHttpServletRequest.class,
                SlingHttpServletResponse.class);

        method.setAccessible(true);
        method.invoke(servlet, request, response);

        verify(request)
                .setAttribute(eq(DataSource.class.getName()), any(DataSource.class));
    }

    @Test
    void testDoGet_PageExists() throws Exception {

        when(resolver.getResource("/content/newsportal/deals"))
                .thenReturn(root);

        Iterator<Resource> iterator =
                Collections.singletonList(child).iterator();

        when(root.listChildren()).thenReturn(iterator);

        when(child.adaptTo(Page.class)).thenReturn(page);

        when(page.getTitle()).thenReturn("Gold Deal");
        when(page.getPath()).thenReturn("/content/newsportal/deals/gold");

        Method method = DealsCardDatasourceServlet.class.getDeclaredMethod(
                "doGet",
                SlingHttpServletRequest.class,
                SlingHttpServletResponse.class);

        method.setAccessible(true);
        method.invoke(servlet, request, response);

        verify(page).getTitle();
        verify(page).getPath();

        verify(request)
                .setAttribute(eq(DataSource.class.getName()), any(DataSource.class));
    }

    @Test
    void testDoGet_NoChildren() throws Exception {

        when(resolver.getResource("/content/newsportal/deals"))
                .thenReturn(root);

        when(root.listChildren())
                .thenReturn(Collections.emptyIterator());

        Method method = DealsCardDatasourceServlet.class.getDeclaredMethod(
                "doGet",
                SlingHttpServletRequest.class,
                SlingHttpServletResponse.class);

        method.setAccessible(true);
        method.invoke(servlet, request, response);

        verify(request)
                .setAttribute(eq(DataSource.class.getName()), any(DataSource.class));
    }
}