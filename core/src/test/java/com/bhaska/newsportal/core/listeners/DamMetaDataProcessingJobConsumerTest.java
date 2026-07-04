package com.bhaska.newsportal.core.listeners;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import com.day.cq.dam.api.Asset;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.apache.sling.settings.SlingSettingsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DamMetaDataProcessingJobConsumerTest {

    private DamMetaDataProcessingJobConsumer consumer;

    private ResourceResolverFactory resolverFactory;
    private SlingSettingsService slingSettingsService;
    private ResourceResolver resolver;
    private Resource assetResource;
    private Resource varResource;
    private Resource reportRoot;
    private Asset asset;
    private Job job;

    @BeforeEach
    void setUp() throws Exception {

        consumer = new DamMetaDataProcessingJobConsumer();

        resolverFactory = mock(ResourceResolverFactory.class);
        slingSettingsService = mock(SlingSettingsService.class);
        resolver = mock(ResourceResolver.class);
        assetResource = mock(Resource.class);
        varResource = mock(Resource.class);
        reportRoot = mock(Resource.class);
        asset = mock(Asset.class);
        job = mock(Job.class);

        Field field1 =
                DamMetaDataProcessingJobConsumer.class
                        .getDeclaredField("resourceResolverFactory");
        field1.setAccessible(true);
        field1.set(consumer, resolverFactory);

        Field field2 =
                DamMetaDataProcessingJobConsumer.class
                        .getDeclaredField("slingSettingsService");
        field2.setAccessible(true);
        field2.set(consumer, slingSettingsService);
    }

    @Test
    void testProcessPublishRunMode() throws Exception {

        when(job.getProperty("assetPath", String.class))
                .thenReturn("/content/dam/test.jpg");
        when(job.getProperty("currentTime", String.class))
                .thenReturn("10:00");
        when(job.getProperty("triggeredBy", String.class))
                .thenReturn("system");

        when(resolverFactory.getServiceResourceResolver(anyMap()))
                .thenReturn(resolver);

        when(slingSettingsService.getRunModes())
                .thenReturn(Collections.singleton("publish"));

        assertEquals(JobConsumer.JobResult.FAILED,
                consumer.process(job));
    }

    @Test
    void testProcessAuthorRunMode() throws Exception {

        when(job.getProperty("assetPath", String.class))
                .thenReturn("/content/dam/test.jpg");
        when(job.getProperty("currentTime", String.class))
                .thenReturn("10:00");
        when(job.getProperty("triggeredBy", String.class))
                .thenReturn("system");

        when(resolverFactory.getServiceResourceResolver(anyMap()))
                .thenReturn(resolver);

        when(slingSettingsService.getRunModes())
                .thenReturn(new HashSet<>());

        when(resolver.getResource("/content/dam/test.jpg"))
                .thenReturn(assetResource);

        when(assetResource.adaptTo(Asset.class))
                .thenReturn(asset);

        when(resolver.getResource("/var/report"))
                .thenReturn(reportRoot);

        assertEquals(JobConsumer.JobResult.OK,
                consumer.process(job));

        verify(resolver).create(eq(reportRoot),
                eq("assetReport"),
                anyMap());

        verify(resolver).commit();
    }

    @Test
    void testProcessWhenReportFolderDoesNotExist() throws Exception {

        when(job.getProperty("assetPath", String.class))
                .thenReturn("/content/dam/test.jpg");
        when(job.getProperty("currentTime", String.class))
                .thenReturn("10:00");
        when(job.getProperty("triggeredBy", String.class))
                .thenReturn("system");

        when(resolverFactory.getServiceResourceResolver(anyMap()))
                .thenReturn(resolver);

        when(slingSettingsService.getRunModes())
                .thenReturn(new HashSet<>());

        when(resolver.getResource("/content/dam/test.jpg"))
                .thenReturn(assetResource);

        when(assetResource.adaptTo(Asset.class))
                .thenReturn(asset);

        when(resolver.getResource("/var/report"))
                .thenReturn(null);

        when(resolver.getResource("/var"))
                .thenReturn(varResource);

        assertEquals(JobConsumer.JobResult.OK,
                consumer.process(job));

        verify(resolver).create(eq(varResource),
                eq("report"),
                anyMap());

        verify(resolver).commit();
    }


}