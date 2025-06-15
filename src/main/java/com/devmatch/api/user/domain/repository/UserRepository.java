package com.devmatch.api.user.domain.repository;

import java.util.List;
import java.util.Optional;

import com.devmatch.api.user.domain.model.User;

public interface UserRepository {

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findAllActive(); // filtra por is_active=true AND is_deleted=false

    User save(User user);

    void deleteById(Long id); // Lógica real será soft delete

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
    
}
