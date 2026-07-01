package com.bhaska.newsportal.core.schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.event.jobs.JobManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bhaska.newsportal.core.config.SiteMapConfiguration;

class SiteMapJobManagerTest {

    private SiteMapJobManager siteMapJobManager;

    private JobManager jobManager;
    private Scheduler scheduler;
    private ScheduleOptions scheduleOptions;
    private SiteMapConfiguration config;

    @BeforeEach
    void setUp() throws Exception {

        siteMapJobManager = new SiteMapJobManager();

        jobManager = mock(JobManager.class);
        scheduler = mock(Scheduler.class);
        scheduleOptions = mock(ScheduleOptions.class);
        config = mock(SiteMapConfiguration.class);

        inject("jobManager", jobManager);
        inject("scheduler", scheduler);
    }

    private void inject(String fieldName, Object value) throws Exception {

        Field field = SiteMapJobManager.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(siteMapJobManager, value);
    }

    @Test
    void testInitWhenStatusTrue() throws Exception {

        when(config.status()).thenReturn(true);
        when(config.pagePath()).thenReturn("/content/newsportal/us/en");
        when(config.cronExp()).thenReturn("0 0 * * * ?");

        when(scheduler.EXPR("0 0 * * * ?"))
                .thenReturn(scheduleOptions);

        when(scheduleOptions.name("site-map-job"))
                .thenReturn(scheduleOptions);

        when(scheduleOptions.canRunConcurrently(false))
                .thenReturn(scheduleOptions);

        Method method =
                SiteMapJobManager.class.getDeclaredMethod("init", SiteMapConfiguration.class);

        method.setAccessible(true);
        method.invoke(siteMapJobManager, config);

        verify(scheduler).unschedule("site-map-job");
        verify(scheduler).EXPR("0 0 * * * ?");
        verify(scheduleOptions).name("site-map-job");
        verify(scheduleOptions).canRunConcurrently(false);
        verify(scheduler).schedule(siteMapJobManager, scheduleOptions);
    }

    @Test
    void testInitWhenStatusFalse() throws Exception {

        when(config.status()).thenReturn(false);

        Method method =
                SiteMapJobManager.class.getDeclaredMethod("init", SiteMapConfiguration.class);

        method.setAccessible(true);
        method.invoke(siteMapJobManager, config);

        verifyNoInteractions(scheduleOptions);

        verify(scheduler, never()).EXPR(anyString());
        verify(scheduler, never()).schedule(any(Runnable.class), any());
        verify(scheduler, never()).unschedule(anyString());
    }

    @Test
    void testRun() throws Exception {

        Field field = SiteMapJobManager.class.getDeclaredField("pagePath");
        field.setAccessible(true);
        field.set(siteMapJobManager, "/content/newsportal/us/en");

        siteMapJobManager.run();

        verify(jobManager).addJob(
                eq("sitempa/scheduler"),
                any(Map.class));
    }
}