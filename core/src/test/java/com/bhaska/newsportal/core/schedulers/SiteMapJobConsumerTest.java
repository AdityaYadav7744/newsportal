package com.bhaska.newsportal.core.schedulers;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bhaska.newsportal.core.servlets.NewsPortalSubService;

class SiteMapJobConsumerTest {

    private SiteMapJobConsumer consumer;

    private NewsPortalSubService service;
    private ResourceResolver resolver;
    private Job job;

    @BeforeEach
    void setUp() throws Exception {

        consumer = new SiteMapJobConsumer();

        service = mock(NewsPortalSubService.class);
        resolver = mock(ResourceResolver.class);
        job = mock(Job.class);

        Field field = SiteMapJobConsumer.class.getDeclaredField("service");
        field.setAccessible(true);
        field.set(consumer, service);
    }

    @Test
    void testProcessSuccess() throws Exception {

        when(job.getProperty("pagePath", String.class))
                .thenReturn("/content/newsportal");

        when(service.getResourceResolver())
                .thenReturn(resolver);

        JobConsumer.JobResult result = consumer.process(job);

        assertNull(result);

        verify(job).getProperty("pagePath", String.class);
        verify(service).getResourceResolver();
        verify(resolver).close();
    }

    @Test
    void testProcessException() throws Exception {

        when(job.getProperty("pagePath", String.class))
                .thenReturn("/content/newsportal");

        when(service.getResourceResolver())
                .thenThrow(new RuntimeException("Exception"));

        JobConsumer.JobResult result = consumer.process(job);

        assertNull(result);

        verify(job).getProperty("pagePath", String.class);
        verify(service).getResourceResolver();
    }
}