package com.worldrank.app.post.repository;

import com.worldrank.app.post.domain.Post;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
