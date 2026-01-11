package com.worldrank.app.lugar.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.worldrank.app.lugar.domain.Lugar;

public interface LugarRepository
        extends JpaRepository<Lugar, UUID> {
}