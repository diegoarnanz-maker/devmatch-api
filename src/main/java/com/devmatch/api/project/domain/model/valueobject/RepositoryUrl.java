package com.devmatch.api.project.domain.model.valueobject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * Value Object que representa la URL de un repositorio.
 * Encapsula las reglas de validación para URLs de repositorios (principalmente GitHub).
 */
public class RepositoryUrl {
    private final String value;
    private final String normalizedValue;
    
    // Constantes para validación
    private static final String GITHUB_PATTERN = "^(https?://)?(www\\.)?github\\.com/[a-zA-Z0-9-]+/[a-zA-Z0-9-_.]+/?$";
    private static final String GITLAB_PATTERN = "^(https?://)?(www\\.)?gitlab\\.com/[a-zA-Z0-9-]+/[a-zA-Z0-9-_.]+/?$";
    private static final String BITBUCKET_PATTERN = "^(https?://)?(www\\.)?bitbucket\\.org/[a-zA-Z0-9-]+/[a-zA-Z0-9-_.]+/?$";
    
    public RepositoryUrl(String value) {
        validateUrl(value);
        this.value = value.trim();
        this.normalizedValue = normalizeUrl(value.trim());
    }
    
    private void validateUrl(String url) {
        if (url == null) {
            throw new IllegalArgumentException("La URL del repositorio no puede ser nula");
        }
        
        String trimmedUrl = url.trim();
        
        if (trimmedUrl.isEmpty()) {
            throw new IllegalArgumentException("La URL del repositorio no puede estar vacía");
        }
        
        // Validar formato de URL
        if (!isValidUrl(trimmedUrl)) {
            throw new IllegalArgumentException("La URL del repositorio no tiene un formato válido");
        }
        
        // Validar que sea de un proveedor soportado
        if (!isSupportedProvider(trimmedUrl)) {
            throw new IllegalArgumentException(
                "Solo se soportan repositorios de GitHub, GitLab y Bitbucket"
            );
        }
        
        // Validar que la URL sea accesible (opcional, puede ser costoso)
        // if (!isUrlAccessible(trimmedUrl)) {
        //     throw new IllegalArgumentException("La URL del repositorio no es accesible");
        // }
    }
    
    private boolean isValidUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }
    
    private boolean isSupportedProvider(String url) {
        String lowerUrl = url.toLowerCase();
        return lowerUrl.matches(GITHUB_PATTERN) || 
               lowerUrl.matches(GITLAB_PATTERN) || 
               lowerUrl.matches(BITBUCKET_PATTERN);
    }
    
    private String normalizeUrl(String url) {
        // Asegurar que tenga protocolo HTTPS
        if (!url.startsWith("http")) {
            url = "https://" + url;
        }
        
        // Remover www si está presente
        url = url.replace("www.", "");
        
        // Asegurar que no termine en /
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        
        return url;
    }
    
    public String getValue() {
        return value;
    }
    
    public String getNormalizedValue() {
        return normalizedValue;
    }
    
    public String getProvider() {
        String lowerUrl = normalizedValue.toLowerCase();
        if (lowerUrl.contains("github.com")) {
            return "GitHub";
        } else if (lowerUrl.contains("gitlab.com")) {
            return "GitLab";
        } else if (lowerUrl.contains("bitbucket.org")) {
            return "Bitbucket";
        }
        return "Unknown";
    }
    
    public String getOwner() {
        String[] parts = normalizedValue.split("/");
        if (parts.length >= 4) {
            return parts[3]; // github.com/owner/repo
        }
        return null;
    }
    
    public String getRepositoryName() {
        String[] parts = normalizedValue.split("/");
        if (parts.length >= 5) {
            return parts[4]; // github.com/owner/repo
        }
        return null;
    }
    
    public boolean isGitHub() {
        return getProvider().equals("GitHub");
    }
    
    public boolean isGitLab() {
        return getProvider().equals("GitLab");
    }
    
    public boolean isBitbucket() {
        return getProvider().equals("Bitbucket");
    }
    
    public String getApiUrl() {
        if (isGitHub()) {
            return "https://api.github.com/repos/" + getOwner() + "/" + getRepositoryName();
        } else if (isGitLab()) {
            return "https://gitlab.com/api/v4/projects/" + getOwner() + "%2F" + getRepositoryName();
        }
        return null;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RepositoryUrl that = (RepositoryUrl) obj;
        return Objects.equals(normalizedValue, that.normalizedValue);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(normalizedValue);
    }
    
    @Override
    public String toString() {
        return normalizedValue;
    }
} 