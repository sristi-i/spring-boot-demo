package com.springboot.demo.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service // spring- managed bean with full lifecycle
public class JwtService {
    
    // read secret key from application.properties
    @Value("${app.jwt.secret}")
    private String secretKey;

    // read expiry from application.properties - 86400000ms = 24 hours
    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    // generate token
    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
                    .setClaims(new HashMap<>())
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(
                        System.currentTimeMillis() + expirationMs))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
    }

    // validate token
    public boolean isTokenValid(String token, UserDetails userDetails){
        // valid if: username matches and token is not expired
        return extractUsername(token).equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    // extract
    public String extractUsername(String token){
        // claims::getExpiration = get "exp" and check if before current time
        return extractClaim(token, Claims::getSubject);
    }

    private boolean isTokenExpired(String token){
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    // generic method - works for any claim type (String, Date,etc...)
    private <T> T extractClaim(String token, Function<Claims, T> resolver){
        return resolver.apply(
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
        );
    }

    // signing key
    private Key getSigningKey(){
        // decode Base64 secret -> convert raw bytes to Key object from HMAC-SHA256
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

}
