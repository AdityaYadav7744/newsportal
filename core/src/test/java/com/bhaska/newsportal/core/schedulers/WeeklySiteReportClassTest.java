package com.bhaska.newsportal.core.schedulers;

import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.event.jobs.JobManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class WeeklySiteReportClassTest {

    @Mock
    Scheduler scheduler;

    @Mock
    JobManager jobManager;

    @Mock
    ScheduleOptions options;

    @Mock
    WeeklySiteReportConfig config;

    @InjectMocks
    WeeklySiteReportClass schedulerClass;

    // ✅ 1. Test init method (enabled)
    @Test
    void testInit_WhenEnabled() {

        when(config.state()).thenReturn(true);
        when(config.cronExpression()).thenReturn("0 0 12 * * ?");
        when(scheduler.EXPR(anyString())).thenReturn(options);

        schedulerClass.init(config);

        verify(scheduler).schedule(eq(schedulerClass), eq(options));
    }

    // ✅ 2. Test run method
    @Test
    void testRunMethod() {

        schedulerClass.run();

        verify(jobManager).addJob(eq("content/api/job"), anyMap());
    }
}