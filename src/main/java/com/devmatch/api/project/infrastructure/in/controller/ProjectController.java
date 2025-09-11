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
import com.devmatch.api.project.application.dto.ProjectMemberRoleRequestDto;
import com.devmatch.api.project.application.port.in.ProjectManagementUseCase;
import com.devmatch.api.security.infrastructure.out.adapter.UserPrincipalAdapter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Tag(name = "project-controller", description = "Endpoints para gestión de proyectos y colaboración")
public class ProjectController {

    private final ProjectManagementUseCase projectManagementUseCase;

    @Operation(summary = "Obtener proyecto público", description = "Obtiene los detalles de un proyecto público por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Proyecto obtenido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Proyecto no encontrado o no es público")
    })
    @GetMapping("/public/{projectId}")
    public ResponseEntity<ProjectResponseDto> getPublicProject(
            @Parameter(description = "ID del proyecto", example = "1")
            @PathVariable Long projectId) {
        ProjectResponseDto response = projectManagementUseCase.getPublicProjectById(projectId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener proyectos públicos", description = "Retorna una lista paginada de todos los proyectos públicos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de proyectos públicos obtenida exitosamente")
    })
    @GetMapping("/public")
    public ResponseEntity<Page<ProjectResponseDto>> getPublicProjects(
            @Parameter(description = "Parámetros de paginación y ordenación", example = "page=0&size=10&sort=createdAt,desc")
            Pageable pageable) {
        Page<ProjectResponseDto> projects = projectManagementUseCase.getAllPublicProjects(pageable);
        return ResponseEntity.ok(projects);
    }

    @Operation(summary = "Buscar proyectos públicos", description = "Busca y filtra proyectos públicos con criterios múltiples")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente")
    })
    @PostMapping("/public/search")
    public ResponseEntity<Page<ProjectResponseDto>> searchPublicProjects(
            @Parameter(description = "Criterios de búsqueda y filtros")
            @RequestBody(required = false) ProjectPublicSearchRequestDto filter,
            @Parameter(description = "Parámetros de paginación y ordenación", example = "page=0&size=10&sort=createdAt,desc")
            Pageable pageable) {
        
        // Si no se envía filtro, usar uno vacío para obtener todos los proyectos públicos
        if (filter == null) {
            filter = new ProjectPublicSearchRequestDto();
        }
        
        Page<ProjectResponseDto> projects = projectManagementUseCase.searchPublicProjects(filter, pageable);
        return ResponseEntity.ok(projects);
    }

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
    public ResponseEntity<Page<ProjectResponseDto>> getMyProjects(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            Pageable pageable) {
        
        Page<ProjectResponseDto> projects = projectManagementUseCase.getProjectsByOwner(userPrincipal.getUserId(), pageable);
        return ResponseEntity.ok(projects);
    }

    /**
     * Obtiene proyectos de un usuario específico con filtros y lógica de seguridad
     * Solo devuelve proyectos públicos o propios del usuario autenticado
     */
    @PostMapping("/owner/{ownerId}")
    public ResponseEntity<List<ProjectResponseDto>> getProjectsByOwner(
            @PathVariable Long ownerId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) Integer minTeamSize,
            @RequestParam(required = false) Integer maxTeamSize,
            @RequestParam(required = false) Integer minDurationWeeks,
            @RequestParam(required = false) Integer maxDurationWeeks,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        // Crear DTO de filtros con los parámetros recibidos
        ProjectPublicSearchRequestDto filter = new ProjectPublicSearchRequestDto();
        filter.setTitle(title);
        filter.setStatus(status);
        filter.setIsActive(isActive);
        filter.setMinTeamSize(minTeamSize);
        filter.setMaxTeamSize(maxTeamSize);
        filter.setMinDurationWeeks(minDurationWeeks);
        filter.setMaxDurationWeeks(maxDurationWeeks);
        
        List<ProjectResponseDto> projects = projectManagementUseCase.getProjectsByOwnerWithSecurity(
                ownerId, 
                userPrincipal.getUserId(), 
                filter
        );
        return ResponseEntity.ok(projects);
    }

    @Operation(summary = "Crear nuevo proyecto", description = "Crea un nuevo proyecto en la plataforma")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Proyecto creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<ProjectResponseDto> createProject(
            @Parameter(description = "Datos del nuevo proyecto")
            @RequestBody @Valid ProjectRequestDto request,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        ProjectResponseDto response = projectManagementUseCase.createProject(request, userPrincipal.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Actualizar proyecto", description = "Actualiza un proyecto existente (solo el propietario)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Proyecto actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo el propietario puede actualizar"),
        @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDto> updateProject(
            @Parameter(description = "ID del proyecto a actualizar", example = "1")
            @PathVariable Long projectId,
            @Parameter(description = "Datos actualizados del proyecto")
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
    public ResponseEntity<ProjectResponseDto> deactivateProject(
            @PathVariable Long projectId,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        ProjectResponseDto response = projectManagementUseCase.deactivateProject(projectId, userPrincipal.getUserId());
        return ResponseEntity.ok(response);
    }

    /**
     * Elimina un proyecto (soft delete)
     * Solo propietario del proyecto
     */
    @DeleteMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDto> deleteProject(
            @PathVariable Long projectId,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        ProjectResponseDto response = projectManagementUseCase.deleteProject(projectId, userPrincipal.getUserId());
        return ResponseEntity.ok(response);
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

    /**
     * Remueve un miembro del proyecto
     * Solo propietario del proyecto
     */
    @DeleteMapping("/{projectId}/members/{userId}")
    public ResponseEntity<Void> removeProjectMember(
            @PathVariable Long projectId,
            @PathVariable Long userId,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        projectManagementUseCase.removeProjectMember(projectId, userId, userPrincipal.getUserId());
        return ResponseEntity.ok().build();
    }

    /**
     * Cambia el rol de un miembro del proyecto
     * Solo propietario del proyecto
     */
    @PutMapping("/{projectId}/members/{userId}/role")
    public ResponseEntity<ProjectResponseDto.ProjectMemberDto> changeMemberRole(
            @PathVariable Long projectId,
            @PathVariable Long userId,
            @RequestBody @Valid ProjectMemberRoleRequestDto request,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        ProjectResponseDto.ProjectMemberDto updatedMember = projectManagementUseCase.changeMemberRole(
                projectId, userId, request.getRole(), userPrincipal.getUserId());
        return ResponseEntity.ok(updatedMember);
    }

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
