package com.bhaska.newsportal.core.service;


public interface EmailService {

    void sendWelcomeEmail(String to, String userName);
}