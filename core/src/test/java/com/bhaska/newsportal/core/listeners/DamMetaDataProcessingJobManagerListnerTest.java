package com.bhaska.newsportal.core.listeners;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.event.jobs.JobManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class DamMetaDataProcessingJobManagerListnerTest {

    private DamMetaDataProcessingJobManagerListner listener;

    private JobManager jobManager;

    @BeforeEach
    void setUp() throws Exception {

        listener = new DamMetaDataProcessingJobManagerListner();

        jobManager = mock(JobManager.class);

        Field field = DamMetaDataProcessingJobManagerListner.class
                .getDeclaredField("jobManager");
        field.setAccessible(true);
        field.set(listener, jobManager);
    }

    @Test
    void testOnChangeSingleResource() {

        ResourceChange change = mock(ResourceChange.class);

        when(change.getPath()).thenReturn("/content/dam/newsportal/image1.jpg");

        listener.onChange(Collections.singletonList(change));

        ArgumentCaptor<Map<String, Object>> captor =
                ArgumentCaptor.forClass(Map.class);

        verify(jobManager, times(1))
                .addJob(eq("dam/metadata/processing"), captor.capture());

        Map<String, Object> map = captor.getValue();

        assertEquals("/content/dam/newsportal/image1.jpg", map.get("assetPath"));
        assertEquals("system", map.get("triggeredBy"));
        assertNotNull(map.get("currentTime"));
    }

    @Test
    void testOnChangeMultipleResources() {

        ResourceChange change1 = mock(ResourceChange.class);
        ResourceChange change2 = mock(ResourceChange.class);

        when(change1.getPath()).thenReturn("/content/dam/newsportal/img1.jpg");
        when(change2.getPath()).thenReturn("/content/dam/newsportal/img2.jpg");

        listener.onChange(Arrays.asList(change1, change2));

        verify(jobManager, times(2))
                .addJob(eq("dam/metadata/processing"), anyMap());
    }

    @Test
    void testOnChangeEmptyList() {

        listener.onChange(Collections.emptyList());

        verify(jobManager, never())
                .addJob(eq("dam/metadata/processing"), anyMap());
    }
}