package com.felipecordeiro.dscatalog.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.felipecordeiro.dscatalog.security.exception.InvalidJwtTokenException;
import com.felipecordeiro.dscatalog.security.exception.TokenCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class JwtService {

    private final String secret;
    private final long expirationMillis;
    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    public JwtService(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expirationMillis) {
        this.secret = secret;
        this.expirationMillis = expirationMillis;
        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(algorithm)
                .withIssuer("dscatalog-api")
                .build();
    }

    public String generateToken(UserDetails user) {
        if (user == null || user.getUsername() == null) {
            throw new TokenCreationException("User details are required to create a token", null);
        }

        List<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        try {
            return JWT.create()
                    .withSubject(user.getUsername())
                    .withIssuer("dscatalog-api")
                    .withClaim("authorities", authorities)
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(Instant.now().plusMillis(expirationMillis))
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new TokenCreationException("Error creating JWT token", e);
        }
    }

    public void validateTokenOrThrow(String token) {
        try {
            verifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new InvalidJwtTokenException("Token is invalid or expired", e);
        }
    }

    public String getSubject(String token) {
        return JWT.decode(token).getSubject();
    }
}