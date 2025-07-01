package com.dailylearn.fileUpload.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/download")
@CrossOrigin // 允许跨域请求
public class FileDownloadController {

    private static final Logger logger = LoggerFactory.getLogger(FileDownloadController.class);

    @Value("${file.upload.dir}")
    private String uploadDir;

    /**
     * 获取文件列表
     */
    @GetMapping("/files")
    public List<String> getFileList() {
        List<String> fileList = new ArrayList<>();
        try {
            File uploadDirectory = new File(uploadDir);
            if (uploadDirectory.exists() && uploadDirectory.isDirectory()) {
                File[] files = uploadDirectory.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isFile() && !file.getName().startsWith(".")) {
                            fileList.add(file.getName());
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("获取文件列表失败", e);
        }
        return fileList;
    }

    /**
     * 下载文件
     */
    @GetMapping("/file/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            File file = new File(uploadDir + File.separator + filename);
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new FileSystemResource(file);
            // 设置文件名编码，支持中文
            String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8.toString())
                    .replaceAll("\\+", "%20");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                            "attachment; filename*=UTF-8''" + encodedFilename)
                    .body(resource);
        } catch (Exception e) {
            logger.error("文件下载失败: " + filename, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/file/{filename}")
    public ResponseEntity<String> deleteFile(@PathVariable String filename) {
        try {
            File file = new File(uploadDir + File.separator + filename);
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }
            if (file.delete()) {
                return ResponseEntity.ok("文件删除成功");
            } else {
                return ResponseEntity.internalServerError().body("文件删除失败");
            }
        } catch (Exception e) {
            logger.error("文件删除失败: " + filename, e);
            return ResponseEntity.internalServerError().body("文件删除失败: " + e.getMessage());
        }
    }
} 