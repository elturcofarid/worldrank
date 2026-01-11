package com.worldrank.app.lugar.repository;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.worldrank.app.lugar.service.StorageService;

@Service
public class LocalStorageService implements StorageService {

    @Override
    public String subirImagen(MultipartFile file) {
        // Simulación
        return "https://cdn.worldrank.com/" + UUID.randomUUID() + ".jpg";
    }
}
