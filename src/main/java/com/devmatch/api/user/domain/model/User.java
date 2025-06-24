package com.devmatch.api.user.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

import com.devmatch.api.shared.domain.model.BaseDomainEntity;
import com.devmatch.api.user.domain.model.valueobject.user.Email;
import com.devmatch.api.user.domain.model.valueobject.user.Password;
import com.devmatch.api.user.domain.model.valueobject.user.Username;
import com.devmatch.api.role.domain.model.Role;

public class User extends BaseDomainEntity {

    // Autenticación
    private Username username;
    private Email email;
    private Password passwordHash;
    private Role role;

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
            Role role,
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
            String avatarUrl, String bio, Role role) {

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
        this.role = role;
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

    public Role getRole() {
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

    // Métodos set individuales para actualización parcial
    public void setFirstName(String firstName) {
        this.firstName = firstName;
        updateTimestamp();
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        updateTimestamp();
    }

    public void setCountry(String country) {
        this.country = country;
        updateTimestamp();
    }

    public void setProvince(String province) {
        this.province = province;
        updateTimestamp();
    }

    public void setCity(String city) {
        this.city = city;
        updateTimestamp();
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
        updateTimestamp();
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
        updateTimestamp();
    }

    public void setPortfolioUrl(String portfolioUrl) {
        this.portfolioUrl = portfolioUrl;
        updateTimestamp();
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        updateTimestamp();
    }

    public void setBio(String bio) {
        this.bio = bio;
        updateTimestamp();
    }

    public void changePassword(Password newPasswordHash) {
        this.passwordHash = newPasswordHash;
        updateTimestamp();
    }

    public void changeEmail(Email newEmail) {
        this.email = newEmail;
        updateTimestamp();
    }

    public void setActive(boolean active) {
        this.isActive = active;
        updateTimestamp();
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
        updateTimestamp();
    }

    public boolean isAdmin() {
        return role != null && "ADMIN".equals(role.getName());
    }

    public void setRole(Role role) {
        this.role = role;
        updateTimestamp();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}