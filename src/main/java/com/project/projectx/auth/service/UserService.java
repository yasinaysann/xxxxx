package com.project.projectx.auth.service;

import com.project.projectx.auth.entity.User;
import com.project.projectx.auth.model.Role;
import com.project.projectx.auth.model.UserDetail;
import com.project.projectx.auth.model.request.RegisterRequest;
import com.project.projectx.auth.model.response.RegisterResponse;
import com.project.projectx.auth.repository.UserRepository;
import com.project.projectx.exceptionHandler.exception.AuthenticatedFailedException;
import com.project.projectx.exceptionHandler.exception.UserAlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    //todo exception yapısı düzenlenecek
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return UserDetail.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .name(user.getName())
                .surname(user.getSurname())
                .phoneNumber(user.getPhoneNumber())
                .authorities(user.getRole())
                .accountNonExpired(user.getIsAccountNonExpired())
                .accountNonLocked(user.getIsAccountNonLocked())
                .credentialsNonExpired(user.getIsCredentialsNonExpired())
                .enabled(user.getIsEnabled())
                .build();

    }

    public RegisterResponse save(RegisterRequest register) {
        if (userRepository.existsByUsername(register.username()))
            throw new UserAlreadyExistException("Username already exists");
        if (userRepository.existsByEmail(register.email()))
            throw new UserAlreadyExistException("Email already exists");
        //todo telefon numarası kontrolü eklenecek

        User user = convertUserFromRequest(register);

        user = userRepository.save(user);

        return new RegisterResponse(user.getId().toString(),user.getUsername(), user.getEmail());
    }

    private User convertUserFromRequest(RegisterRequest register) {
        User user = new User();
        user.setUsername(register.username());
        user.setPassword(passwordEncoder.encode(register.password()));
        user.setEmail(register.email());
        user.setName(register.name());
        user.setSurname(register.surname());
        user.setPhoneNumber(register.phoneNumber());
        user.setIsAccountNonExpired(true);
        user.setIsAccountNonLocked(false);
        user.setIsCredentialsNonExpired(true);
        user.setIsEnabled(false);
        user.setRole(register.role() != null ? register.role() : Role.ROLE_USER);
        user.setClientIp(null);
        return user;
    }

    public UserDetail getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object o = authentication.getPrincipal();
            if (o instanceof UserDetail) {
                return (UserDetail) o;
            }
        }
        return null;
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
/*
accountNonExpired: Hesap geçerli mi?
accountNonLocked: Hesap kilitli mi?
credentialsNonExpired: Şifre geçerli mi?
enabled: Hesap aktif mi?
 */
    public boolean checkUser(UserDetail userDetail){
        if (!userDetail.isAccountNonExpired()){
            throw new AuthenticatedFailedException("Account is expired");
        }else if (!userDetail.isAccountNonLocked()){
            throw new AuthenticatedFailedException("Account is locked");
        }else if (!userDetail.isCredentialsNonExpired()){
            throw new AuthenticatedFailedException("Credentials is expired");
        }else if (!userDetail.isEnabled()){
            throw new AuthenticatedFailedException("User is not enabled");
        }else{
            return true;
        }
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
