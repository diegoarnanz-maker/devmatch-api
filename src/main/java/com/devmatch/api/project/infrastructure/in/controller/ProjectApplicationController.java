package com.devmatch.api.project.infrastructure.in.controller;

import com.devmatch.api.project.application.dto.ProjectApplicationRequestDto;
import com.devmatch.api.project.application.dto.ProjectApplicationResponseDto;
import com.devmatch.api.project.application.port.in.ProjectApplicationUseCase;
import com.devmatch.api.security.infrastructure.out.adapter.UserPrincipalAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controlador REST para gestionar las aplicaciones a proyectos.
 * Expone endpoints para que los usuarios puedan aplicar a proyectos
 * y para que los owners puedan gestionar las aplicaciones.
 */
@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectApplicationController {

    private final ProjectApplicationUseCase projectApplicationUseCase;

    /**
     * Permite a un usuario autenticado aplicar a un proyecto.
     * 
     * @param projectId ID del proyecto al que se quiere aplicar
     * @param request DTO con el mensaje de motivación
     * @param userPrincipal Usuario autenticado
     * @return Respuesta HTTP 201 si la aplicación se creó exitosamente
     */
    @PostMapping("/apply/{projectId}")
    public ResponseEntity<Void> applyToProject(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectApplicationRequestDto request,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        projectApplicationUseCase.applyToProject(
                projectId, 
                userPrincipal.getUserId(), 
                request.getMotivationMessage()
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Permite al owner de un proyecto ver todas las aplicaciones recibidas.
     * 
     * @param projectId ID del proyecto
     * @param userPrincipal Usuario autenticado (debe ser el owner)
     * @return Lista de aplicaciones al proyecto
     */
    @GetMapping("/{projectId}/applications")
    public ResponseEntity<List<ProjectApplicationResponseDto>> getProjectApplications(
            @PathVariable Long projectId,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        List<ProjectApplicationResponseDto> applications = 
                projectApplicationUseCase.getProjectApplications(projectId, userPrincipal.getUserId());
        
        return ResponseEntity.ok(applications);
    }

    /**
     * Permite al owner de un proyecto aceptar una aplicación específica.
     * 
     * @param projectId ID del proyecto
     * @param applicationId ID de la aplicación a aceptar
     * @param userPrincipal Usuario autenticado (debe ser el owner)
     * @return Respuesta HTTP 200 si la aplicación se aceptó exitosamente
     */
    @PutMapping("/{projectId}/applications/{applicationId}/accept")
    public ResponseEntity<Void> acceptApplication(
            @PathVariable Long projectId,
            @PathVariable Long applicationId,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        projectApplicationUseCase.acceptApplication(projectId, applicationId, userPrincipal.getUserId());
        
        return ResponseEntity.ok().build();
    }

    /**
     * Permite al owner de un proyecto rechazar una aplicación específica.
     * 
     * @param projectId ID del proyecto
     * @param applicationId ID de la aplicación a rechazar
     * @param userPrincipal Usuario autenticado (debe ser el owner)
     * @return Respuesta HTTP 200 si la aplicación se rechazó exitosamente
     */
    @PutMapping("/{projectId}/applications/{applicationId}/reject")
    public ResponseEntity<Void> rejectApplication(
            @PathVariable Long projectId,
            @PathVariable Long applicationId,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        projectApplicationUseCase.rejectApplication(projectId, applicationId, userPrincipal.getUserId());
        
        return ResponseEntity.ok().build();
    }
} 