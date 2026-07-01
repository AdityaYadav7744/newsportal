package com.bhaska.newsportal.core.schedulers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Arrays;

import javax.jcr.Session;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bhaska.newsportal.core.service.impl.NPUtilService;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.Replicator;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

class ScheduledContentJobConsumerExpiryClassTest {

    private ScheduledContentJobConsumerExpiryClass consumer;

    private ResourceResolverFactory resolverFactory;
    private QueryBuilder queryBuilder;
    private Replicator replicator;
    private NPUtilService npUtilService;

    private ResourceResolver resolver;
    private Session session;
    private Query query;
    private SearchResult searchResult;
    private Hit hit1;
    private Hit hit2;
    private Job job;

    @BeforeEach
    void setUp() throws Exception {

        consumer = new ScheduledContentJobConsumerExpiryClass();

        resolverFactory = mock(ResourceResolverFactory.class);
        queryBuilder = mock(QueryBuilder.class);
        replicator = mock(Replicator.class);
        npUtilService = mock(NPUtilService.class);

        resolver = mock(ResourceResolver.class);
        session = mock(Session.class);
        query = mock(Query.class);
        searchResult = mock(SearchResult.class);
        hit1 = mock(Hit.class);
        hit2 = mock(Hit.class);
        job = mock(Job.class);

        inject("resolverFactory", resolverFactory);
        inject("queryBuilder", queryBuilder);
        inject("replicator", replicator);
        inject("npUtilService", npUtilService);
    }

    private void inject(String fieldName, Object value) throws Exception {

        Field field =
                ScheduledContentJobConsumerExpiryClass.class
                        .getDeclaredField(fieldName);

        field.setAccessible(true);
        field.set(consumer, value);
    }

    @Test
    void testProcessSuccess() throws Exception {

        when(npUtilService.getResourceResolver())
                .thenReturn(resolver);

        when(resolver.adaptTo(Session.class))
                .thenReturn(session);

        when(queryBuilder.createQuery(any(PredicateGroup.class), eq(session)))
                .thenReturn(query);

        when(query.getResult())
                .thenReturn(searchResult);

        when(searchResult.getHits())
                .thenReturn(Arrays.asList(hit1, hit2));

        when(hit1.getPath())
                .thenReturn("/content/newsportal/us/en/test/page1/jcr:content");

        when(hit2.getPath())
                .thenReturn("/content/newsportal/us/en/test/page2/jcr:content");

        when(resolver.isLive()).thenReturn(true);

        JobConsumer.JobResult result = consumer.process(job);

        assertEquals(JobConsumer.JobResult.OK, result);

        verify(replicator).replicate(
                session,
                ReplicationActionType.DEACTIVATE,
                "/content/newsportal/us/en/test/page1");

        verify(replicator).replicate(
                session,
                ReplicationActionType.DEACTIVATE,
                "/content/newsportal/us/en/test/page2");

        verify(resolver).close();
    }


    @Test
    void testProcessSessionNull() throws Exception {

        when(npUtilService.getResourceResolver()).thenReturn(resolver);
        when(resolver.adaptTo(Session.class)).thenReturn(null);
        when(resolver.isLive()).thenReturn(true);

        JobConsumer.JobResult result = consumer.process(job);

        assertEquals(JobConsumer.JobResult.FAILED, result);

        verify(resolver).close();
        verifyNoInteractions(replicator);
    }

    @Test
    void testProcessNoHits() throws Exception {

        when(npUtilService.getResourceResolver()).thenReturn(resolver);
        when(resolver.adaptTo(Session.class)).thenReturn(session);

        when(queryBuilder.createQuery(any(PredicateGroup.class), eq(session)))
                .thenReturn(query);

        when(query.getResult()).thenReturn(searchResult);
        when(searchResult.getHits()).thenReturn(java.util.Collections.emptyList());

        when(resolver.isLive()).thenReturn(true);

        JobConsumer.JobResult result = consumer.process(job);

        assertEquals(JobConsumer.JobResult.OK, result);

        verifyNoInteractions(replicator);
        verify(resolver).close();
    }

    @Test
    void testProcessReplicationException() throws Exception {

        when(npUtilService.getResourceResolver()).thenReturn(resolver);
        when(resolver.adaptTo(Session.class)).thenReturn(session);

        when(queryBuilder.createQuery(any(PredicateGroup.class), eq(session)))
                .thenReturn(query);

        when(query.getResult()).thenReturn(searchResult);
        when(searchResult.getHits()).thenReturn(Arrays.asList(hit1));

        when(hit1.getPath())
                .thenReturn("/content/newsportal/us/en/test/page1/jcr:content");

        doThrow(new RuntimeException("Replication Failed"))
                .when(replicator)
                .replicate(any(Session.class),
                        eq(ReplicationActionType.DEACTIVATE),
                        anyString());

        when(resolver.isLive()).thenReturn(true);

        JobConsumer.JobResult result = consumer.process(job);

        // Exception is caught inside the loop
        assertEquals(JobConsumer.JobResult.OK, result);

        verify(replicator).replicate(any(Session.class),
                eq(ReplicationActionType.DEACTIVATE),
                anyString());

        verify(resolver).close();
    }

    @Test
    void testProcessCreateQueryException() throws Exception {

        when(npUtilService.getResourceResolver()).thenReturn(resolver);
        when(resolver.adaptTo(Session.class)).thenReturn(session);

        when(queryBuilder.createQuery(any(PredicateGroup.class), eq(session)))
                .thenThrow(new RuntimeException("Query Exception"));

        when(resolver.isLive()).thenReturn(true);

        JobConsumer.JobResult result = consumer.process(job);

        assertEquals(JobConsumer.JobResult.FAILED, result);

        verify(resolver).close();
    }

    @Test
    void testProcessResolverNotLive() throws Exception {

        when(npUtilService.getResourceResolver()).thenReturn(resolver);
        when(resolver.adaptTo(Session.class)).thenReturn(session);

        when(queryBuilder.createQuery(any(PredicateGroup.class), eq(session)))
                .thenReturn(query);

        when(query.getResult()).thenReturn(searchResult);
        when(searchResult.getHits()).thenReturn(java.util.Collections.emptyList());

        when(resolver.isLive()).thenReturn(false);

        JobConsumer.JobResult result = consumer.process(job);

        assertEquals(JobConsumer.JobResult.OK, result);

        verify(resolver, never()).close();
    }

    @Test
    void testProcessResolverException() throws Exception {

        when(npUtilService.getResourceResolver())
                .thenThrow(new RuntimeException("Resolver Exception"));

        JobConsumer.JobResult result = consumer.process(job);

        assertEquals(JobConsumer.JobResult.FAILED, result);

        verifyNoInteractions(replicator);
    }
}