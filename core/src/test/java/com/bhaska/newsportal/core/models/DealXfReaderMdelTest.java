package com.bhaska.newsportal.core.models;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.day.cq.wcm.api.Page;

class DealXfReaderMdelTest {

    private DealXfReaderMdel model;
    private ResourceResolver resourceResolver;
    private Page currentPage;

    @BeforeEach
    void setUp() throws Exception {

        model = new DealXfReaderMdel();

        resourceResolver = mock(ResourceResolver.class);
        currentPage = mock(Page.class);

        setField("resourceResolver", resourceResolver);
        setField("currentPage", currentPage);
    }

    private void setField(String fieldName, Object value) throws Exception {
        Field field = DealXfReaderMdel.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(model, value);
    }

    private void invokeInit() throws Exception {
        Method method = DealXfReaderMdel.class.getDeclaredMethod("init");
        method.setAccessible(true);
        method.invoke(model);
    }

    @Test
    void testInit_NormalPage() throws Exception {

        when(currentPage.getName()).thenReturn("silver");

        Resource resource = mock(Resource.class);
        ValueMap vm = mock(ValueMap.class);

        when(resourceResolver.getResource(
                "/content/experience-fragments/newsportal/deals/silver/master/jcr:content/root/deals_xf_component"))
                .thenReturn(resource);

        when(resource.getValueMap()).thenReturn(vm);

        when(vm.get("cardImageReference", String.class))
                .thenReturn("/content/dam/silver.png");

        when(vm.get("longDescription", String.class))
                .thenReturn("Silver Long Description");

        when(vm.get("selectedDeal", String.class))
                .thenReturn("/content/newsportal/us/en/deals/silver");

        invokeInit();

        assertEquals("/content/dam/silver.png", model.getImage());
        assertEquals("Silver Long Description", model.getLongDescription());
        assertEquals("silver", model.getId());
    }

    @Test
    void testInit_GoldePage() throws Exception {

        when(currentPage.getName()).thenReturn("golde");

        Resource resource = mock(Resource.class);
        ValueMap vm = mock(ValueMap.class);

        when(resourceResolver.getResource(
                "/content/experience-fragments/newsportal/deals/golden/master/jcr:content/root/deals_xf_component"))
                .thenReturn(resource);

        when(resource.getValueMap()).thenReturn(vm);

        when(vm.get("cardImageReference", String.class))
                .thenReturn("/content/dam/golden.png");

        when(vm.get("longDescription", String.class))
                .thenReturn("Golden Description");

        when(vm.get("selectedDeal", String.class))
                .thenReturn("/content/newsportal/us/en/deals/golden");

        invokeInit();

        assertEquals("/content/dam/golden.png", model.getImage());
        assertEquals("Golden Description", model.getLongDescription());
        assertEquals("golden", model.getId());
    }

    @Test
    void testCurrentPageNull() throws Exception {

        setField("currentPage", null);

        invokeInit();

        assertNull(model.getImage());
        assertNull(model.getLongDescription());
        assertNull(model.getId());
    }

    @Test
    void testResourceNull() throws Exception {

        when(currentPage.getName()).thenReturn("silver");

        when(resourceResolver.getResource(anyString()))
                .thenReturn(null);

        invokeInit();

        assertNull(model.getImage());
        assertNull(model.getLongDescription());
        assertNull(model.getId());
    }
}