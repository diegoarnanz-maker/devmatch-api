package com.devmatch.api.project.domain.service;

import com.devmatch.api.project.application.port.out.ProjectApplicationRepositoryPort;
import com.devmatch.api.project.domain.model.ProjectApplication;
import com.devmatch.api.project.domain.model.valueobject.ApplicationStatus;
import com.devmatch.api.shared.application.port.out.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio de dominio que maneja la expiración automática de aplicaciones a proyectos.
 * Se ejecuta periódicamente para revisar aplicaciones que han estado pendientes por más de 7 días.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectApplicationExpirationService {

    private final ProjectApplicationRepositoryPort projectApplicationRepositoryPort;
    private final DomainEventPublisher domainEventPublisher;

    private static final int EXPIRATION_DAYS = 7;

    /**
     * Tarea programada que se ejecuta cada día para revisar aplicaciones vencidas.
     * Busca aplicaciones en estado PENDING que tienen más de 7 días sin respuesta.
     */
    @Scheduled(cron = "0 0 2 * * ?") // Cada día a las 2:00 AM
    public void checkAndExpireApplications() {
        log.info("Iniciando verificación de aplicaciones vencidas...");
        
        try {
            LocalDateTime expirationThreshold = LocalDateTime.now().minusDays(EXPIRATION_DAYS);
            
            // Buscar aplicaciones pendientes que han superado el umbral de expiración
            List<ProjectApplication> expiredApplications = projectApplicationRepositoryPort
                .findPendingApplicationsOlderThan(expirationThreshold);
            
            if (expiredApplications.isEmpty()) {
                log.debug("No se encontraron aplicaciones vencidas");
                return;
            }
            
            log.info("Se encontraron {} aplicaciones vencidas para procesar", expiredApplications.size());
            
            for (ProjectApplication application : expiredApplications) {
                try {
                    // Verificar que la aplicación aún esté en estado PENDING
                    if (application.getStatus() != ApplicationStatus.PENDING) {
                        log.debug("Aplicación {} ya no está en estado PENDING (estado actual: {}), saltando", 
                                application.getId(), application.getStatus());
                        continue;
                    }
                    
                    expireApplication(application);
                } catch (Exception e) {
                    log.error("Error al expirar aplicación {}: {}", application.getId(), e.getMessage(), e);
                }
            }
            
            log.info("Procesamiento de aplicaciones vencidas completado");
            
        } catch (Exception e) {
            log.error("Error durante la verificación de aplicaciones vencidas", e);
        }
    }

    /**
     * Expira una aplicación específica, cambiando su estado a EXPIRED.
     * 
     * @param application La aplicación a expirar
     */
    private void expireApplication(ProjectApplication application) {
        log.info("Expirando aplicación {} del usuario {} para proyecto {}", 
                application.getId(), application.getUserId(), application.getProjectId());
        
        // Verificación adicional: asegurar que la aplicación esté en estado PENDING
        if (application.getStatus() != ApplicationStatus.PENDING) {
            log.warn("Intento de expirar aplicación {} que no está en estado PENDING (estado actual: {}), abortando", 
                    application.getId(), application.getStatus());
            return;
        }
        
        // Cambiar estado a EXPIRED
        application.expire();
        
        // Guardar la aplicación actualizada
        projectApplicationRepositoryPort.save(application);
        
        // Publicar evento de dominio
        domainEventPublisher.publish(new com.devmatch.api.project.domain.event.ProjectApplicationExpiredEvent(
            application.getUserId(),
            application.getProjectId(),
            "Proyecto #" + application.getProjectId(), // Fallback para el nombre del proyecto
            null // No tenemos acceso directo al ownerId desde aquí
        ));
        
        log.info("Aplicación {} expirada exitosamente", application.getId());
    }

    /**
     * Método manual para expirar una aplicación específica (útil para testing).
     * 
     * @param applicationId ID de la aplicación a expirar
     */
    public void manuallyExpireApplication(Long applicationId) {
        ProjectApplication application = projectApplicationRepositoryPort.findById(applicationId)
            .orElseThrow(() -> new IllegalArgumentException("Aplicación no encontrada: " + applicationId));
        
        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new IllegalStateException("Solo se pueden expirar aplicaciones en estado PENDING");
        }
        
        expireApplication(application);
    }
}
