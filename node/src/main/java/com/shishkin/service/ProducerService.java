package com.shishkin.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface ProducerService {
    void produce(SendMessage sendMessage);
}
