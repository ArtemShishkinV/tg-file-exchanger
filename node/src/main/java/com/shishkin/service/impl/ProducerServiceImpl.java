package com.shishkin.service.impl;

import com.shishkin.Properties;
import com.shishkin.service.ProducerService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class ProducerServiceImpl implements ProducerService {
    private final RabbitTemplate rabbitTemplate;

    public ProducerServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void produce(SendMessage sendMessage) {
        rabbitTemplate.convertAndSend(Properties.RabbitQueue.ANSWER_MESSAGE, sendMessage);
    }
}
