package com.project.projectx.auth.entity;


import com.project.projectx.auth.model.Role;
import com.project.projectx.core.model.Base;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "app_users",
        indexes = {@Index(name = "idx_email", columnList = "email",unique = true),
                @Index(name = "idx_username", columnList = "username",unique = true)}
)
@EntityListeners(value = AuditingEntityListener.class)
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
public class User extends Base {
    @NotBlank
    @Size(max = 255)
    @Column(name = "username", unique = true)
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Only Numeric or Alphabetic Character")
    private String username;

    @Email
    @Size(max = 255)
    @NotBlank
    @Column(name = "email", unique = true)
    private String email;

    @Size(max = 255)
    @NotBlank
    @Column(name = "password")
    private String password;

    @Column(name = "name")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Only Numeric or Alphabetic Character")
    private String name;

    @Column(name = "surname")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Only Numeric or Alphabetic Character")
    private String surname;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "is_account_non_expired")
    private Boolean isAccountNonExpired;

    @Column(name = "is_account_non_locked")
    private Boolean isAccountNonLocked;

    @Column(name = "is_credectials_non_expired")
    private Boolean isCredentialsNonExpired;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;
    //todo yanlış şifre girişine karşın şifre sayısı tutulacak ve hesap pasife alınacak
    //todo verification code eklenecek ve mail ile doğrulanacak
    //todo sms login ile giriş yapma özelliği eklenebilir.
    // kullanıcının kayıt ipsi kayıt edilecek ve en son login olunan cihaz ipsi ya da bütün ipler saklanacak
}

