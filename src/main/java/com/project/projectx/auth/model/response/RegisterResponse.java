package com.project.projectx.auth.model.response;

public record RegisterResponse(
        String id,
        String username,
        String email
) {
}
