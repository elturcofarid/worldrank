package com.worldrank.app.user.repository;

import com.worldrank.app.user.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {}

