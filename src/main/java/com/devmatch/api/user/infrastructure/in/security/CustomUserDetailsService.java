package com.devmatch.api.user.infrastructure.in.security;

import com.devmatch.api.user.application.port.out.UserPersistencePort;
import com.devmatch.api.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserPersistencePort userPersistencePort;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userPersistencePort.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return new org.springframework.security.core.userdetails.User(
            user.getUsername().getValue(),
            user.getPasswordHash().getValue(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }

    public UserDetails loadUserById(Long id) {
        User user = userPersistencePort.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con ID: " + id));

        return new org.springframework.security.core.userdetails.User(
            user.getUsername().getValue(),
            user.getPasswordHash().getValue(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }
} 