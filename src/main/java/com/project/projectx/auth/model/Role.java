package com.project.projectx.auth.model;


import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
     ROLE_ADMIN("ADMIN"),
     ROLE_USER("USER");

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }



    @Override
    public String getAuthority() {
        return authority;
    }
}
