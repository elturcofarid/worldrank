package com.worldrank.app.post.service;

import com.worldrank.app.post.domain.Post;
import com.worldrank.app.post.repository.PostRepository;
import com.worldrank.app.score.domain.Visit;
import com.worldrank.app.score.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private PostRepository postRepository;
    private VisitRepository visitRepository;

    @Transactional
    public UUID createPost(UUID userId, UUID placeId, String description, String imageUrl) {
        Post post = new Post(
                UUID.randomUUID(),
                userId,
                placeId,
                description,
                imageUrl,
                OffsetDateTime.now()
        );
        postRepository.save(post);

        Visit visit = new Visit(
                UUID.randomUUID(),
                userId,
                placeId,
                OffsetDateTime.now()
        );
        visitRepository.save(visit); // trigger DB suma puntaje

        return post.getId();
    }

    public Page<Post> feed(Pageable pageable) {
        return postRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
}