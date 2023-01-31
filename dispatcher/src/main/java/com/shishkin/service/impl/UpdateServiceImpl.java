package com.shishkin.service.impl;

import com.shishkin.Properties;
import com.shishkin.service.UpdateProducer;
import com.shishkin.service.UpdateService;
import com.shishkin.utils.MessageUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class UpdateServiceImpl implements UpdateService {
    private final UpdateProducer updateProducer;

    public UpdateServiceImpl(UpdateProducer updateProducer) {
        this.updateProducer = updateProducer;
    }

    @Override
    public SendMessage distributeMessageByType(Update update) {
        var message = update.getMessage();
        if (message.hasPhoto()) {
            return processPhotoMessage(update);
        }
        if (message.hasDocument()) {
            return processDocumentMessage(update);
        }
        if (message.hasText()) {
            return processTextMessage(update);
        }
        return processUnsupportedTypeMessage(update);
    }

    private SendMessage processUnsupportedTypeMessage(Update update) {
        return MessageUtils.generateMessage(update,
                "Unsupported message type!");
    }

    private SendMessage processTextMessage(Update update) {
        updateProducer.produce(Properties.RabbitQueue.TEXT_MESSAGE, update);
        return getViewTextAccepted(update);
    }

    private SendMessage processDocumentMessage(Update update) {
        updateProducer.produce(Properties.RabbitQueue.DOC_MESSAGE, update);
        return getViewFileIsReceived(update);
    }

    private SendMessage processPhotoMessage(Update update) {
        updateProducer.produce(Properties.RabbitQueue.PHOTO_MESSAGE, update);
        return getViewFileIsReceived(update);
    }

    private SendMessage getViewFileIsReceived(Update update) {
        return MessageUtils.generateMessage(update, "File rendering, wait...");
    }

    private SendMessage getViewTextAccepted(Update update) {
        return MessageUtils.generateMessage(update, "Text message was accepted!");
    }
}
