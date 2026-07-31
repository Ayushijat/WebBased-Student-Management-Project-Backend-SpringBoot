package com.example.main.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private final String SECRET =
            "mysecretkeymysecretkeymysecretkeymysecretkey";


    // ==============================
    // ACCESS TOKEN
    // ==============================

    public String generateAccessToken(
            String email,
            String role
    ) {

        return Jwts.builder()

                .setSubject(email)

                .claim("role", role)

                .claim("type", "ACCESS")

                .setIssuedAt(new Date())

                // 15 minutes
                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 15 * 60 * 1000
                        )
                )

                .signWith(
                        Keys.hmacShaKeyFor(
                                SECRET.getBytes()
                        ),
                        SignatureAlgorithm.HS256
                )

                .compact();
    }


    // ==============================
    // REFRESH TOKEN
    // ==============================

    public String generateRefreshToken(
            String email
    ) {

        return Jwts.builder()

                .setSubject(email)

                .claim("type", "REFRESH")

                .setIssuedAt(new Date())

                // 7 days
                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 7L * 24 * 60 * 60 * 1000
                        )
                )

                .signWith(
                        Keys.hmacShaKeyFor(
                                SECRET.getBytes()
                        ),
                        SignatureAlgorithm.HS256
                )

                .compact();
    }


    // ==============================
    // EXTRACT EMAIL
    // ==============================

    public String extractEmail(
            String token
    ) {

        return extractAllClaims(token)
                .getSubject();
    }


    // ==============================
    // EXTRACT ROLE
    // ==============================

    public String extractRole(
            String token
    ) {

        return extractAllClaims(token)
                .get(
                        "role",
                        String.class
                );
    }


    // ==============================
    // EXTRACT TOKEN TYPE
    // ==============================

    public String extractTokenType(
            String token
    ) {

        return extractAllClaims(token)
                .get(
                        "type",
                        String.class
                );
    }


    // ==============================
    // EXTRACT ALL CLAIMS
    // ==============================

    private Claims extractAllClaims(
            String token
    ) {

        return Jwts.parserBuilder()

                .setSigningKey(
                        Keys.hmacShaKeyFor(
                                SECRET.getBytes()
                        )
                )

                .build()

                .parseClaimsJws(token)

                .getBody();
    }


    // ==============================
    // VALIDATE TOKEN
    // ==============================

    public boolean validateToken(
            String token
    ) {

        try {

            extractAllClaims(token);

            return true;

        } catch (Exception e) {

            return false;
        }
    }
}