package com.javaAi.fileUpload.service;

import cn.hutool.core.io.FileUtil;
import com.javaAi.fileUpload.dto.FileChunkDTO;
import com.javaAi.fileUpload.resp.FileUploadResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

@Service
public class FileUploadService {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);

    @Value("${file.upload.dir}")
    private String uploadDir;

    /**
     * 检查文件是否已上传过
     */
    public boolean checkFileExists(FileChunkDTO chunk) {
        String storeChunkPath = uploadDir + File.separator + "chunks" + File.separator + chunk.getIdentifier() + File.separator + chunk.getChunkNumber();
        File storeChunk = new File(storeChunkPath);
        return storeChunk.exists() && chunk.getChunkSize() == storeChunk.length();
    }

    /**
     * 上传文件块
     */
    public FileUploadResponse uploadChunk(FileChunkDTO chunk, MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return FileUploadResponse.error("文件块为空");
            }

            // 创建块文件目录
            String chunkDirPath = uploadDir + File.separator + "chunks" + File.separator + chunk.getIdentifier();
            File chunkDir = new File(chunkDirPath);
            if (!chunkDir.exists()) {
                chunkDir.mkdirs();
            }

            // 保存分块
            String chunkPath = chunkDirPath + File.separator + chunk.getChunkNumber();
            file.transferTo(new File(chunkPath));

            return FileUploadResponse.success("文件块上传成功");
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
            return FileUploadResponse.error("文件块上传失败: " + e.getMessage());
        }
    }

    /**
     * 合并文件块
     */
    public FileUploadResponse mergeChunks(String identifier, String filename, Integer totalChunks) {
        try {
            String chunkDirPath = uploadDir + File.separator + "chunks" + File.separator + identifier;
            if(!FileUtil.exist(chunkDirPath)){
                return FileUploadResponse.error("文件合并失败, 目录不存在" );
            }

            File chunkDir = new File(chunkDirPath);
            // 创建目标文件
            String filePath = uploadDir + File.separator + filename;
            File destFile = new File(filePath);
            if (destFile.exists()) {
                destFile.delete();
            }
            // 使用RandomAccessFile合并文件块
            try (RandomAccessFile randomAccessFile = new RandomAccessFile(destFile, "rw")) {
                byte[] buffer = new byte[1024];
                for (int i = 1; i <= totalChunks; i++) {
                    File chunk = new File(chunkDirPath + File.separator + i);
                    if (!chunk.exists()) {
                        return FileUploadResponse.error("文件块" + i + "不存在");
                    }

                    try (java.io.FileInputStream fis = new java.io.FileInputStream(chunk)) {
                        int len;
                        while ((len = fis.read(buffer)) != -1) {
                            randomAccessFile.write(buffer, 0, len);
                        }
                    }
                }
            }

            // 清理临时文件块
            FileUtil.del(chunkDir);

            return FileUploadResponse.success("文件合并成功", filePath);
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
            return FileUploadResponse.error("文件合并失败: " + e.getMessage());
        }
    }
}
