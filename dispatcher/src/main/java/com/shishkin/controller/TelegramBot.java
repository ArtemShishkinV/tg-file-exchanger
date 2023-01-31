package com.shishkin.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Controller
public class TelegramBot extends TelegramLongPollingBot {
    private static final Logger LOGGER = LogManager.getLogger(TelegramBot.class);
    private final UpdateController updateController;
    @Value("${bot.name}")
    private String username;
    @Value("${bot.token}")
    private String token;

    public TelegramBot(UpdateController updateController) {
        this.updateController = updateController;
    }


    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        sendMessage(updateController.processUpdate(update));
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    public void sendMessage(SendMessage sendMessage) {
        if (sendMessage != null) {
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                LOGGER.error(e);
            }
        }
    }
}
