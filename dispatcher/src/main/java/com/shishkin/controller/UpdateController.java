package com.shishkin.controller;


import com.shishkin.service.UpdateService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Controller
public class UpdateController {
    private static final Logger LOGGER = LogManager.getLogger(UpdateController.class);
    private final UpdateService updateService;

    public UpdateController(UpdateService updateService) {
        this.updateService = updateService;
    }

    public SendMessage processUpdate(Update update) {
        if (update == null) {
            LOGGER.error("Received update is null");
            return null;
        }

        if (update.hasMessage()) {
            return updateService.distributeMessageByType(update);
        } else {
            LOGGER.error("Unsupported message type is received: " + update);
        }
        return null;
    }

}
