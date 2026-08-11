package com.training.mq.storage;

import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 对象存储服务（MinIO / S3 兼容）
 */
@Service
@Slf4j
public class FileStorageService {

    private MinioClient minioClient;

    @Value("${storage.endpoint:http://localhost:9000}")
    private String endpoint;

    @Value("${storage.access-key:minioadmin}")
    private String accessKey;

    @Value("${storage.secret-key:minioadmin}")
    private String secretKey;

    @Value("${storage.bucket:training-files}")
    private String bucket;

    @PostConstruct
    public void init() {
        this.minioClient = MinioClient.builder()
            .endpoint(endpoint)
            .credentials(accessKey, secretKey)
            .build();
        ensureBucket();
        log.info("对象存储初始化完成: endpoint={}, bucket={}", endpoint, bucket);
    }

    private void ensureBucket() {
        try {
            boolean exists = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(bucket).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
                log.info("创建存储桶: {}", bucket);
            }
        } catch (Exception e) {
            throw new RuntimeException("初始化存储桶失败", e);
        }
    }

    /**
     * 上传文件
     * @return objectName（存储路径）
     */
    public String upload(MultipartFile file, String directory) {
        String objectName = directory + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
        try (InputStream is = file.getInputStream()) {
            minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucket)
                .object(objectName)
                .stream(is, file.getSize(), -1)
                .contentType(file.getContentType())
                .build());
            log.info("文件上传成功: {}", objectName);
            return objectName;
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        }
    }

    /**
     * 下载文件
     */
    public InputStream download(String objectName) {
        try {
            return minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucket)
                .object(objectName)
                .build());
        } catch (Exception e) {
            throw new RuntimeException("文件下载失败", e);
        }
    }

    /**
     * 获取预签名 URL（临时访问链接，有效期 1 小时）
     */
    public String getPresignedUrl(String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucket)
                    .object(objectName)
                    .expiry(1, TimeUnit.HOURS)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("获取访问链接失败", e);
        }
    }

    /**
     * 删除文件
     */
    public void delete(String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucket)
                .object(objectName)
                .build());
            log.info("文件删除成功: {}", objectName);
        } catch (Exception e) {
            throw new RuntimeException("文件删除失败", e);
        }
    }
}
