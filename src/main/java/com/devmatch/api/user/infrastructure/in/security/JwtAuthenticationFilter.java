package com.devmatch.api.user.infrastructure.in.security;

import com.devmatch.api.user.application.port.out.AuthTokenRepositoryPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthTokenRepositoryPort authTokenRepositoryPort;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String requestPath = request.getRequestURI();
        final String method = request.getMethod();

        System.out.println("=== JWT Filter Debug ===");
        System.out.println("Path: " + requestPath);
        System.out.println("Method: " + method);
        System.out.println("Auth Header: " + (authHeader != null ? "Present" : "Missing"));

        // Permitir acceso sin token a las rutas de autenticación
        if (requestPath.contains("/api/v1/auth/")) {
            System.out.println("Skipping auth path");
            filterChain.doFilter(request, response);
            return;
        }

        // Si no hay header de autorización, continuar con el filtro
        // (Spring Security manejará la autenticación requerida)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("No valid auth header, continuing");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authTokenRepositoryPort.extractToken(authHeader);
            final String username = authTokenRepositoryPort.validateTokenAndGetUsername(jwt);
            System.out.println("Token validated for username: " + username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                System.out.println("User details loaded, authorities: " + userDetails.getAuthorities());
                
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("Authentication set in context successfully");
            } else {
                System.out.println("Username is null or authentication already exists");
            }
        } catch (Exception e) {
            logger.error("No se pudo establecer la autenticación del usuario", e);
            System.out.println("Error during authentication: " + e.getMessage());
            // No establecer la autenticación en caso de error
            // Spring Security manejará la respuesta de error
        }

        System.out.println("=== End JWT Filter Debug ===");
        filterChain.doFilter(request, response);
    }
} 