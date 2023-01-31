package com.shishkin.service.impl;

import com.shishkin.Properties;
import com.shishkin.controller.TelegramBot;
import com.shishkin.service.AnswerConsumer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class AnswerConsumerImpl implements AnswerConsumer {
    private final TelegramBot telegramBot;

    public AnswerConsumerImpl(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Override
    @RabbitListener(queues = Properties.RabbitQueue.ANSWER_MESSAGE)
    public void consume(SendMessage sendMessage) {
        telegramBot.sendMessage(sendMessage);
    }
}
