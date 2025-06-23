package com.devmatch.api.security.infrastructure.in.security;

import com.devmatch.api.user.application.port.out.UserRepositoryPort;
import com.devmatch.api.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName().getValue()));

        return new org.springframework.security.core.userdetails.User(
            user.getUsername().getValue(),
            user.getPasswordHash().getValue(),
            authorities
        );
    }

    public UserDetails loadUserById(Long id) {
        User user = userRepositoryPort.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con ID: " + id));

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName().getValue()));

        return new org.springframework.security.core.userdetails.User(
            user.getUsername().getValue(),
            user.getPasswordHash().getValue(),
            authorities
        );
    }
} 