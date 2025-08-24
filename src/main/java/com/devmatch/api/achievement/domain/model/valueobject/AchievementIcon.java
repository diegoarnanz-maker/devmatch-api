package com.devmatch.api.achievement.domain.model.valueobject;

import java.util.Objects;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * Value Object que representa el icono de un logro.
 * Encapsula las reglas de validación y formato de la URL del icono.
 */
public class AchievementIcon {
    private final String value;
    
    // Constantes para validación
    private static final int MAX_LENGTH = 500;
    private static final String VALID_PATTERN = "^https?://[\\w\\-\\.]+\\.[a-zA-Z]{2,}(?:[\\w\\-\\./\\?\\=\\&\\%\\#\\+]*)?$";
    
    // Dominios permitidos para iconos
    private static final String[] ALLOWED_DOMAINS = {
        "cdn.jsdelivr.net",
        "cdnjs.cloudflare.com",
        "fonts.gstatic.com",
        "unpkg.com",
        "cdn.example.com" // Para datos de prueba
    };
    
    public AchievementIcon(String value) {
        validateIcon(value);
        this.value = value.trim();
    }
    
    private void validateIcon(String iconUrl) {
        if (iconUrl == null) {
            throw new IllegalArgumentException("La URL del icono no puede ser nula");
        }
        
        String trimmedIcon = iconUrl.trim();
        
        if (trimmedIcon.isEmpty()) {
            throw new IllegalArgumentException("La URL del icono no puede estar vacía");
        }
        
        if (trimmedIcon.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                String.format("La URL del icono no puede exceder %d caracteres", MAX_LENGTH)
            );
        }
        
        // Validar formato de URL
        if (!trimmedIcon.matches(VALID_PATTERN)) {
            throw new IllegalArgumentException(
                "La URL del icono tiene un formato inválido. Debe ser una URL HTTP/HTTPS válida"
            );
        }
        
        // Validar que sea una URL válida
        try {
            new URL(trimmedIcon);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("La URL del icono no es válida: " + e.getMessage());
        }
        
        // Validar dominio permitido
        boolean isAllowedDomain = false;
        for (String allowedDomain : ALLOWED_DOMAINS) {
            if (trimmedIcon.contains(allowedDomain)) {
                isAllowedDomain = true;
                break;
            }
        }
        
        if (!isAllowedDomain) {
            throw new IllegalArgumentException(
                "El dominio del icono no está permitido. Solo se permiten CDNs confiables"
            );
        }
        
        // Validaciones específicas de negocio para iconos
        String lowerIcon = trimmedIcon.toLowerCase();
        if (lowerIcon.contains("spam") || 
            lowerIcon.contains("test") ||
            lowerIcon.contains("prueba") ||
            lowerIcon.contains("malicious") ||
            lowerIcon.contains("virus")) {
            throw new IllegalArgumentException("La URL del icono contiene palabras no permitidas");
        }
    }
    
    public String getValue() {
        return value;
    }
    
    public int getLength() {
        return value.length();
    }
    
    public boolean isHttps() {
        return value.startsWith("https://");
    }
    
    public boolean isHttp() {
        return value.startsWith("http://");
    }
    
    public String getDomain() {
        try {
            URL url = new URL(value);
            return url.getHost();
        } catch (MalformedURLException e) {
            return "unknown";
        }
    }
    
    public String getPath() {
        try {
            URL url = new URL(value);
            return url.getPath();
        } catch (MalformedURLException e) {
            return "";
        }
    }
    
    public boolean isFromCDN() {
        String domain = getDomain();
        for (String allowedDomain : ALLOWED_DOMAINS) {
            if (domain.contains(allowedDomain)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isShortUrl() {
        return value.length() <= 100;
    }
    
    public boolean isLongUrl() {
        return value.length() >= 300;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AchievementIcon that = (AchievementIcon) obj;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}
