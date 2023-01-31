package com.shishkin.service.enums;

import lombok.Getter;

import java.util.Arrays;

public enum TypeCommand {
    HELP("/help", """
            Список доступных команд:
            /cancel - отмена выполнения текущей команды;
            /registration - регистрация пользователя."""),
    REGISTRATION("/registration", "Команда временно недоступна!"),
    CANCEL("/cancel", "Команда отменена!"),
    START("/start", "Для того чтобы узнать список команд напишите /help!");

    private final String cmd;
    @Getter
    private final String message;

    TypeCommand(String cmd, String message) {
        this.cmd = cmd;
        this.message = message;
    }

    public static TypeCommand getInstance(String cmd) {
        return Arrays.stream(TypeCommand.values()).filter(c -> c.cmd.equals(cmd)).findFirst().orElse(null);
    }
}
