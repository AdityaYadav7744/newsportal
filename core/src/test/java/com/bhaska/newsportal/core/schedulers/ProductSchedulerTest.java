package com.bhaska.newsportal.core.schedulers;

import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.event.jobs.JobManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ProductSchedulerTest {

    @Mock
    Scheduler scheduler;

    @Mock
    JobManager jobManager;

    @Mock
    ScheduleOptions options;

    @Mock
    ProductSchedulerConfig config;

    @InjectMocks
    ProductScheduler productScheduler;

    // ✅ 1. Test activate method
    @Test
    void testActivate() {

        when(config.schedulerName()).thenReturn("testScheduler");
        when(config.apiUrl()).thenReturn("http://test.com");
        when(config.enable()).thenReturn(true);
        when(config.cronExpression()).thenReturn("0 * * * * ?");

        when(scheduler.EXPR(anyString())).thenReturn(options);

        productScheduler.activate(config);

        verify(scheduler).unschedule("testScheduler");
        verify(scheduler).schedule(eq(productScheduler), eq(options));
    }

    // ✅ 2. Test run method
    @Test
    void testRunMethod() {

        when(config.schedulerName()).thenReturn("testScheduler");
        when(config.apiUrl()).thenReturn("http://test.com");
        when(config.enable()).thenReturn(false); // skip scheduling

        productScheduler.activate(config);

        productScheduler.run();

        verify(jobManager).addJob(eq("newsportal/productScheduler"), anyMap());
    }
}