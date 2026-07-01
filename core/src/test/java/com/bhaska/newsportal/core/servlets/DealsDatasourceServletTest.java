package com.bhaska.newsportal.core.servlets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Iterator;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.adobe.granite.ui.components.ds.DataSource;
import com.day.cq.wcm.api.Page;

class DealsDatasourceServletTest {

    private DealsDatasourceServlet servlet;

    private SlingHttpServletRequest request;
    private SlingHttpServletResponse response;

    private ResourceResolver resolver;
    private Resource currentResource;
    private Resource datasourceResource;

    private Resource dealsRoot;
    private Resource childResource;
    private Resource contentResource;
    private Resource dealsComponent;

    private Page page;

    @BeforeEach
    void setup() {

        servlet = new DealsDatasourceServlet();

        request = mock(SlingHttpServletRequest.class);
        response = mock(SlingHttpServletResponse.class);

        resolver = mock(ResourceResolver.class);

        currentResource = mock(Resource.class);
        datasourceResource = mock(Resource.class);

        dealsRoot = mock(Resource.class);
        childResource = mock(Resource.class);
        contentResource = mock(Resource.class);
        dealsComponent = mock(Resource.class);

        page = mock(Page.class);

        when(request.getResourceResolver()).thenReturn(resolver);
        when(request.getResource()).thenReturn(currentResource);

        when(currentResource.getChild("datasource"))
                .thenReturn(datasourceResource);
    }

    @Test
    void testDoGet_PublicDeal() throws Exception {

        when(currentResource.getPath())
                .thenReturn("/apps/private/publicDeals/test");

        when(resolver.getResource("/content/newsportal/deals"))
                .thenReturn(dealsRoot);

        Iterator<Resource> iterator =
                java.util.Collections.singletonList(childResource).iterator();

        when(dealsRoot.listChildren()).thenReturn(iterator);

        when(childResource.adaptTo(Page.class)).thenReturn(page);

        when(page.getContentResource()).thenReturn(contentResource);

        when(contentResource.getChild("root/container/deals"))
                .thenReturn(dealsComponent);

        when(dealsComponent.getValueMap()).thenReturn(new org.apache.sling.api.wrappers.ValueMapDecorator(new java.util.HashMap<>()));

        when(page.getTitle()).thenReturn("Public");

        when(page.getPath()).thenReturn("/content/newsportal/deals/public");

        Method m = DealsDatasourceServlet.class.getDeclaredMethod(
                "doGet",
                SlingHttpServletRequest.class,
                SlingHttpServletResponse.class);

        m.setAccessible(true);

        m.invoke(servlet, request, response);

        verify(request).setAttribute(eq(DataSource.class.getName()), any());
    }

    @Test
    void testDoGet_PrivateDeal() throws Exception {

        when(currentResource.getPath())
                .thenReturn("/apps/private/privateDeals/test");

        when(resolver.getResource("/content/newsportal/deals"))
                .thenReturn(dealsRoot);

        Iterator<Resource> iterator =
                java.util.Collections.singletonList(childResource).iterator();

        when(dealsRoot.listChildren()).thenReturn(iterator);

        when(childResource.adaptTo(Page.class)).thenReturn(page);

        when(page.getContentResource()).thenReturn(contentResource);

        when(contentResource.getChild("root/container/deals"))
                .thenReturn(dealsComponent);

        java.util.Map<String,Object> map=new java.util.HashMap<>();
        map.put("privateDeals",true);

        when(dealsComponent.getValueMap())
                .thenReturn(new org.apache.sling.api.wrappers.ValueMapDecorator(map));

        when(page.getTitle()).thenReturn("Private");

        when(page.getPath()).thenReturn("/content/newsportal/deals/private");

        Method m = DealsDatasourceServlet.class.getDeclaredMethod(
                "doGet",
                SlingHttpServletRequest.class,
                SlingHttpServletResponse.class);

        m.setAccessible(true);

        m.invoke(servlet, request, response);

        verify(request).setAttribute(eq(DataSource.class.getName()), any());
    }

    @Test
    void testDoGet_DealsRootNull() throws Exception {

        when(currentResource.getPath()).thenReturn("/apps/test");

        when(resolver.getResource("/content/newsportal/deals"))
                .thenReturn(null);

        Method m = DealsDatasourceServlet.class.getDeclaredMethod(
                "doGet",
                SlingHttpServletRequest.class,
                SlingHttpServletResponse.class);

        m.setAccessible(true);

        m.invoke(servlet, request, response);

        verify(request).setAttribute(eq(DataSource.class.getName()), any());
    }

    @Test
    void testDoGet_PageNull() throws Exception {

        when(currentResource.getPath()).thenReturn("/apps/test");

        when(resolver.getResource("/content/newsportal/deals"))
                .thenReturn(dealsRoot);

        Iterator<Resource> iterator =
                java.util.Collections.singletonList(childResource).iterator();

        when(dealsRoot.listChildren()).thenReturn(iterator);

        when(childResource.adaptTo(Page.class))
                .thenReturn(null);

        Method m = DealsDatasourceServlet.class.getDeclaredMethod(
                "doGet",
                SlingHttpServletRequest.class,
                SlingHttpServletResponse.class);

        m.setAccessible(true);

        m.invoke(servlet, request, response);

        verify(request).setAttribute(eq(DataSource.class.getName()), any());
    }

    @Test
    void testDoGet_DealsComponentNull() throws Exception {

        when(currentResource.getPath()).thenReturn("/apps/test");

        when(resolver.getResource("/content/newsportal/deals"))
                .thenReturn(dealsRoot);

        Iterator<Resource> iterator =
                java.util.Collections.singletonList(childResource).iterator();

        when(dealsRoot.listChildren()).thenReturn(iterator);

        when(childResource.adaptTo(Page.class)).thenReturn(page);

        when(page.getContentResource()).thenReturn(contentResource);

        when(contentResource.getChild("root/container/deals"))
                .thenReturn(null);

        Method m = DealsDatasourceServlet.class.getDeclaredMethod(
                "doGet",
                SlingHttpServletRequest.class,
                SlingHttpServletResponse.class);

        m.setAccessible(true);

        m.invoke(servlet, request, response);

        verify(request).setAttribute(eq(DataSource.class.getName()), any());
    }
}