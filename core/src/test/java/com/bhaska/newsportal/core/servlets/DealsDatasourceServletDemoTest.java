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
import org.apache.sling.api.resource.*;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DealsDatasourceServletDemoTest {

    private DealsDatasourceServletDemo servlet;

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
    void setUp() {

        servlet = new DealsDatasourceServletDemo();

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
    }

    @Test
    void testDoGet_DataSourceNull() throws Exception {

        when(currentResource.getChild("datasource")).thenReturn(null);

        invokeDoGet();

        verify(request, never())
                .setAttribute(any(), any());
    }

    @Test
    void testDoGet_DealsRootNull() throws Exception {

        when(currentResource.getChild("datasource"))
                .thenReturn(datasourceResource);

        ValueMap vm = new ValueMapDecorator(Collections.singletonMap("type", "public"));

        when(datasourceResource.getValueMap()).thenReturn(vm);

        when(resolver.getResource("/content/newsportal/deals"))
                .thenReturn(null);

        invokeDoGet();

        verify(request)
                .setAttribute(eq(DataSource.class.getName()), any());
    }

    @Test
    void testDoGet_PageNull() throws Exception {

        when(currentResource.getChild("datasource"))
                .thenReturn(datasourceResource);

        ValueMap vm = new ValueMapDecorator(Collections.singletonMap("type", "public"));

        when(datasourceResource.getValueMap()).thenReturn(vm);

        when(resolver.getResource("/content/newsportal/deals"))
                .thenReturn(dealsRoot);

        Iterator<Resource> iterator =
                Collections.singletonList(childResource).iterator();

        when(dealsRoot.listChildren()).thenReturn(iterator);

        when(childResource.adaptTo(Page.class))
                .thenReturn(null);

        invokeDoGet();

        verify(request)
                .setAttribute(eq(DataSource.class.getName()), any());
    }

    @Test
    void testDoGet_DealComponentNull() throws Exception {

        when(currentResource.getChild("datasource"))
                .thenReturn(datasourceResource);

        ValueMap vm = new ValueMapDecorator(Collections.singletonMap("type", "public"));

        when(datasourceResource.getValueMap()).thenReturn(vm);

        when(resolver.getResource("/content/newsportal/deals"))
                .thenReturn(dealsRoot);

        Iterator<Resource> iterator =
                Collections.singletonList(childResource).iterator();

        when(dealsRoot.listChildren()).thenReturn(iterator);

        when(childResource.adaptTo(Page.class))
                .thenReturn(page);

        when(page.getContentResource())
                .thenReturn(contentResource);

        when(contentResource.getChild("root/container/deals"))
                .thenReturn(null);

        invokeDoGet();

        verify(request)
                .setAttribute(eq(DataSource.class.getName()), any());
    }

    @Test
    void testDoGet_PrivateDeal() throws Exception {

        when(currentResource.getChild("datasource"))
                .thenReturn(datasourceResource);

        ValueMap dsVm = new ValueMapDecorator(
                Collections.singletonMap("type", "private"));

        when(datasourceResource.getValueMap()).thenReturn(dsVm);

        when(resolver.getResource("/content/newsportal/deals"))
                .thenReturn(dealsRoot);

        Iterator<Resource> iterator =
                Collections.singletonList(childResource).iterator();

        when(dealsRoot.listChildren()).thenReturn(iterator);

        when(childResource.adaptTo(Page.class))
                .thenReturn(page);

        when(page.getContentResource())
                .thenReturn(contentResource);

        when(page.getTitle()).thenReturn("Gold");
        when(page.getPath()).thenReturn("/content/gold");

        when(contentResource.getChild("root/container/deals"))
                .thenReturn(dealsComponent);

        ValueMap dealVm =
                new ValueMapDecorator(
                        Collections.singletonMap("privateDeals", true));

        when(dealsComponent.getValueMap())
                .thenReturn(dealVm);

        invokeDoGet();

        verify(page).getTitle();
        verify(page).getPath();
    }

    @Test
    void testDoGet_PublicDeal() throws Exception {

        when(currentResource.getChild("datasource"))
                .thenReturn(datasourceResource);

        ValueMap dsVm = new ValueMapDecorator(
                Collections.singletonMap("type", "public"));

        when(datasourceResource.getValueMap()).thenReturn(dsVm);

        when(resolver.getResource("/content/newsportal/deals"))
                .thenReturn(dealsRoot);

        Iterator<Resource> iterator =
                Collections.singletonList(childResource).iterator();

        when(dealsRoot.listChildren()).thenReturn(iterator);

        when(childResource.adaptTo(Page.class))
                .thenReturn(page);

        when(page.getContentResource())
                .thenReturn(contentResource);

        when(page.getTitle()).thenReturn("Silver");
        when(page.getPath()).thenReturn("/content/silver");

        when(contentResource.getChild("root/container/deals"))
                .thenReturn(dealsComponent);

        ValueMap dealVm =
                new ValueMapDecorator(
                        Collections.singletonMap("privateDeals", false));

        when(dealsComponent.getValueMap())
                .thenReturn(dealVm);

        invokeDoGet();

        verify(page).getTitle();
        verify(page).getPath();
    }

    private void invokeDoGet() throws Exception {

        Method method =
                DealsDatasourceServletDemo.class.getDeclaredMethod(
                        "doGet",
                        SlingHttpServletRequest.class,
                        SlingHttpServletResponse.class);

        method.setAccessible(true);
        method.invoke(servlet, request, response);
    }
}