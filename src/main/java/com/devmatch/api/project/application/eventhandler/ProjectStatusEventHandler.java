package com.devmatch.api.project.application.eventhandler;

import com.devmatch.api.project.domain.event.ProjectMemberJoinedEvent;
import com.devmatch.api.project.domain.model.Project;
import com.devmatch.api.project.domain.model.valueobject.ProjectStatus;
import com.devmatch.api.project.application.port.out.ProjectRepositoryPort;
import com.devmatch.api.project.application.port.out.ProjectMemberRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Event handler para manejar cambios de estado de proyectos.
 * Escucha eventos relacionados con miembros del proyecto y actualiza el estado según corresponda.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectStatusEventHandler {

    private final ProjectRepositoryPort projectRepositoryPort;
    private final ProjectMemberRepositoryPort projectMemberRepositoryPort;

    /**
     * Maneja el evento de nuevo miembro unido al proyecto.
     * Verifica si el proyecto debe cambiar a COMPLETED cuando el equipo esté completo.
     */
    @EventListener
    @Transactional
    public void handleProjectMemberJoined(ProjectMemberJoinedEvent event) {
        log.info("Verificando si el proyecto {} debe cambiar a COMPLETED tras unirse el miembro {}", 
                event.getProjectId(), event.getNewMemberId());

        try {
            // 1. Obtener el proyecto
            Project project = projectRepositoryPort.findById(event.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + event.getProjectId()));

            // 2. Solo procesar si el proyecto está en estado OPEN
            if (project.getStatus() != ProjectStatus.OPEN) {
                log.debug("Proyecto {} no está en estado OPEN (actual: {}), no se cambia el estado", 
                        event.getProjectId(), project.getStatus());
                return;
            }

            // 3. Contar miembros activos actuales
            int currentTeamSize = projectMemberRepositoryPort.countActiveMembersByProjectId(event.getProjectId());
            log.debug("Proyecto {} tiene {} miembros activos de un máximo de {}", 
                    event.getProjectId(), currentTeamSize, project.getMaxTeamSize().getValue());

            // 4. Verificar si el proyecto está completo
            if (project.isFull(currentTeamSize)) {
                log.info("Proyecto {} está completo ({} miembros), cambiando estado a COMPLETED", 
                        event.getProjectId(), currentTeamSize);

                // 5. Actualizar el estado del proyecto a COMPLETED
                Project updatedProject = project.updateStatus(ProjectStatus.COMPLETED);
                projectRepositoryPort.save(updatedProject);

                log.info("Proyecto {} actualizado exitosamente a estado COMPLETED", event.getProjectId());
            } else {
                log.debug("Proyecto {} aún no está completo ({} de {} miembros), manteniendo estado OPEN", 
                        event.getProjectId(), currentTeamSize, project.getMaxTeamSize().getValue());
            }

        } catch (Exception e) {
            // Log de error - si algo falla, no rompe el flujo principal
            log.error("Error al verificar cambio de estado del proyecto {} tras unirse miembro {}", 
                    event.getProjectId(), event.getNewMemberId(), e);
        }
    }
}
