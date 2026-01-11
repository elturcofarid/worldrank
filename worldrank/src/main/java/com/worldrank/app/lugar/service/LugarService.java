package com.worldrank.app.lugar.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.worldrank.app.lugar.domain.Lugar;
import com.worldrank.app.lugar.repository.LugarRepository;

@Service
public class LugarService {

    private final LugarRepository lugarRepository;

    public LugarService(LugarRepository lugarRepository) {
        this.lugarRepository = lugarRepository;
    }

    public Lugar obtenerPorId(UUID idLugar) {
        return lugarRepository.findById(idLugar)
                .orElseThrow(() -> new RuntimeException("Lugar no encontrado"));
    }
}
