package com.shishkin.service.impl;

import com.shishkin.file.FileUploadHelper;
import com.shishkin.common.jpa.entity.BinaryContent;
import com.shishkin.common.jpa.entity.DocumentEntity;
import com.shishkin.common.jpa.entity.PhotoEntity;
import com.shishkin.common.jpa.repository.BinaryContentRepository;
import com.shishkin.common.jpa.repository.DocumentRepository;
import com.shishkin.common.jpa.repository.PhotoRepository;
import com.shishkin.exception.UploadFileException;
import com.shishkin.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

@Log4j
@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {
    @Value("${bot.token}")
    private String token;
    @Value("${service.file_info.uri}")
    private String fileInfoUri;
    @Value("${service.file_storage.uri}")
    private String fileStorageUri;

    private final BinaryContentRepository binaryContentRepository;
    private final DocumentRepository documentRepository;
    private final PhotoRepository photoRepository;

    @Override
    public DocumentEntity processDoc(Message message) {
        var fileId = message.getDocument().getFileId();
        ResponseEntity<String> response = getFilePath(fileId);

        if (HttpStatus.OK.equals(response.getStatusCode())) {
            return saveDocument(response, message.getDocument());
        }

        throw new UploadFileException("Bad response from telegram service: " + response);
    }

    @Override
    public PhotoEntity processPhoto(Message message) throws UploadFileException {
        var maxPhotoSizeIndex = message.getPhoto().size() - 1;
        var file = message.getPhoto().get(maxPhotoSizeIndex);
        ResponseEntity<String> response = getFilePath(file.getFileId());

        if (HttpStatus.OK.equals(response.getStatusCode())) return savePhoto(response, file);

        throw new UploadFileException("Bad response from telegram service: " + response);
    }

    private PhotoEntity savePhoto(ResponseEntity<String> response, PhotoSize photoSize) {
        var filePath = getFilePath(response);
        var persistentBinaryContent = saveAndGetPersistentBinaryContent(filePath, photoSize.getFileSize());
        var photoEntity = buildPhotoEntity(photoSize, persistentBinaryContent);
        return photoRepository.save(photoEntity);
    }

    private DocumentEntity saveDocument(ResponseEntity<String> response, Document document) {
        var filePath = getFilePath(response);

        var persistentBinaryContent = saveAndGetPersistentBinaryContent(filePath,
                document.getFileSize().intValue());
        var documentEntity = buildDocumentEntity(document, persistentBinaryContent);
        return documentRepository.save(documentEntity);
    }

    private String getFilePath(ResponseEntity<String> response) {
        return new JSONObject(Objects.requireNonNull(response.getBody()))
                .getJSONObject("result").getString("file_path");
    }

    private BinaryContent saveAndGetPersistentBinaryContent(String filePath, int fileSize) {
        var transientBinaryContent = BinaryContent.builder()
                .arrayOfBytes(uploadFile(filePath, fileSize))
                .build();
        return binaryContentRepository.save(transientBinaryContent);
    }

    private DocumentEntity buildDocumentEntity(Document document, BinaryContent binaryContent) {
        return DocumentEntity.builder()
                .telegramFileId(document.getFileId())
                .name(document.getFileName())
                .mimeType(document.getMimeType())
                .size(document.getFileSize())
                .binaryContent(binaryContent)
                .build();
    }

    private PhotoEntity buildPhotoEntity(PhotoSize photoSize, BinaryContent binaryContent) {
        return PhotoEntity.builder()
                .telegramFileId(photoSize.getFileId())
                .binaryContent(binaryContent)
                .fileSize(photoSize.getFileSize())
                .build();
    }

    private ResponseEntity<String> getFilePath(String fileId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(httpHeaders);

        return restTemplate.exchange(
                fileInfoUri,
                HttpMethod.GET,
                request,
                String.class,
                token, fileId
        );
    }

    private byte[] uploadFile(String filePath, int fileSize) {
        var uri = fileStorageUri.replace("{bot.token}", token).replace("{filePath}", filePath);
        URL urlObj = null;
        try {
            urlObj = new URL(uri);
            return FileUploadHelper.uploadFile(urlObj, fileSize);
        } catch (MalformedURLException e) {
            throw new UploadFileException(e);
        } catch (IOException e) {
            throw new UploadFileException(urlObj.toExternalForm(), e);
        }

    }
}
