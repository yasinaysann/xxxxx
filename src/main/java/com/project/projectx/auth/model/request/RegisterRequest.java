package com.project.projectx.auth.model.request;


import com.project.projectx.auth.model.Role;

public record  RegisterRequest(
        String username,
        String password,
        String email,
        String name,
        String surname,
        String phoneNumber,
        Role role
) {
}
