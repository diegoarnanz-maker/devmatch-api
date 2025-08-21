package com.devmatch.api.security.domain.exception;

/**
 * Excepción lanzada cuando hay errores en la configuración de seguridad
 */
public class SecurityConfigurationException extends RuntimeException {
    
    public SecurityConfigurationException(String message) {
        super(message);
    }
    
    public SecurityConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public SecurityConfigurationException(String component, String issue) {
        super("Error de configuración en el componente de seguridad '" + component + "': " + issue);
    }
}
