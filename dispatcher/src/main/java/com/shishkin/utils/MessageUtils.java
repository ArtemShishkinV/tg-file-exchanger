package com.shishkin.utils;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class MessageUtils {
    private MessageUtils() {
        throw new UnsupportedOperationException();
    }
    public static SendMessage generateMessage(Update update, String text) {
        return new SendMessage(update.getMessage().getChatId().toString(), text);
    }
}
