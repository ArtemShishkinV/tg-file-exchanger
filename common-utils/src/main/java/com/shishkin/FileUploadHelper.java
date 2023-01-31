package com.shishkin;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Log4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileUploadHelper {
    public static byte[] uploadFile(URL url, int fileSize) throws IOException {
        try (InputStream is = url.openStream()) {
            return readByteArrayFromFile(is, fileSize);
        }
    }

    public static byte[] readByteArrayFromFile(InputStream is, int fileSize) throws IOException {
        byte[] bytes;
        int offset = 0;
        bytes = new byte[fileSize];
        while (fileSize > 0) {
            int bytesRead = is.read(bytes, offset, fileSize);
            fileSize = fileSize - bytesRead;
            offset = offset + bytesRead;
            log.debug("#upload-file: " + offset + "/" + fileSize);
        }
        return bytes;
    }
}
