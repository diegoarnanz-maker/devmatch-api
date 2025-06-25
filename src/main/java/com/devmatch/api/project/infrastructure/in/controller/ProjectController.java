package com.devmatch.api.project.infrastructure.in.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devmatch.api.project.application.dto.ProjectRequestDto;
import com.devmatch.api.project.application.dto.ProjectResponseDto;
import com.devmatch.api.project.application.port.in.ProjectManagementUseCase;
import com.devmatch.api.project.domain.model.valueobject.ProjectStatus;
import com.devmatch.api.security.infrastructure.out.adapter.UserPrincipalAdapter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectManagementUseCase projectManagementUseCase;

    // ===== ENDPOINTS PÚBLICOS (sin autenticación) =====

    /**
     * Obtiene un proyecto público específico por su ID
     * Accesible sin autenticación
     */
    @GetMapping("/public/{projectId}")
    public ResponseEntity<ProjectResponseDto> getPublicProject(@PathVariable Long projectId) {
        ProjectResponseDto response = projectManagementUseCase.getPublicProjectById(projectId);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene lista de proyectos públicos
     * Accesible sin autenticación
     */
    // @GetMapping("/public")
    // public ResponseEntity<List<ProjectResponseDto>> getPublicProjects(
    //         @RequestParam(defaultValue = "0") int page,
    //         @RequestParam(defaultValue = "20") int size) {
    //     // TODO: Implementar ProjectQueryUseCase para búsquedas públicas
    //     // Por ahora retorna lista vacía
    //     return ResponseEntity.ok(List.of());
    // }

    // ===== ENDPOINTS PRIVADOS (con autenticación) =====

    /**
     * Obtiene un proyecto específico por su ID
     * Accesible para proyectos públicos + propios del usuario
     */
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDto> getProject(
            @PathVariable Long projectId,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        ProjectResponseDto response = projectManagementUseCase.getProjectById(projectId, userPrincipal.getUserId());
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene todos los proyectos del usuario autenticado
     */
    @GetMapping("/my-projects")
    public ResponseEntity<List<ProjectResponseDto>> getMyProjects(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        List<ProjectResponseDto> projects = projectManagementUseCase.getProjectsByOwner(userPrincipal.getUserId());
        return ResponseEntity.ok(projects);
    }

    // ===== ENDPOINTS DE PROPIETARIO =====

    /**
     * Crea un nuevo proyecto
     * Solo usuarios autenticados
     */
    @PostMapping
    public ResponseEntity<ProjectResponseDto> createProject(
            @RequestBody @Valid ProjectRequestDto request,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        ProjectResponseDto response = projectManagementUseCase.createProject(request, userPrincipal.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Actualiza un proyecto existente
     * Solo propietario del proyecto
     */
    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDto> updateProject(
            @PathVariable Long projectId,
            @RequestBody @Valid ProjectRequestDto request,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        ProjectResponseDto response = projectManagementUseCase.updateProject(projectId, request, userPrincipal.getUserId());
        return ResponseEntity.ok(response);
    }

    /**
     * Cambia el estado de un proyecto
     * Solo propietario del proyecto
     */
    @PutMapping("/status/{projectId}")
    public ResponseEntity<ProjectResponseDto> changeProjectStatus(
            @PathVariable Long projectId,
            @RequestBody ProjectStatus newStatus,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        ProjectResponseDto response = projectManagementUseCase.changeProjectStatus(projectId, newStatus, userPrincipal.getUserId());
        return ResponseEntity.ok(response);
    }

    /**
     * Desactiva un proyecto (soft deactivate)
     * Solo propietario del proyecto
     */
    @PutMapping("/deactivate/{projectId}")
    public ResponseEntity<Void> deactivateProject(
            @PathVariable Long projectId,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        projectManagementUseCase.deactivateProject(projectId, userPrincipal.getUserId());
        return ResponseEntity.noContent().build();
    }

    /**
     * Elimina un proyecto (soft delete)
     * Solo propietario del proyecto
     */
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable Long projectId,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        projectManagementUseCase.deleteProject(projectId, userPrincipal.getUserId());
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene todos los proyectos de un usuario específico
     * Solo proyectos públicos o propios del usuario autenticado
     */
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<ProjectResponseDto>> getProjectsByOwner(
            @PathVariable Long ownerId,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        // TODO: Implementar lógica para mostrar solo proyectos públicos
        // o proyectos del usuario autenticado
        List<ProjectResponseDto> projects = projectManagementUseCase.getProjectsByOwner(ownerId);
        return ResponseEntity.ok(projects);
    }
}
