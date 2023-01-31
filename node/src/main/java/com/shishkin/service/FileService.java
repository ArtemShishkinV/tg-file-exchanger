package com.shishkin.service;

import com.shishkin.common.jpa.entity.DocumentEntity;
import com.shishkin.common.jpa.entity.PhotoEntity;
import com.shishkin.exception.UploadFileException;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {
    DocumentEntity processDoc(Message message) throws UploadFileException;
    PhotoEntity processPhoto(Message message) throws UploadFileException;
}
