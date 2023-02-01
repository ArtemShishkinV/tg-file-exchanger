package com.shishkin.service.impl;

import com.shishkin.crypto.CryptoTool;
import com.shishkin.common.jpa.entity.UserEntity;
import com.shishkin.common.jpa.entity.enums.UserStatus;
import com.shishkin.common.jpa.service.UserService;
import com.shishkin.entity.RawData;
import com.shishkin.exception.UploadFileException;
import com.shishkin.model.EmailMessage;
import com.shishkin.repository.RawDataRepo;
import com.shishkin.service.FileService;
import com.shishkin.service.MainService;
import com.shishkin.service.ProducerService;
import com.shishkin.service.enums.TypeCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
@RequiredArgsConstructor
@Log4j
public class MainServiceImpl implements MainService {
    private static final String GET_DOCUMENT_LINK_FORMAT = "Документ успешно загружен! " +
            "Ссылка для скачивания:%n http://%s/file/get-document?id=%s";
    private static final String GET_PHOTO_LINK_FORMAT = "Фото успешно загружено! " +
            "Ссылка для скачивания:%n http://%s/file/get-photo?id=%s";

    private final RawDataRepo rawDataRepo;
    private final ProducerService producerService;
    private final UserService userService;
    private final FileService fileService;
    private final CryptoTool cryptoTool;
    @Value("${upload.domain.name}")
    private String uploadDomain;
    @Value("${service.activation.uri}")
    private String emailConfirmLink;

    @Override
    public void processTextMessage(Update update) {
        var message = update.getMessage();
        var user = findOrSaveUser(message.getFrom());
        log.debug(user);
        rawDataRepo.save(getRawData(update));

        var reply = processMessage(user, message.getText());
        sendAnswer(message.getChatId(), reply);
    }

    @Override
    public void processDocMessage(Update update) {
        var message = update.getMessage();
        rawDataRepo.save(getRawData(update));
        var user = findOrSaveUser(message.getFrom());
        log.debug("#process-doc-message: " + user + "\n" + message);
        if (isAllowToSendContent(user, message.getChatId())) {
            try {
                var document = fileService.processDoc(message);
                sendUploadLink(document.getId(), message, GET_DOCUMENT_LINK_FORMAT);
            } catch (UploadFileException e) {
                log.error(e);
                var error = "К сожалению, загрузка документа не удалась. Повторите попытку позже.";
                sendAnswer(message.getChatId(), error);
            }

        }
    }

    @Override
    public void processPhotoMessage(Update update) {
        var message = update.getMessage();
        rawDataRepo.save(getRawData(update));
        var user = findOrSaveUser(message.getFrom());
        log.debug("#process-doc-message: " + user + "\n" + message);
        if (isAllowToSendContent(user, message.getChatId())) {
            try {
                var photo = fileService.processPhoto(message);
                sendUploadLink(photo.getId(), message, GET_PHOTO_LINK_FORMAT);
            } catch (UploadFileException e) {
                log.error(e);
                var error = "К сожалению, загрузка файла не удалась. Повторите попытку позже.";
                sendAnswer(message.getChatId(), error);
            }
        }
    }

    private void sendUploadLink(Long id, Message message, String formatLink) {
        var hashId = cryptoTool.hashOf(id);
        var answer = String.format(formatLink, uploadDomain, hashId);
        sendAnswer(message.getChatId(), answer);
    }

    private boolean isAllowToSendContent(UserEntity user, Long chatId) {
        var error = getErrorMessageToIsNotAllowSend(user);
        log.debug(error);
        if (error.isEmpty()) {
            return true;
        }
        sendAnswer(chatId, error);
        return false;
    }

    private String getErrorMessageToIsNotAllowSend(UserEntity user) {
        if (!user.isActive()) {
            return "Сначала выполните регистрацию или закончите активацию учетной записи.";
        } else if (!UserStatus.REGISTERED.equals(user.getStatus())) {
            return "Отмените текущую команду с помощью /cancel для отправки файлов.";
        }

        return "";
    }

    private void sendAnswer(Long chatId, String message) {
        var sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .build();
        producerService.produce(sendMessage);
    }

    private String processMessage(UserEntity user, String cmd) {
        var command = TypeCommand.getInstance(cmd);

        return switch (user.getStatus()) {
            case REGISTERED -> processCommand(command);
            case WAIT_EMAIL_CONFIRM -> "На вашу электронную почту отправлено письмо для подтверждения регистрации\n" +
                    "Перейдите по ссылке в письме и возвращайтесь в бота!";
        };
    }



    private String processCommand(TypeCommand command) {
        if (command != null) {
            return command.getMessage();
        }
        return "Неизвестная команда, напишите /help!";
    }

    private UserEntity findOrSaveUser(User user) {
        UserEntity userEntity = userService.findByTelegramId(user.getId());
        if (userEntity == null) {
            userEntity = UserEntity.builder()
                    .telegramId(user.getId())
                    .username(user.getUserName())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email("ArtemShishkinV@yandex.ru")
                    .status(UserStatus.WAIT_EMAIL_CONFIRM)
                    .isActive(true)
                    .build();
            userEntity = userService.save(userEntity);
            sendEmailConfirm(userEntity);
        }

        return userEntity;
    }

    private void sendEmailConfirm(UserEntity user) {
        EmailMessage message = new EmailMessage(
                user.getEmail(),
                "Активация учетной записи",
                getEmailConfirmLinkByUser(user));

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        restTemplate.exchange("http://localhost:8087/send",
                HttpMethod.POST,
                new HttpEntity<>(message, headers),
                String.class);
    }

    private String getEmailConfirmLinkByUser(UserEntity user) {
        return emailConfirmLink.replace("{id}", cryptoTool.hashOf(user.getId()));
    }

    private RawData getRawData(Update update) {
        return RawData.builder()
                .event(update)
                .build();
    }
}
