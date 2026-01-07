package com.worldrank.app.auth.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private JwtProvider jwtProvider;

    public JwtFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();

        if (uri.startsWith("/api/v1/auth/")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        System.out.println("JwtFilter: URI=" + uri + ", AuthHeader=" + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtProvider.validate(token)) {
                String subject = jwtProvider.getSubject(token);
                var auth = new UsernamePasswordAuthenticationToken(subject, null, null);
                SecurityContextHolder.getContext().setAuthentication(auth);
                System.out.println("JwtFilter: Set authentication for " + subject);
            } else {
                System.out.println("JwtFilter: Invalid token");
            }
        }
        chain.doFilter(request, response);
    }
}