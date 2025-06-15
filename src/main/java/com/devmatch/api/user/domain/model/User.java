package com.devmatch.api.user.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

import com.devmatch.api.shared.domain.model.BaseDomainEntity;

public class User extends BaseDomainEntity {

    // Autenticación
    private String username;
    private String email;
    private String passwordHash;

    // Datos personales
    private String firstName;
    private String lastName;

    // Localización
    private String country;
    private String province;
    private String city;

    // Enlaces profesionales
    private String githubUrl;
    private String linkedinUrl;
    private String portfolioUrl;
    private String avatarUrl;

    // Descripción
    private String bio;

    // Constructor completo (para reconstrucción desde una fuente externa (BD,
    // eventos, etc.))
    public User(Long id, String username, String email, String passwordHash,
            String firstName, String lastName,
            String country, String province, String city,
            String githubUrl, String linkedinUrl, String portfolioUrl,
            String avatarUrl, String bio,
            boolean isActive, boolean isDeleted,
            LocalDateTime createdAt, LocalDateTime updatedAt) {

        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.province = province;
        this.city = city;
        this.githubUrl = githubUrl;
        this.linkedinUrl = linkedinUrl;
        this.portfolioUrl = portfolioUrl;
        this.avatarUrl = avatarUrl;
        this.bio = bio;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Constructor simplificado (creación desde capa de aplicación)
    public User(String username, String email, String passwordHash,
            String firstName, String lastName,
            String country, String province, String city,
            String githubUrl, String linkedinUrl, String portfolioUrl,
            String avatarUrl, String bio) {

        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.province = province;
        this.city = city;
        this.githubUrl = githubUrl;
        this.linkedinUrl = linkedinUrl;
        this.portfolioUrl = portfolioUrl;
        this.avatarUrl = avatarUrl;
        this.bio = bio;
        this.createdAt = LocalDateTime.now();
    }

    // Getters (los setters no son públicos, la modificación debe ser intencionada a
    // través de métodos explícitos como updateProfile)
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCountry() {
        return country;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public String getPortfolioUrl() {
        return portfolioUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getBio() {
        return bio;
    }

    // Lógica de dominio
    public void updateProfile(String firstName, String lastName, String country,
            String province, String city,
            String githubUrl, String linkedinUrl, String portfolioUrl,
            String avatarUrl, String bio) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.province = province;
        this.city = city;
        this.githubUrl = githubUrl;
        this.linkedinUrl = linkedinUrl;
        this.portfolioUrl = portfolioUrl;
        this.avatarUrl = avatarUrl;
        this.bio = bio;
        updateTimestamp();
    }

    // El servicio de autenticación debe llamar a este método para cambiar la
    // contraseña
    public void changePassword(String newPasswordHash) {
        this.passwordHash = newPasswordHash;
        updateTimestamp();
    }

    // Igualdad basada en identidad
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof User other))
            return false;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}