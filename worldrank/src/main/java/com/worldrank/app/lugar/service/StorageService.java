package com.worldrank.app.lugar.service;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String subirImagen(MultipartFile file, String bucket, UUID userId);
    String subirImagen(byte[] bytes, String bucket, UUID userId);
}