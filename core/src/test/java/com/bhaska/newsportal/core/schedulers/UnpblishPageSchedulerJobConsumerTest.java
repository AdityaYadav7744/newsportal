package com.bhaska.newsportal.core.schedulers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import javax.jcr.Session;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.Replicator;

class UnpblishPageSchedulerJobConsumerTest {

    private UnpblishPageSchedulerJobConsumer consumer;

    private ResourceResolverFactory resolverFactory;
    private ResourceResolver resolver;
    private Replicator replicator;
    private Job job;
    private Resource rootResource;
    private Resource pageResource;
    private ValueMap valueMap;
    private Session session;

    @BeforeEach
    void setUp() throws Exception {

        consumer = new UnpblishPageSchedulerJobConsumer();

        resolverFactory = mock(ResourceResolverFactory.class);
        resolver = mock(ResourceResolver.class);
        replicator = mock(Replicator.class);
        job = mock(Job.class);
        rootResource = mock(Resource.class);
        pageResource = mock(Resource.class);
        valueMap = mock(ValueMap.class);
        session = mock(Session.class);

        Field field =
                UnpblishPageSchedulerJobConsumer.class.getDeclaredField("resourceResolverFactory");
        field.setAccessible(true);
        field.set(consumer, resolverFactory);

        field =
                UnpblishPageSchedulerJobConsumer.class.getDeclaredField("replicator");
        field.setAccessible(true);
        field.set(consumer, replicator);
    }

    @Test
    void testProcessResourceNull() throws Exception {

        when(job.getProperty("path", String.class))
                .thenReturn("/content/newsportal");

        when(resolverFactory.getServiceResourceResolver(anyMap()))
                .thenReturn(resolver);

        when(resolver.getResource("/content/newsportal"))
                .thenReturn(null);

        JobConsumer.JobResult result = consumer.process(job);

        assertEquals(JobConsumer.JobResult.FAILED, result);
    }

    @Test
    void testProcessExpiredPage() throws Exception {

        when(job.getProperty("path", String.class))
                .thenReturn("/content/newsportal");

        when(resolverFactory.getServiceResourceResolver(anyMap()))
                .thenReturn(resolver);

        when(resolver.getResource(anyString()))
                .thenReturn(rootResource);

        Iterator<Resource> iterator =
                Arrays.asList(pageResource).iterator();

        when(resolver.findResources(anyString(), eq("JCR-SQL2")))
                .thenReturn(iterator);

        when(pageResource.getValueMap())
                .thenReturn(valueMap);

        when(valueMap.get("isExpired", false))
                .thenReturn(true);

        when(pageResource.getPath())
                .thenReturn("/content/newsportal/page1");

        when(resolver.adaptTo(Session.class))
                .thenReturn(session);

        JobConsumer.JobResult result = consumer.process(job);

        assertEquals(JobConsumer.JobResult.OK, result);

        verify(replicator).replicate(
                session,
                ReplicationActionType.DEACTIVATE,
                "/content/newsportal/page1");
    }

    @Test
    void testProcessNotExpiredPage() throws Exception {

        when(job.getProperty("path", String.class))
                .thenReturn("/content/newsportal");

        when(resolverFactory.getServiceResourceResolver(anyMap()))
                .thenReturn(resolver);

        when(resolver.getResource(anyString()))
                .thenReturn(rootResource);

        Iterator<Resource> iterator =
                Collections.singletonList(pageResource).iterator();

        when(resolver.findResources(anyString(), eq("JCR-SQL2")))
                .thenReturn(iterator);

        when(pageResource.getValueMap())
                .thenReturn(valueMap);

        when(valueMap.get("isExpired", false))
                .thenReturn(false);

        JobConsumer.JobResult result = consumer.process(job);

        assertEquals(JobConsumer.JobResult.OK, result);

        verifyNoInteractions(replicator);
    }

    @Test
    void testProcessException() throws Exception {

        when(resolverFactory.getServiceResourceResolver(anyMap()))
                .thenThrow(new RuntimeException("Exception"));

        JobConsumer.JobResult result = consumer.process(job);

        assertEquals(JobConsumer.JobResult.OK, result);
    }
}