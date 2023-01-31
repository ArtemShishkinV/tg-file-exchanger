package com.shishkin.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateProducer {
    void produce(String messageType, Update update);
}
