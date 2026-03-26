package com.bhaska.newsportal.core.service;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ApiServiceImplTest {

    AemContext context = new AemContext();

    @Mock
    ApiConfig config;

    ApiServiceImpl service;

    @BeforeEach
    void setUp() {
        service = Mockito.spy(new ApiServiceImpl());
    }

    /**
     * ✅ Branch 1: enableApi = true → fetchApiData() should be called
     */
    @Test
    void testActivate_WithApiEnabled() {

        Mockito.when(config.apiUrl()).thenReturn("https://example.com");
        Mockito.when(config.enableApi()).thenReturn(true);

        // Spy → verify method call
        service.activate(config);

        Mockito.verify(service, Mockito.times(1)).fetchApiData();
    }

    /**
     * ✅ Branch 2: enableApi = false → fetchApiData() should NOT be called
     */
    @Test
    void testActivate_WithApiDisabled() {

        Mockito.when(config.apiUrl()).thenReturn("https://example.com");
        Mockito.when(config.enableApi()).thenReturn(false);

        service.activate(config);

        Mockito.verify(service, Mockito.never()).fetchApiData();
    }

    /**
     * ✅ Branch 3: Normal execution (no exception)
     */
    @Test
    void testFetchApiData_Success() {

        // Give a valid URL
        Mockito.when(config.apiUrl()).thenReturn("https://gorest.co.in/public/v2/users");
        Mockito.when(config.enableApi()).thenReturn(false);

        service.activate(config);

        assertDoesNotThrow(() -> service.fetchApiData());
    }

    /**
     * ✅ Branch 4: Exception scenario
     */
    @Test
    void testFetchApiData_Exception() {

        // Invalid URL to force exception
        Mockito.when(config.apiUrl()).thenReturn("invalid-url");
        Mockito.when(config.enableApi()).thenReturn(false);

        service.activate(config);

        assertDoesNotThrow(() -> service.fetchApiData());
    }
}