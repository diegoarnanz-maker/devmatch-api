package com.devmatch.api.shared.domain.exception;

/**
 * Excepción lanzada cuando se viola una regla de negocio
 */
public class BusinessRuleViolationException extends RuntimeException {
    
    public BusinessRuleViolationException(String message) {
        super(message);
    }
    
    public BusinessRuleViolationException(String rule, String details) {
        super("Violación de regla de negocio '" + rule + "': " + details);
    }
    
    public BusinessRuleViolationException(String entity, String rule, String details) {
        super("Violación de regla de negocio en " + entity + " - '" + rule + "': " + details);
    }
}
