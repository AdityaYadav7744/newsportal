package com.bhaska.newsportal.core.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import com.bhaska.newsportal.core.service.impl.EmailServiceImpl;
import org.apache.commons.mail.HtmlEmail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;

class EmailServiceImplTest {

    private EmailServiceImpl service;

    private MessageGatewayService messageGatewayService;
    private MessageGateway<HtmlEmail> gateway;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() throws Exception {

        service = new EmailServiceImpl();

        messageGatewayService = mock(MessageGatewayService.class);
        gateway = mock(MessageGateway.class);

        Field field = EmailServiceImpl.class
                .getDeclaredField("messageGatewayService");
        field.setAccessible(true);
        field.set(service, messageGatewayService);
    }

    @Test
    void testSendWelcomeEmail_Success() {

        when(messageGatewayService.getGateway(HtmlEmail.class))
                .thenReturn(gateway);

        service.sendWelcomeEmail("test@gmail.com", "Aditya");

        ArgumentCaptor<HtmlEmail> captor =
                ArgumentCaptor.forClass(HtmlEmail.class);

        verify(gateway).send(captor.capture());

        HtmlEmail email = captor.getValue();

        assertEquals("Welcome to News Portal", email.getSubject());
    }

    @Test
    void testSendWelcomeEmail_GatewayNull() {

        when(messageGatewayService.getGateway(HtmlEmail.class))
                .thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                service.sendWelcomeEmail("abc@gmail.com", "Aditya"));

        assertEquals("Mail gateway not available", ex.getMessage());
    }

}