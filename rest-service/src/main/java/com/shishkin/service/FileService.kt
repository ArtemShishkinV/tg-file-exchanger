package com.shishkin.service

import com.shishkin.common.jpa.entity.BinaryContent
import com.shishkin.common.jpa.entity.DocumentEntity
import com.shishkin.common.jpa.entity.PhotoEntity
import org.springframework.core.io.FileSystemResource

interface FileService {
    fun getDocument(id: String): DocumentEntity?
    fun getPhoto(id: String): PhotoEntity?
    fun getFileSystemSource(binaryContent: BinaryContent): FileSystemResource?
}