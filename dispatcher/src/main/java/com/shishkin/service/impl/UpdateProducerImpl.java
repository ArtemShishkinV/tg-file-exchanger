package com.shishkin.service.impl;

import com.shishkin.controller.TelegramBot;
import com.shishkin.service.UpdateProducer;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class UpdateProducerImpl implements UpdateProducer {
    private static final Logger LOGGER = LogManager.getLogger(TelegramBot.class);

    private final RabbitTemplate rabbitTemplate;

    public UpdateProducerImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void produce(String messageType, Update update) {
        LOGGER.debug(messageType + ": " + update.getMessage().getText());
        rabbitTemplate.convertAndSend(messageType, update);
    }
}
