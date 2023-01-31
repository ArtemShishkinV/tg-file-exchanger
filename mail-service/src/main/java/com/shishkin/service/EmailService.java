package com.shishkin.service;

import com.shishkin.model.EmailMessage;

public interface EmailService {
    void send(EmailMessage message);
}
