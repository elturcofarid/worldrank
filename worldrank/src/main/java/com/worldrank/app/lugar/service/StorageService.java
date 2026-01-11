package com.worldrank.app.lugar.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String subirImagen(MultipartFile file);
}