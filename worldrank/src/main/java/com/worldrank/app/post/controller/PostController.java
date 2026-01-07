package com.worldrank.app.post.controller;

import com.worldrank.app.post.service.PostService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.*;
import org.springframework.data.domain.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private PostService postService;

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    public UUID create(@AuthenticationPrincipal String userId,
                        @RequestBody CreatePostRequest request) {
        return postService.createPost(
                UUID.fromString(userId),
                request.placeId(),
                request.description(),
                request.imageUrl()
        );
    }

    @GetMapping("/feed")
    public Page<?> feed(@RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {
        return postService.feed(PageRequest.of(page, size));
    }
}

record CreatePostRequest(UUID placeId, String description, String imageUrl) {}

