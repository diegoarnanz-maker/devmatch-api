package com.devmatch.api.user.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

import com.devmatch.api.shared.domain.model.BaseDomainEntity;
import com.devmatch.api.user.domain.model.valueobject.Email;
import com.devmatch.api.user.domain.model.valueobject.Password;
import com.devmatch.api.user.domain.model.valueobject.Username;

public class User extends BaseDomainEntity {

    // Autenticación
    private Username username;
    private Email email;
    private Password passwordHash;
    private String role;

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

    // Constructor completo
    public User(Long id, Username username, Email email, Password passwordHash,
            String firstName, String lastName,
            String country, String province, String city,
            String githubUrl, String linkedinUrl, String portfolioUrl,
            String avatarUrl, String bio,
            boolean isActive, boolean isDeleted,
            String role,
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
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Constructor simplificado
    public User(Username username, Email email, Password passwordHash,
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
        this.role = "USER"; // Rol por defecto
        this.createdAt = LocalDateTime.now();
    }

    // Getters
    public Username getUsername() {
        return username;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPasswordHash() {
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

    public String getRole() {
        return role;
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

    public void changePassword(Password newPasswordHash) {
        this.passwordHash = newPasswordHash;
        updateTimestamp();
    }

    // Métodos de ciclo de vida
    public void setActive(boolean active) {
        this.isActive = active;
        updateTimestamp();
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
        updateTimestamp();
    }

    public boolean isAdmin() {
        return this.role != null && this.role.equals("ADMIN");
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