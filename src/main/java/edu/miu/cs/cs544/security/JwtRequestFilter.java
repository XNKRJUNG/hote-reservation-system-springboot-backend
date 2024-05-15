package edu.miu.cs.cs544.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import java.io.IOException;

import static edu.miu.cs.cs544.domain.UserType.ADMIN;
import static edu.miu.cs.cs544.domain.UserType.CLIENT;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String[] WHITE_LIST_URL = {
            "/auth/login",
            "/auth/signup",
            "/auth/admin",
    };

    @Autowired
    AccessDeniedHandler accessDeniedHandler;
    private final JwtTokenService jwtTokenService;

    @Autowired
    public JwtRequestFilter(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            final String token = request.getHeader("Authorization");
            if (token != null) {
                // Validate the JWT token
                String subToken = token.substring(7);
                Authentication authentication = jwtTokenService.getAuthentication(subToken);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\": \"JWT token validation failed\"}");
                    return;
                }
            }
        } catch (Exception e) {
//            logger.error("JWT token validation failed", e);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"JWT token validation failed\"}");
            return;
        }
        filterChain.doFilter(request, response);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITE_LIST_URL).permitAll()
                        .requestMatchers("/auth/add/admin").hasAnyRole(ADMIN.name())
                        .requestMatchers("/products").hasAnyRole(ADMIN.name())
                        .requestMatchers("/state").hasAnyRole(ADMIN.name())
                        .requestMatchers("/country").hasAnyRole(ADMIN.name())
                        .anyRequest().authenticated())
                .addFilterBefore(this, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .accessDeniedHandler(accessDeniedHandler)
                );
        ;
        return http.build();
    }
}
