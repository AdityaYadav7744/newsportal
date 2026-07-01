package com.bhaska.newsportal.core.schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.event.jobs.JobManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ScheduledContentJobManagerExpiryClassTest {

    private ScheduledContentJobManagerExpiryClass schedulerClass;

    private JobManager jobManager;
    private Scheduler scheduler;
    private ScheduledContentExpiry config;
    private ScheduleOptions scheduleOptions;

    @BeforeEach
    void setUp() throws Exception {

        schedulerClass = new ScheduledContentJobManagerExpiryClass();

        jobManager = mock(JobManager.class);
        scheduler = mock(Scheduler.class);
        config = mock(ScheduledContentExpiry.class);
        scheduleOptions = mock(ScheduleOptions.class);

        Field jobManagerField =
                ScheduledContentJobManagerExpiryClass.class.getDeclaredField("jobManager");
        jobManagerField.setAccessible(true);
        jobManagerField.set(schedulerClass, jobManager);

        Field schedulerField =
                ScheduledContentJobManagerExpiryClass.class.getDeclaredField("scheduler");
        schedulerField.setAccessible(true);
        schedulerField.set(schedulerClass, scheduler);
    }

    @Test
    void testInitWhenStatusTrue() {

        when(config.status()).thenReturn(true);
        when(config.cronExpression()).thenReturn("0 0 * * * ?");
        when(scheduler.EXPR("0 0 * * * ?")).thenReturn(scheduleOptions);

        schedulerClass.init(config);

        verify(scheduler).EXPR("0 0 * * * ?");
        verify(scheduler).schedule(schedulerClass, scheduleOptions);
    }

    @Test
    void testInitWhenStatusFalse() {

        when(config.status()).thenReturn(false);

        schedulerClass.init(config);

        verify(scheduler, never()).EXPR(anyString());
        verify(scheduler, never()).schedule(any(Runnable.class), any(ScheduleOptions.class));
    }

    @Test
    void testRun() {

        schedulerClass.run();

        verify(jobManager).addJob(
                eq("content/ScheduledContentExpiryClass"),
                any(Map.class));
    }
}