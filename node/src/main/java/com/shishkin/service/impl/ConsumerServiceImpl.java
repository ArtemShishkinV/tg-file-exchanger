package com.shishkin.service.impl;

import com.shishkin.Properties;
import com.shishkin.service.ConsumerService;
import com.shishkin.service.MainService;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@AllArgsConstructor
public class ConsumerServiceImpl implements ConsumerService {
    private static final Logger LOGGER = Logger.getLogger(ConsumerServiceImpl.class);

    private final MainService mainService;

    @Override
    @RabbitListener(queues = Properties.RabbitQueue.TEXT_MESSAGE)
    public void consumeTextMessage(Update update) {
        LOGGER.debug("#node-consume-text: " + update.getMessage());
        mainService.processTextMessage(update);
    }

    @Override
    @RabbitListener(queues = Properties.RabbitQueue.DOC_MESSAGE)
    public void consumeDocMessage(Update update) {
        LOGGER.debug("#node-consume-doc: " + update.getMessage());
        mainService.processDocMessage(update);
    }

    @Override
    @RabbitListener(queues = Properties.RabbitQueue.PHOTO_MESSAGE)
    public void consumePhotoMessage(Update update) {
        LOGGER.debug("#node-consume-photo: " + update.getMessage());
        mainService.processPhotoMessage(update);
    }
}
