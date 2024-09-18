package com.project.projectx.auth.filter;

import com.project.projectx.auth.model.UserDetail;
import com.project.projectx.auth.service.JwtService;
import com.project.projectx.auth.service.UserService;
import com.project.projectx.core.model.ApiResponse;
import com.project.projectx.exceptionHandler.exception.AuthenticatedFailedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserService userService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.addCookie(new Cookie("Test", UUID.randomUUID().toString()));


        String username = null;
        if (request.getHeader("Authorization") != null) {
            String token = jwtService.replaceToken(request.getHeader("Authorization"));
//            if(request.getRequestURI().contains("/auth/login")) {
//                SecurityContextHolder.clearContext();
//            }else{
//                throw new RuntimeException("Invalid token");
//            }

            username = jwtService.getUsernameFromJWT(token);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                    UserDetail userDetail =(UserDetail) userService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }catch (Exception e){
                    //return ApiResponse.error(HttpStatus.UNAUTHORIZED, "Username or password is incorrect");
                    throw new AuthenticatedFailedException("Username or password is incorrect");
                }

            }
        }


        filterChain.doFilter(request, response);
    }

}
