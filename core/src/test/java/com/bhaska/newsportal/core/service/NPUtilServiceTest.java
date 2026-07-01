package com.bhaska.newsportal.core.service;

import com.bhaska.newsportal.core.service.impl.NPUtilService;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NPUtilServiceTest {

    @Mock
    ResourceResolverFactory factory;

    @Mock
    ResourceResolver resolver;

    @InjectMocks
    NPUtilService service;

    // ✅ 1. Success case
    @Test
    void testGetResourceResolver_Success() throws Exception {

        Mockito.when(factory.getServiceResourceResolver(Mockito.anyMap()))
                .thenReturn(resolver);

        ResourceResolver result = service.getResourceResolver();

        assertNotNull(result);
        assertEquals(resolver, result);
    }

    // ✅ 2. Exception case
    @Test
    void testGetResourceResolver_Exception() throws Exception {

        Mockito.when(factory.getServiceResourceResolver(Mockito.anyMap()))
                .thenThrow(new LoginException("Login failed"));

        assertThrows(RuntimeException.class, () -> {
            service.getResourceResolver();
        });
    }
}