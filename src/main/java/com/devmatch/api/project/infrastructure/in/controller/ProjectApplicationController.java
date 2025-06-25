package com.devmatch.api.project.infrastructure.in.controller;

import com.devmatch.api.project.application.dto.ProjectApplicationRequestDto;
import com.devmatch.api.project.application.port.in.ProjectApplicationUseCase;
import com.devmatch.api.security.infrastructure.out.adapter.UserPrincipalAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Controlador REST para gestionar las aplicaciones a proyectos.
 * Expone endpoints para que los usuarios puedan aplicar a proyectos.
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
} 