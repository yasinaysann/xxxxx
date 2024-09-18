package com.project.projectx.auth.repository;

import com.project.projectx.auth.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername(
            @Pattern(regexp = "^[a-zA-Z0-9]+$",
            message = "Only Numeric or Alphabetic Character")
            String username
    );
    User findByEmail(@Email String email);

    Boolean existsByUsername(
            @Pattern(regexp = "^[a-zA-Z0-9]+$",
            message = "Only Numeric or Alphabetic Character")
            String username);
    Boolean existsByEmail(@Email String email);
}
