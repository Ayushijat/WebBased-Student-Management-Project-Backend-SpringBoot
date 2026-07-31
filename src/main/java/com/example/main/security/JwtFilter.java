package com.example.main.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(

            HttpServletRequest request,

            HttpServletResponse response,

            FilterChain filterChain

    ) throws ServletException, IOException {


        // 1. Get Authorization header

        String authHeader =
                request.getHeader("Authorization");


        String token = null;

        String email = null;


        // 2. Check Bearer Token

        if (

                authHeader != null

                        &&

                        authHeader.startsWith("Bearer ")

        ) {

            token =
                    authHeader.substring(7);


            // Extract email from token

            email =
                    jwtService.extractEmail(token);
        }


        // 3. Check if user is not already authenticated

        if (

                email != null

                        &&

                        SecurityContextHolder

                                .getContext()

                                .getAuthentication() == null

        ) {


            // 4. Load user from database

            UserDetails userDetails =

                    userDetailsService

                            .loadUserByUsername(email);


            // 5. Validate JWT

            if (

                    jwtService.validateToken(token)

            ) {


                // 6. Extract role from JWT

                String role =

                        jwtService.extractRole(token);


                // 7. Convert role into Spring Security authority

                List<GrantedAuthority> authorities =

                        List.of(

                                new SimpleGrantedAuthority(

                                        "ROLE_" + role

                                )

                        );


                // 8. Create authentication object

                UsernamePasswordAuthenticationToken authentication =

                        new UsernamePasswordAuthenticationToken(

                                userDetails,

                                null,

                                authorities

                        );


                // 9. Store authentication in SecurityContext

                SecurityContextHolder

                        .getContext()

                        .setAuthentication(authentication);

            }

        }


        // 10. Continue request

        filterChain.doFilter(

                request,

                response

        );

    }

}