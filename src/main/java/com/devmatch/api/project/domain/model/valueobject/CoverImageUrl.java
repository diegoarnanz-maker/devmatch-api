package com.devmatch.api.project.domain.model.valueobject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * Value Object que representa la URL de la imagen de portada de un proyecto.
 * Encapsula las reglas de validación para URLs de imágenes.
 */
public class CoverImageUrl {
    private final String value;
    private final String normalizedValue;
    
    // Constantes para validación
    private static final String IMAGE_PATTERN = "^(https?://).*\\.(jpg|jpeg|png|gif|webp|svg)(\\?.*)?$";
    private static final int MAX_URL_LENGTH = 255; // Según DDL
    
    public CoverImageUrl(String value) {
        validateImageUrl(value);
        this.value = value != null ? value.trim() : null;
        this.normalizedValue = value != null ? normalizeUrl(value.trim()) : null;
    }
    
    private void validateImageUrl(String url) {
        if (url == null) {
            // Es opcional según el DDL
            return;
        }
        
        String trimmedUrl = url.trim();
        
        if (trimmedUrl.isEmpty()) {
            throw new IllegalArgumentException("La URL de la imagen no puede estar vacía");
        }
        
        if (trimmedUrl.length() > MAX_URL_LENGTH) {
            throw new IllegalArgumentException(
                String.format("La URL de la imagen no puede exceder %d caracteres", MAX_URL_LENGTH)
            );
        }
        
        // Validar formato de URL
        if (!isValidUrl(trimmedUrl)) {
            throw new IllegalArgumentException("La URL de la imagen no tiene un formato válido");
        }
        
        // Validar que sea una URL de imagen
        if (!isImageUrl(trimmedUrl)) {
            throw new IllegalArgumentException(
                "La URL debe apuntar a una imagen válida (jpg, jpeg, png, gif, webp, svg)"
            );
        }
        
        // Validar que use HTTPS (recomendación de seguridad)
        if (!trimmedUrl.toLowerCase().startsWith("https://")) {
            throw new IllegalArgumentException(
                "Por seguridad, la URL de la imagen debe usar HTTPS"
            );
        }
    }
    
    private boolean isValidUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }
    
    private boolean isImageUrl(String url) {
        String lowerUrl = url.toLowerCase();
        return lowerUrl.matches(IMAGE_PATTERN);
    }
    
    private String normalizeUrl(String url) {
        // Asegurar que tenga protocolo HTTPS
        if (!url.startsWith("http")) {
            url = "https://" + url;
        }
        
        // Remover www si está presente
        url = url.replace("www.", "");
        
        return url;
    }
    
    public String getValue() {
        return value;
    }
    
    public String getNormalizedValue() {
        return normalizedValue;
    }
    
    public boolean isPresent() {
        return value != null && !value.isEmpty();
    }
    
    public String getImageFormat() {
        if (!isPresent()) return null;
        
        String lowerUrl = normalizedValue.toLowerCase();
        if (lowerUrl.endsWith(".jpg") || lowerUrl.endsWith(".jpeg")) {
            return "JPEG";
        } else if (lowerUrl.endsWith(".png")) {
            return "PNG";
        } else if (lowerUrl.endsWith(".gif")) {
            return "GIF";
        } else if (lowerUrl.endsWith(".webp")) {
            return "WEBP";
        } else if (lowerUrl.endsWith(".svg")) {
            return "SVG";
        }
        return "UNKNOWN";
    }
    
    public boolean isVectorImage() {
        return "SVG".equals(getImageFormat());
    }
    
    public boolean isRasterImage() {
        String format = getImageFormat();
        return "JPEG".equals(format) || "PNG".equals(format) || 
               "GIF".equals(format) || "WEBP".equals(format);
    }
    
    public String getDomain() {
        if (!isPresent()) return null;
        
        try {
            URI uri = new URI(normalizedValue);
            return uri.getHost();
        } catch (URISyntaxException e) {
            return null;
        }
    }
    
    public boolean isFromTrustedDomain() {
        String domain = getDomain();
        if (domain == null) return false;
        
        String lowerDomain = domain.toLowerCase();
        return lowerDomain.contains("githubusercontent.com") ||
               lowerDomain.contains("imgur.com") ||
               lowerDomain.contains("cloudinary.com") ||
               lowerDomain.contains("amazonaws.com") ||
               lowerDomain.contains("googleusercontent.com");
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CoverImageUrl that = (CoverImageUrl) obj;
        return Objects.equals(normalizedValue, that.normalizedValue);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(normalizedValue);
    }
    
    @Override
    public String toString() {
        return normalizedValue != null ? normalizedValue : "";
    }
} 