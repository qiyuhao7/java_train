package com.training.mq.controller;

import com.training.mq.storage.FileStorageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

/**
 * 文件管理接口
 */
@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileStorageService storageService;

    public FileController(FileStorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * 上传文件
     * POST /api/files/upload
     */
    @PostMapping("/upload")
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file) {
        String objectName = storageService.upload(file, "uploads");
        String url = storageService.getPresignedUrl(objectName);
        return Map.of("objectName", objectName, "url", url);
    }

    /**
     * 下载文件
     * GET /api/files/download?objectName=...
     */
    @GetMapping("/download")
    public void download(@RequestParam String objectName, HttpServletResponse response) {
        try (InputStream is = storageService.download(objectName)) {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" +
                URLEncoder.encode(objectName, StandardCharsets.UTF_8));
            is.transferTo(response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("下载失败", e);
        }
    }

    /**
     * 获取临时访问链接
     * GET /api/files/url?objectName=...
     */
    @GetMapping("/url")
    public Map<String, Object> url(@RequestParam String objectName) {
        return Map.of("url", storageService.getPresignedUrl(objectName));
    }

    /**
     * 删除文件
     * DELETE /api/files?objectName=...
     */
    @DeleteMapping
    public Map<String, Object> delete(@RequestParam String objectName) {
        storageService.delete(objectName);
        return Map.of("deleted", objectName);
    }
}
