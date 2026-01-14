package com.worldrank.app.lugar.repository;

import java.io.ByteArrayInputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.worldrank.app.lugar.service.StorageService;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

@Service
public class MinioStorageService implements StorageService {

    private final MinioClient minioClient;

    @Value("${minio.endpoint:http://localhost:9000}")
    private String endpoint;

    public MinioStorageService(@Value("${minio.endpoint:http://localhost:9000}") String endpoint,
                               @Value("${minio.access-key:admin}") String accessKey,
                               @Value("${minio.secret-key:admin123}") String secretKey) {
        this.minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
        this.endpoint = endpoint;
    }

    @Override
    public String subirImagen(MultipartFile file, String bucket, UUID userId) {
        try {
            ensureBucketExists(bucket);
            String objectName = userId.toString() + "/" + UUID.randomUUID() + ".jpg";
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build()
            );
            return endpoint + "/" + bucket + "/" + objectName;
        } catch (Exception e) {
            throw new RuntimeException("Error uploading image to MinIO", e);
        }
    }

    @Override
    public String subirImagen(byte[] bytes, String bucket, UUID userId) {
        try {
            ensureBucketExists(bucket);
            String objectName = userId.toString() + "/" + UUID.randomUUID() + ".jpg";
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .stream(new ByteArrayInputStream(bytes), bytes.length, -1)
                    .contentType("image/jpeg")
                    .build()
            );
            return endpoint + "/" + bucket + "/" + objectName;
        } catch (Exception e) {
            throw new RuntimeException("Error uploading image to MinIO", e);
        }
    }

    private void ensureBucketExists(String bucket) {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error checking/creating bucket", e);
        }
    }
}
