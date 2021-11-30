package com.fyp.vasclinicserver.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;

import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


import java.security.*;
import java.security.SignatureException;

import java.sql.Date;
import java.time.Instant;
import java.util.UUID;

import static java.util.Date.from;

@Service
public class JwtProvider {

    @Value("${jwt.jwtSecret}")
    private String jwtSecret;

    @Value("${jwt.expiration.time}")
    private Long jwtExpirationInMillis;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtProvider.class);

    private Key getKey() {
        byte[] keyBytes =  Decoders.BASE64.decode(jwtSecret);
     return Keys.hmacShaKeyFor(keyBytes);
    }


    public String generateToken(Authentication authentication) {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .setIssuedAt(from(Instant.now()))
                .signWith(getKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();
    }

    public String generateTokenWithUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(from(Instant.now()))
                .signWith(getKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();
    }

    public boolean validateToken(String authToken) throws SignatureException {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            LOGGER.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            LOGGER.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            LOGGER.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            LOGGER.error("JWT claims string is empty.");
        }
        return false;
    }

    public String getUsernameFromJwt(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public Long getJwtExpirationInMillis() {
        return jwtExpirationInMillis;
    }
}
