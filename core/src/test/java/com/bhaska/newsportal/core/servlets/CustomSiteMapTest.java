package com.bhaska.newsportal.core.servlets;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;

import com.day.cq.replication.ReplicationStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomSiteMapTest {

    private CustomSiteMap servlet;

    private SlingHttpServletRequest request;
    private SlingHttpServletResponse response;

    private ResourceResolver resolver;

    @BeforeEach
    void setUp() {

        servlet = new CustomSiteMap();

        request = mock(SlingHttpServletRequest.class);
        response = mock(SlingHttpServletResponse.class);

        resolver = mock(ResourceResolver.class);

        when(request.getResourceResolver()).thenReturn(resolver);
    }

    @Test
    void testDoGet_RootNull() throws Exception {

        StringWriter sw = new StringWriter();
        PrintWriter writer = new PrintWriter(sw);

        when(response.getWriter()).thenReturn(writer);

        when(resolver.getResource("/content/newsportal/us/en"))
                .thenReturn(null);

        servlet.doGet(request, response);

        verify(response).setContentType("application/xml");
    }

    @Test
    void testDoGet_ActivatedPage() throws Exception {

        StringWriter sw = new StringWriter();
        PrintWriter writer = new PrintWriter(sw);

        when(response.getWriter()).thenReturn(writer);

        Resource root = mock(Resource.class);
        Resource page = mock(Resource.class);
        Resource content = mock(Resource.class);

        ValueMap vm = mock(ValueMap.class);

        ReplicationStatus status = mock(ReplicationStatus.class);

        when(resolver.getResource("/content/newsportal/us/en"))
                .thenReturn(root);

        when(root.getChildren())
                .thenReturn(Collections.singletonList(page));

        when(page.getValueMap()).thenReturn(vm);

        when(vm.get("jcr:primaryType", "")).thenReturn("cq:Page");

        when(page.getChild("jcr:content")).thenReturn(content);

        when(content.adaptTo(ReplicationStatus.class))
                .thenReturn(status);

        when(status.isActivated()).thenReturn(true);

        when(page.getChildren()).thenReturn(Collections.emptyList());

        when(page.getPath()).thenReturn("/content/newsportal/us/en/page1");

        servlet.doGet(request, response);

        verify(status).isActivated();
    }

    @Test
    void testDoGet_NotActivated() throws Exception {

        StringWriter sw = new StringWriter();
        PrintWriter writer = new PrintWriter(sw);

        when(response.getWriter()).thenReturn(writer);

        Resource root = mock(Resource.class);
        Resource page = mock(Resource.class);
        Resource content = mock(Resource.class);

        ValueMap vm = mock(ValueMap.class);

        ReplicationStatus status = mock(ReplicationStatus.class);

        when(resolver.getResource(anyString())).thenReturn(root);

        when(root.getChildren())
                .thenReturn(Collections.singletonList(page));

        when(page.getValueMap()).thenReturn(vm);

        when(vm.get("jcr:primaryType", "")).thenReturn("cq:Page");

        when(page.getChild("jcr:content")).thenReturn(content);

        when(content.adaptTo(ReplicationStatus.class))
                .thenReturn(status);

        when(status.isActivated()).thenReturn(false);

        when(page.getChildren()).thenReturn(Collections.emptyList());

        servlet.doGet(request, response);

        verify(status).isActivated();
    }

    @Test
    void testDoGet_ContentNull() throws Exception {

        StringWriter sw = new StringWriter();
        PrintWriter writer = new PrintWriter(sw);

        when(response.getWriter()).thenReturn(writer);

        Resource root = mock(Resource.class);
        Resource page = mock(Resource.class);

        ValueMap vm = mock(ValueMap.class);

        when(resolver.getResource(anyString())).thenReturn(root);

        when(root.getChildren())
                .thenReturn(Collections.singletonList(page));

        when(page.getValueMap()).thenReturn(vm);

        when(vm.get("jcr:primaryType", "")).thenReturn("cq:Page");

        when(page.getChild("jcr:content")).thenReturn(null);

        when(page.getChildren()).thenReturn(Collections.emptyList());

        servlet.doGet(request, response);
    }

    @Test
    void testDoGet_ReplicationStatusNull() throws Exception {

        StringWriter sw = new StringWriter();
        PrintWriter writer = new PrintWriter(sw);

        when(response.getWriter()).thenReturn(writer);

        Resource root = mock(Resource.class);
        Resource page = mock(Resource.class);
        Resource content = mock(Resource.class);

        ValueMap vm = mock(ValueMap.class);

        when(resolver.getResource(anyString())).thenReturn(root);

        when(root.getChildren())
                .thenReturn(Collections.singletonList(page));

        when(page.getValueMap()).thenReturn(vm);

        when(vm.get("jcr:primaryType", "")).thenReturn("cq:Page");

        when(page.getChild("jcr:content")).thenReturn(content);

        when(content.adaptTo(ReplicationStatus.class))
                .thenReturn(null);

        when(page.getChildren()).thenReturn(Collections.emptyList());

        servlet.doGet(request, response);
    }

    @Test
    void testDoGet_RecursiveTraversal() throws Exception {

        StringWriter sw = new StringWriter();
        PrintWriter writer = new PrintWriter(sw);

        when(response.getWriter()).thenReturn(writer);

        Resource root = mock(Resource.class);
        Resource page1 = mock(Resource.class);
        Resource page2 = mock(Resource.class);

        Resource content1 = mock(Resource.class);
        Resource content2 = mock(Resource.class);

        ValueMap vm1 = mock(ValueMap.class);
        ValueMap vm2 = mock(ValueMap.class);

        ReplicationStatus status1 = mock(ReplicationStatus.class);
        ReplicationStatus status2 = mock(ReplicationStatus.class);

        when(resolver.getResource(anyString())).thenReturn(root);

        when(root.getChildren()).thenReturn(Collections.singletonList(page1));

        when(page1.getValueMap()).thenReturn(vm1);
        when(vm1.get("jcr:primaryType", "")).thenReturn("cq:Page");

        when(page1.getChild("jcr:content")).thenReturn(content1);
        when(content1.adaptTo(ReplicationStatus.class)).thenReturn(status1);
        when(status1.isActivated()).thenReturn(true);
        when(page1.getPath()).thenReturn("/content/page1");

        when(page1.getChildren()).thenReturn(Collections.singletonList(page2));

        when(page2.getValueMap()).thenReturn(vm2);
        when(vm2.get("jcr:primaryType", "")).thenReturn("cq:Page");

        when(page2.getChild("jcr:content")).thenReturn(content2);
        when(content2.adaptTo(ReplicationStatus.class)).thenReturn(status2);
        when(status2.isActivated()).thenReturn(true);
        when(page2.getPath()).thenReturn("/content/page2");

        when(page2.getChildren()).thenReturn(Collections.emptyList());

        servlet.doGet(request, response);

        verify(status1).isActivated();
        verify(status2).isActivated();
    }
}