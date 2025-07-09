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
import com.devmatch.api.project.application.dto.ProjectPublicSearchRequestDto;
import com.devmatch.api.project.application.dto.ProjectTagsRequestDto;
import com.devmatch.api.project.application.dto.ProjectStatusRequestDto;
import com.devmatch.api.project.application.dto.ProjectVisibilityRequestDto;
import com.devmatch.api.project.application.port.in.ProjectManagementUseCase;
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
     * Obtiene todos los proyectos públicos
     * Accesible sin autenticación
     */
    @GetMapping("/public")
    public ResponseEntity<List<ProjectResponseDto>> getPublicProjects() {
        List<ProjectResponseDto> projects = projectManagementUseCase.getAllPublicProjects();
        return ResponseEntity.ok(projects);
    }

    /**
     * Busca y filtra proyectos públicos con criterios múltiples
     * Accesible sin autenticación
     * Permite filtrar por título, estado, tags, propietario, etc.
     */
    @PostMapping("/public/search")
    public ResponseEntity<List<ProjectResponseDto>> searchPublicProjects(
            @RequestBody(required = false) ProjectPublicSearchRequestDto filter) {
        
        // Si no se envía filtro, usar uno vacío para obtener todos los proyectos públicos
        if (filter == null) {
            filter = new ProjectPublicSearchRequestDto();
        }
        
        List<ProjectResponseDto> projects = projectManagementUseCase.searchPublicProjects(filter);
        return ResponseEntity.ok(projects);
    }

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
    @PutMapping("/{projectId}/status")
    public ResponseEntity<ProjectResponseDto> changeProjectStatus(
            @PathVariable Long projectId,
            @RequestBody @Valid ProjectStatusRequestDto request,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        ProjectResponseDto response = projectManagementUseCase.changeProjectStatus(projectId, request.getStatus(), userPrincipal.getUserId());
        return ResponseEntity.ok(response);
    }

    /**
     * Cambia la visibilidad pública de un proyecto
     * Solo propietario del proyecto
     */
    @PutMapping("/{projectId}/visibility")
    public ResponseEntity<ProjectResponseDto> changeProjectVisibility(
            @PathVariable Long projectId,
            @RequestBody @Valid ProjectVisibilityRequestDto request,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        ProjectResponseDto response = projectManagementUseCase.changeProjectVisibility(projectId, request.isPublic(), userPrincipal.getUserId());
        return ResponseEntity.ok(response);
    }

    /**
     * Desactiva un proyecto (soft deactivate)
     * Solo propietario del proyecto
     */
    @PutMapping("/{projectId}/deactivate")
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
     * Restaura un proyecto (desactivado o eliminado)
     * Solo propietario del proyecto
     */
    @PutMapping("/{projectId}/restore")
    public ResponseEntity<ProjectResponseDto> restoreProject(
            @PathVariable Long projectId,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        ProjectResponseDto response = projectManagementUseCase.restoreProject(projectId, userPrincipal.getUserId());
        return ResponseEntity.ok(response);
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

    /**
     * Obtiene los miembros de un proyecto
     * Solo propietario del proyecto
     */
    @GetMapping("/{projectId}/members")
    public ResponseEntity<List<ProjectResponseDto.ProjectMemberDto>> getProjectMembers(
            @PathVariable Long projectId,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        List<ProjectResponseDto.ProjectMemberDto> members = projectManagementUseCase.getProjectMembers(projectId, userPrincipal.getUserId());
        return ResponseEntity.ok(members);
    }

    // ===== ENDPOINTS DE GESTIÓN DE TAGS =====

    /**
     * Agrega tags a un proyecto
     * Solo propietario del proyecto
     */
    @PostMapping("/{projectId}/tags")
    public ResponseEntity<ProjectResponseDto> addTagsToProject(
            @PathVariable Long projectId,
            @RequestBody @Valid ProjectTagsRequestDto request,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        ProjectResponseDto response = projectManagementUseCase.addTagsToProject(projectId, request, userPrincipal.getUserId());
        return ResponseEntity.ok(response);
    }

    /**
     * Remueve un tag específico de un proyecto
     * Solo propietario del proyecto
     */
    @DeleteMapping("/{projectId}/tags/{tagName}")
    public ResponseEntity<ProjectResponseDto> removeTagFromProject(
            @PathVariable Long projectId,
            @PathVariable String tagName,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        ProjectResponseDto response = projectManagementUseCase.removeTagFromProject(projectId, tagName, userPrincipal.getUserId());
        return ResponseEntity.ok(response);
    }
}
