package com.shishkin.service.impl

import com.shishkin.CryptoTool
import com.shishkin.common.jpa.entity.BinaryContent
import com.shishkin.common.jpa.entity.DocumentEntity
import com.shishkin.common.jpa.entity.PhotoEntity
import com.shishkin.common.jpa.repository.DocumentRepository
import com.shishkin.common.jpa.repository.PhotoRepository
import com.shishkin.service.FileService
import org.apache.commons.io.FileUtils
import org.springframework.core.io.FileSystemResource
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.io.File
import java.io.IOException
import org.apache.log4j.Logger


@Service
class FileServiceImpl(
    private val documentRepository: DocumentRepository,
    private val photoRepository: PhotoRepository,
    private val cryptoTool: CryptoTool
) : FileService {
    private val logger = Logger.getLogger(FileServiceImpl::class.java)

    override fun getDocument(id: String): DocumentEntity? {
        val documentId = cryptoTool.idOf(id)
        return documentId?.let { documentRepository.findByIdOrNull(documentId) }
    }

    override fun getPhoto(id: String): PhotoEntity? {
        val photoId = cryptoTool.idOf(id)
        return photoId?.let { photoRepository.findByIdOrNull(photoId) }
    }

    override fun getFileSystemSource(binaryContent: BinaryContent): FileSystemResource? {
        return try {
            val temp = File.createTempFile("tempFile", ".bin")
            FileUtils.writeByteArrayToFile(temp, binaryContent.arrayOfBytes)
            FileSystemResource(temp)
        } catch (e: IOException) {
            logger.error(e)
            null
        }
    }
}