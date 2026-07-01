package com.bhaska.newsportal.core.listeners;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import com.bhaska.newsportal.core.servlets.NewsPortalSubService;
import com.day.cq.replication.ReplicationAction;
import com.day.cq.replication.ReplicationOptions;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AutorMistakePreventionSystemTest {

    private AutorMistakePreventionSystem preprocessor;

    private NewsPortalSubService subService;
    private ResourceResolver resolver;
    private Resource resource;
    private ValueMap valueMap;
    private ReplicationAction replicationAction;
    private ReplicationOptions replicationOptions;

    @BeforeEach
    void setUp() throws Exception {

        preprocessor = new AutorMistakePreventionSystem();

        subService = mock(NewsPortalSubService.class);
        resolver = mock(ResourceResolver.class);
        resource = mock(Resource.class);
        valueMap = mock(ValueMap.class);
        replicationAction = mock(ReplicationAction.class);
        replicationOptions = mock(ReplicationOptions.class);

        Field field = AutorMistakePreventionSystem.class
                .getDeclaredField("subService");
        field.setAccessible(true);
        field.set(preprocessor, subService);
    }

    @Test
    void testPreprocess_WhenTitleExists() throws Exception {

        when(replicationAction.getPath()).thenReturn("/content/news/page1");
        when(subService.getResourceResolver()).thenReturn(resolver);
        when(resolver.getResource("/content/news/page1")).thenReturn(resource);
        when(resource.adaptTo(ValueMap.class)).thenReturn(valueMap);
        when(valueMap.get("jcr:title", String.class)).thenReturn("News Title");

        assertDoesNotThrow(() ->
                preprocessor.preprocess(replicationAction, replicationOptions));

        verify(resolver).close();
    }

    @Test
    void testPreprocess_WhenTitleIsNull() throws Exception {

        when(replicationAction.getPath()).thenReturn("/content/news/page1");
        when(subService.getResourceResolver()).thenReturn(resolver);
        when(resolver.getResource("/content/news/page1")).thenReturn(resource);
        when(resource.adaptTo(ValueMap.class)).thenReturn(valueMap);
        when(valueMap.get("jcr:title", String.class)).thenReturn(null);

        // Exception is swallowed by the catch block
        assertDoesNotThrow(() ->
                preprocessor.preprocess(replicationAction, replicationOptions));

        verify(resolver).close();
    }

    @Test
    void testPreprocess_WhenResourceResolverThrowsException() throws Exception {

        when(replicationAction.getPath()).thenReturn("/content/news/page1");
        when(subService.getResourceResolver())
                .thenThrow(new RuntimeException("Resolver Error"));

        assertDoesNotThrow(() ->
                preprocessor.preprocess(replicationAction, replicationOptions));
    }
}