package com.bhaska.newsportal.core.models;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DealsTwoTabModelTest {

    private DealsTwoTabModel model;
    private ResourceResolver resolver;

    @BeforeEach
    void setUp() throws Exception {

        model = new DealsTwoTabModel();
        resolver = mock(ResourceResolver.class);

        Field resolverField =
                DealsTwoTabModel.class.getDeclaredField("resourceResolver");
        resolverField.setAccessible(true);
        resolverField.set(model, resolver);
    }

    private void setField(String fieldName, Object value) throws Exception {
        Field field = DealsTwoTabModel.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(model, value);
    }

    private void invokeInit() throws Exception {
        Method method = DealsTwoTabModel.class.getDeclaredMethod("init");
        method.setAccessible(true);
        method.invoke(model);
    }

    @Test
    void testInit_PublicAndPrivateDeals() throws Exception {

        setField("publicDeals",
                Collections.singletonList("/content/config/public/silver"));

        setField("privateDeals",
                Collections.singletonList("/content/config/private/golden"));

        Resource publicResource = mock(Resource.class);
        Resource privateResource = mock(Resource.class);

        ValueMap publicVm = mock(ValueMap.class);
        ValueMap privateVm = mock(ValueMap.class);

        when(resolver.getResource(
                "/content/newsportal/us/en/deals/silver/jcr:content/root/container/container/deals_cards"))
                .thenReturn(publicResource);

        when(resolver.getResource(
                "/content/newsportal/us/en/deals/golde/jcr:content/root/container/container/deals_cards"))
                .thenReturn(privateResource);

        when(publicResource.getValueMap()).thenReturn(publicVm);
        when(privateResource.getValueMap()).thenReturn(privateVm);

        when(publicVm.get("cardTitle", String.class)).thenReturn("Silver Deal");
        when(publicVm.get("cardDescription", String.class)).thenReturn("Silver Description");
        when(publicVm.get("cardImageReference", String.class)).thenReturn("/content/dam/silver.png");

        when(privateVm.get("cardTitle", String.class)).thenReturn("Golden Deal");
        when(privateVm.get("cardDescription", String.class)).thenReturn("Golden Description");
        when(privateVm.get("cardImageReference", String.class)).thenReturn("/content/dam/golden.png");

        invokeInit();

        assertEquals(1, model.getPublicDealCards().size());
        assertEquals(1, model.getPrivateDealCards().size());

        DealCardDetail publicCard = model.getPublicDealCards().get(0);

        assertEquals("Silver Deal", publicCard.getCardTitle());
        assertEquals("Silver Description", publicCard.getCardDescription());
        assertEquals("/content/dam/silver.png", publicCard.getCardImage());
        assertEquals("/content/newsportal/us/en/deals/silver.html",
                publicCard.getCardLink());

        DealCardDetail privateCard = model.getPrivateDealCards().get(0);

        assertEquals("Golden Deal", privateCard.getCardTitle());
        assertEquals("/content/newsportal/us/en/deals/golde.html",
                privateCard.getCardLink());
    }

    @Test
    void testSelectedPathsNull() throws Exception {

        setField("publicDeals", null);
        setField("privateDeals", null);

        invokeInit();

        assertTrue(model.getPublicDealCards().isEmpty());
        assertTrue(model.getPrivateDealCards().isEmpty());
    }

    @Test
    void testResourceNotFound() throws Exception {

        setField("publicDeals",
                Collections.singletonList("/content/config/public/silver"));

        when(resolver.getResource(anyString())).thenReturn(null);

        invokeInit();

        assertTrue(model.getPublicDealCards().isEmpty());
    }

    @Test
    void testCardLinkGetterSetter() {

        model.setCardLink("/content/test.html");

        assertEquals("/content/test.html", model.getCardLink());
    }
}