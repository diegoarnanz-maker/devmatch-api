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
 * Servicio de dominio para expiración automática de aplicaciones a proyectos.
 * 
 * <p>Maneja la lógica de negocio para expirar aplicaciones pendientes
 * que han superado el tiempo límite establecido.</p>
 * 
 * <p>Responsabilidades principales:</p>
 * <ul>
 *   <li>Expiración automática de aplicaciones vencidas</li>
 *   <li>Notificación de eventos de expiración</li>
 *   <li>Validación de estados de aplicación</li>
 * </ul>
 * 
 * @see <a href="../../../../docs/domain/project.md">Documentación completa del dominio</a>
 * @author diegoarnanz-maker
 * @version 1.0
 * @since 2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectApplicationExpirationService {

    private final ProjectApplicationRepositoryPort projectApplicationRepositoryPort;
    private final DomainEventPublisher domainEventPublisher;

    private static final int EXPIRATION_DAYS = 7;

    /**
     * Verifica y expira aplicaciones vencidas automáticamente.
     */
    @Scheduled(cron = "0 0 2 * * ?") // Cada día a las 2:00 AM
    public void checkAndExpireApplications() {
        try {
            LocalDateTime expirationThreshold = LocalDateTime.now().minusDays(EXPIRATION_DAYS);
            
            // Buscar aplicaciones pendientes expiradas
            List<ProjectApplication> expiredApplications = projectApplicationRepositoryPort
                .findPendingApplicationsOlderThan(expirationThreshold);
            
            if (expiredApplications.isEmpty()) {
                return;
            }
            
            for (ProjectApplication application : expiredApplications) {
                try {
                    // Verificar estado PENDING
                    if (application.getStatus() != ApplicationStatus.PENDING) {
                        continue;
                    }
                    
                    expireApplication(application);
                } catch (Exception e) {
                    log.error("Error al expirar aplicación {}: {}", application.getId(), e.getMessage(), e);
                }
            }
            
            
        } catch (Exception e) {
            log.error("Error durante la verificación de aplicaciones vencidas", e);
        }
    }

    /**
     * Expira una aplicación específica.
     *
     * @param application La aplicación a expirar
     */
    private void expireApplication(ProjectApplication application) {
        log.info("Expirando aplicación {} para proyecto {}", 
                application.getId(), application.getProjectId());
        
        // Verificar estado PENDING
        if (application.getStatus() != ApplicationStatus.PENDING) {
            return;
        }
        
        // Expirar aplicación
        application.expire();
        
        // Guardar cambios
        projectApplicationRepositoryPort.save(application);
        
        // Notificar evento
        domainEventPublisher.publish(new com.devmatch.api.project.domain.event.ProjectApplicationExpiredEvent(
            application.getUserId(),
            application.getProjectId(),
            "Proyecto #" + application.getProjectId(), // Fallback para el nombre del proyecto
            null // No tenemos acceso directo al ownerId desde aquí
        ));
        
    }

    /**
     * Expira manualmente una aplicación específica.
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
