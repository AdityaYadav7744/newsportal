package com.bhaska.newsportal.core.schedulers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.event.jobs.JobManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bhaska.newsportal.core.config.UnpblishPageSchedulerConfig;

class UnpblishPageSchedulerJobManagerTest {

    private UnpblishPageSchedulerJobManager schedulerJobManager;

    private JobManager jobManager;
    private Scheduler scheduler;
    private ScheduleOptions scheduleOptions;
    private UnpblishPageSchedulerConfig config;

    @BeforeEach
    void setUp() throws Exception {

        schedulerJobManager = new UnpblishPageSchedulerJobManager();

        jobManager = mock(JobManager.class);
        scheduler = mock(Scheduler.class);
        scheduleOptions = mock(ScheduleOptions.class);
        config = mock(UnpblishPageSchedulerConfig.class);

        Field jobField =
                UnpblishPageSchedulerJobManager.class.getDeclaredField("jobManager");
        jobField.setAccessible(true);
        jobField.set(schedulerJobManager, jobManager);

        Field schedulerField =
                UnpblishPageSchedulerJobManager.class.getDeclaredField("scheduler");
        schedulerField.setAccessible(true);
        schedulerField.set(schedulerJobManager, scheduler);
    }

    @Test
    void testInitWhenStatusTrue() {

        when(config.status()).thenReturn(true);
        when(config.path()).thenReturn("/content/newsportal");
        when(config.cron()).thenReturn("0 0 * * * ?");

        when(scheduler.EXPR("0 0 * * * ?"))
                .thenReturn(scheduleOptions);

        when(scheduleOptions.name("unpublishpages"))
                .thenReturn(scheduleOptions);

        schedulerJobManager.inti(config);

        verify(scheduler).unschedule("unpublishpages");
        verify(scheduler).EXPR("0 0 * * * ?");
        verify(scheduleOptions).name("unpublishpages");
        verify(scheduler).schedule(schedulerJobManager, scheduleOptions);
    }

    @Test
    void testInitWhenStatusFalse() {

        when(config.status()).thenReturn(false);

        schedulerJobManager.inti(config);

        verify(scheduler, never()).unschedule(anyString());
        verify(scheduler, never()).EXPR(anyString());
        verify(scheduler, never()).schedule(any(Runnable.class), any());
    }

    @Test
    void testRun() throws Exception {

        Field field =
                UnpblishPageSchedulerJobManager.class.getDeclaredField("path");
        field.setAccessible(true);
        field.set(schedulerJobManager, "/content/newsportal");

        schedulerJobManager.run();

        verify(jobManager).addJob(
                eq("page/unpublish"),
                any(Map.class));
    }
}