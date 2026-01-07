package com.worldrank.app.score.repository;

import com.worldrank.app.score.domain.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VisitRepository extends JpaRepository<Visit, UUID> {}

