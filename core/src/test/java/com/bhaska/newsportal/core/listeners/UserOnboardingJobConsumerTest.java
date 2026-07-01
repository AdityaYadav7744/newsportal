package com.bhaska.newsportal.core.listeners;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import com.bhaska.newsportal.core.service.EmailService;
import com.bhaska.newsportal.core.service.impl.NPUtilService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer.JobResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserOnboardingJobConsumerTest {

    private UserOnboardingJobConsumer consumer;

    private EmailService emailService;
    private NPUtilService utilService;

    @BeforeEach
    void setUp() throws Exception {

        consumer = new UserOnboardingJobConsumer();

        emailService = mock(EmailService.class);
        utilService = mock(NPUtilService.class);

        Field f1 = UserOnboardingJobConsumer.class.getDeclaredField("emailService");
        f1.setAccessible(true);
        f1.set(consumer, emailService);

        Field f2 = UserOnboardingJobConsumer.class.getDeclaredField("npUtilService");
        f2.setAccessible(true);
        f2.set(consumer, utilService);
    }

    @Test
    void testNullUserPath() {

        Job job = mock(Job.class);

        when(job.getProperty("userPath", String.class)).thenReturn(null);

        assertEquals(JobResult.CANCEL, consumer.process(job));
    }

    @Test
    void testProfilePath() {

        Job job = mock(Job.class);

        when(job.getProperty("userPath", String.class))
                .thenReturn("/home/users/test/profile");

        assertEquals(JobResult.CANCEL, consumer.process(job));
    }

    @Test
    void testUserResourceNotFound() {

        Job job = mock(Job.class);
        ResourceResolver resolver = mock(ResourceResolver.class);

        when(job.getProperty("userPath", String.class))
                .thenReturn("/home/users/test");

        when(utilService.getResourceResolver()).thenReturn(resolver);
        when(resolver.getResource(anyString())).thenReturn(null);

        assertEquals(JobResult.CANCEL, consumer.process(job));
    }

    @Test
    void testProfileMissing() {

        Job job = mock(Job.class);
        ResourceResolver resolver = mock(ResourceResolver.class);
        Resource user = mock(Resource.class);

        when(job.getProperty("userPath", String.class))
                .thenReturn("/home/users/test");

        when(utilService.getResourceResolver()).thenReturn(resolver);
        when(resolver.getResource(anyString())).thenReturn(user);
        when(user.getChild("profile")).thenReturn(null);

        assertEquals(JobResult.CANCEL, consumer.process(job));
    }

    @Test
    void testException() {

        Job job = mock(Job.class);

        when(job.getProperty("userPath", String.class))
                .thenReturn("/home/users/test");

        when(utilService.getResourceResolver())
                .thenThrow(new RuntimeException());

        assertEquals(JobResult.FAILED, consumer.process(job));
    }
}