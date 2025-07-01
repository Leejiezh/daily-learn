package com.dailylearn.fileUpload.controller;

import com.dailylearn.fileUpload.dto.FileChunkDTO;
import com.dailylearn.fileUpload.resp.FileUploadResponse;
import com.dailylearn.fileUpload.service.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin // 允许跨域请求
public class FileUploadController {

    private static Logger logger = LoggerFactory.getLogger(FileUploadService.class);

    @Autowired
    private FileUploadService fileUploadService;

    /**
     * 检查文件或文件块是否已存在
     */
    @GetMapping("/check")
    public ResponseEntity<Void> checkFileExists(FileChunkDTO chunk) {
        boolean exists = fileUploadService.checkFileExists(chunk);
        if (exists) {
            // 分片存在，返回 200
            return ResponseEntity.ok().build();
        } else {
            // 分片不存在，返回 404
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 上传文件块
     */
    @PostMapping("/chunk")
    public FileUploadResponse uploadChunk(
            @RequestParam(value = "chunkNumber") Integer chunkNumber,
            @RequestParam(value = "chunkSize") Long chunkSize,
            @RequestParam(value = "currentChunkSize") Long currentChunkSize,
            @RequestParam(value = "totalSize") Long totalSize,
            @RequestParam(value = "identifier") String identifier,
            @RequestParam(value = "filename") String filename,
            @RequestParam(value = "totalChunks") Integer totalChunks,
            @RequestParam("file") MultipartFile file) {

        String identifierName = identifier;
        // 如果 identifierName 包含逗号，取第一个值
        if (identifierName.contains(",")) {
            identifierName = identifierName.split(",")[0];
        }

        FileChunkDTO chunk = new FileChunkDTO();
        chunk.setChunkNumber(chunkNumber);
        chunk.setChunkSize(chunkSize);
        chunk.setCurrentChunkSize(currentChunkSize);
        chunk.setTotalSize(totalSize);
        chunk.setIdentifier(identifierName);
        chunk.setFilename(filename);
        chunk.setTotalChunks(totalChunks);

        return fileUploadService.uploadChunk(chunk, file);
    }

    /**
     * 合并文件块
     */
    @PostMapping("/merge")
    public FileUploadResponse mergeChunks(
            @RequestParam("identifier") String identifier,
            @RequestParam("filename") String filename,
            @RequestParam("totalChunks") Integer totalChunks) {
        return fileUploadService.mergeChunks(identifier, filename, totalChunks);
    }
} 