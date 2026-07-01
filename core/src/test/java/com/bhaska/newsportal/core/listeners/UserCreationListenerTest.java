package com.bhaska.newsportal.core.listeners;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;

import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.event.jobs.JobManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserCreationListenerTest {

    private UserCreationListener listener;

    private JobManager jobManager;

    @BeforeEach
    void setUp() throws Exception {
        listener = new UserCreationListener();
        jobManager = mock(JobManager.class);
        Field field = UserCreationListener.class.getDeclaredField("jobManager");
        field.setAccessible(true);
        field.set(listener, jobManager);
    }

    @Test
    void testOnChangeWithSingleResourceChange() {
        ResourceChange change = mock(ResourceChange.class);
        when(change.getPath()).thenReturn("/home/users/testuser");
        listener.onChange(Collections.singletonList(change));
        verify(jobManager, times(1))
                .addJob(eq("user/onboarding/job"), anyMap());
    }

    @Test
    void testOnChangeWithMultipleResourceChanges() {

        ResourceChange change1 = mock(ResourceChange.class);
        ResourceChange change2 = mock(ResourceChange.class);

        when(change1.getPath()).thenReturn("/home/users/user1");
        when(change2.getPath()).thenReturn("/home/users/user2");

        listener.onChange(Arrays.asList(change1, change2));

        verify(jobManager, times(2))
                .addJob(eq("user/onboarding/job"), anyMap());
    }

    @Test
    void testOnChangeWithEmptyList() {

        listener.onChange(Collections.emptyList());

        verify(jobManager, times(0))
                .addJob(eq("user/onboarding/job"), anyMap());
    }

    @Test
    void testListenerObjectCreation() {
        assertNotNull(listener);
    }
}