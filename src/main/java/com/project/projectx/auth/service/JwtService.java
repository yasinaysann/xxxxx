package com.project.projectx.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.project.projectx.auth.model.UserDetail;
import com.project.projectx.exceptionHandler.exception.AuthenticatedFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    @Value(value = "${jwt.secret}")
    private String secret;
    @Value(value = "${jwt.expiration}")
    private long expirationTime;

    private JWTVerifier jwtVerifier;


    public String generateToken(UserDetail user) {
        Map<String, Object> claims = new HashMap<>();
        return "Bearer " + JWT.create()
                .withJWTId(UUID.randomUUID().toString())
                .withClaim("username", user.getUsername())
                .withClaim("phone", user.getPhoneNumber())
                .withIssuedAt(new Date())
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .withNotBefore(new Date(System.currentTimeMillis() + 1000L))
                .sign(Algorithm.HMAC256(secret));
    }

    public String getUsernameFromJWT(String token) {
        DecodedJWT decodedJWT = verifyToken(token);
        if (!isJWTExpired(decodedJWT)){
            return decodedJWT.getClaim("username").asString();
        }
        return null;
    }



    public DecodedJWT verifyToken(String token) {
        DecodedJWT decodedJWT = null;
        try {
            jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();
            decodedJWT = jwtVerifier.verify(token);
            return isJWTExpired(decodedJWT) ? null : decodedJWT;

        }catch (JWTVerificationException exception) {
            log.error(exception.getMessage());
            throw new AuthenticatedFailedException("Session is not valid");
        }

    }

    private boolean isJWTExpired(DecodedJWT decodedJWT) {
        Date expiresAt = decodedJWT.getExpiresAt();
        return expiresAt.before(new Date());
    }


    public String replaceToken(String token) {
        return token.replace("Bearer ","");
    }


}
