package com.shishkin.controller

import com.shishkin.FileUploadHelper
import com.shishkin.common.jpa.entity.BinaryContent
import com.shishkin.service.FileService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/file")
@RestController
class FileController(
    private val fileService: FileService
) {
    @GetMapping("/get-document")
    fun getDocument(
        @RequestParam("id") id: String
    ): ResponseEntity<out Any>? {
        val document = fileService.getDocument(id)

        document ?: return ResponseEntity.badRequest().build()

        val content = getContentToResponseBody(document.binaryContent, document.size.toInt())
        content ?: return ResponseEntity.internalServerError().build()

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(document.mimeType))
            .header("Content-disposition", "attachment; filename=" + document.name)
            .body(content)
    }

    @GetMapping("/get-photo")
    fun getPhoto(
        @RequestParam("id") id: String
    ): ResponseEntity<out Any>? {
        val photo = fileService.getPhoto(id)

        photo ?: return ResponseEntity.badRequest().build()

        val content = getContentToResponseBody(photo.binaryContent, photo.fileSize)
        content ?: return ResponseEntity.internalServerError().build()

        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG)
            .header("Content-disposition", "attachment;")
            .body(content)
    }

    private fun getContentToResponseBody(binaryContent: BinaryContent, fileSize: Int): ByteArray? {
        val fileSystemResource = fileService.getFileSystemSource(binaryContent)

        fileSystemResource ?: return null
        
        val content = FileUploadHelper.readByteArrayFromFile(fileSystemResource.inputStream, fileSize)

        fileSystemResource.file.delete()

        return content
    }
}