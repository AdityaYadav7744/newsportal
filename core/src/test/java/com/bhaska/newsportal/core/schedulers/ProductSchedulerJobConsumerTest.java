package com.bhaska.newsportal.core.schedulers;

import com.bhaska.newsportal.core.service.NPUtilService;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ProductSchedulerJobConsumerTest {

    @Mock
    NPUtilService npUtilService;

    @Mock
    Job job;

    @InjectMocks
    ProductSchedulerJobConsumer consumer;

    // ✅ 1. Test process method
    @Test
    void testProcessMethod() {

        when(job.getProperty("apiUrl", String.class))
                .thenReturn("http://test.com");

        ProductSchedulerJobConsumer spyConsumer = Mockito.spy(consumer);

        // avoid calling real API
        doNothing().when(spyConsumer).getResponse(anyString());

        JobConsumer.JobResult result = spyConsumer.process(job);

        assertEquals(JobConsumer.JobResult.OK, result);
        verify(spyConsumer).getResponse("http://test.com");
    }

    // ✅ 2. Test getResponse method (no exception check)
    @Test
    void testGetResponse() {

        // we are not mocking full AEM + HTTP here (keep simple)
        // just check method runs without crashing

        assertDoesNotThrow(() -> {
            consumer.getResponse("https://jsonplaceholder.typicode.com/posts");
        });
    }
}