package com.project.projectx.auth.model.response;

public record LoginResponse(
        String id,
        String token,
        String name,
        String surname
) {
}
