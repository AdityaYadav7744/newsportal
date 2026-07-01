package com.bhaska.newsportal.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DealsModelClassTest {

    private DealsModelClass model;

    private ResourceResolver resourceResolver;
    private PageManager pageManager;
    private Page rootPage;
    private Page publicPage;
    private Page privatePage;

    @BeforeEach
    void setUp() throws Exception {

        model = new DealsModelClass();

        resourceResolver = mock(ResourceResolver.class);
        pageManager = mock(PageManager.class);

        rootPage = mock(Page.class);
        publicPage = mock(Page.class);
        privatePage = mock(Page.class);

        Field field =
                DealsModelClass.class.getDeclaredField("resourceResolver");

        field.setAccessible(true);
        field.set(model, resourceResolver);

        when(resourceResolver.adaptTo(PageManager.class))
                .thenReturn(pageManager);

        when(pageManager.getPage("/content/newsportal/deals"))
                .thenReturn(rootPage);
    }

    @Test
    void testInit() {

        Resource publicContent = mock(Resource.class);
        Resource privateContent = mock(Resource.class);

        Resource publicDeals = mock(Resource.class);
        Resource privateDeals = mock(Resource.class);

        ValueMap publicVm = mock(ValueMap.class);
        ValueMap privateVm = mock(ValueMap.class);

        when(rootPage.listChildren()).thenReturn(
                Arrays.asList(publicPage, privatePage).iterator());

        // Public Page
        when(publicPage.getContentResource()).thenReturn(publicContent);
        when(publicContent.getChild("root/container/deals"))
                .thenReturn(publicDeals);
        when(publicDeals.getValueMap()).thenReturn(publicVm);
        when(publicVm.get("privateDeal", false)).thenReturn(false);

        // Private Page
        when(privatePage.getContentResource()).thenReturn(privateContent);
        when(privateContent.getChild("root/container/deals"))
                .thenReturn(privateDeals);
        when(privateDeals.getValueMap()).thenReturn(privateVm);
        when(privateVm.get("privateDeal", false)).thenReturn(true);

        model.init();

        assertEquals(1, model.getPublicDeals().size());
        assertEquals(1, model.getPrivateDeals().size());

        assertEquals(publicPage, model.getPublicDeals().get(0));
        assertEquals(privatePage, model.getPrivateDeals().get(0));
    }

    @Test
    void testRootPageNull() {

        when(pageManager.getPage("/content/newsportal/deals"))
                .thenReturn(null);

        model.init();

        assertEquals(0, model.getPublicDeals().size());
        assertEquals(0, model.getPrivateDeals().size());
    }

    @Test
    void testDealsComponentNull() {

        Resource content = mock(Resource.class);

        when(rootPage.listChildren())
                .thenReturn(Collections.singletonList(publicPage).iterator());

        when(publicPage.getContentResource()).thenReturn(content);

        when(content.getChild("root/container/deals"))
                .thenReturn(null);

        model.init();

        assertEquals(0, model.getPublicDeals().size());
        assertEquals(0, model.getPrivateDeals().size());
    }
}