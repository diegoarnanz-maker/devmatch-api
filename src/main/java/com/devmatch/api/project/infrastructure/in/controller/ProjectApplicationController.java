package com.devmatch.api.project.infrastructure.in.controller;

import com.devmatch.api.project.application.dto.ProjectApplicationRequestDto;
import com.devmatch.api.project.application.dto.ProjectApplicationResponseDto;
import com.devmatch.api.project.application.port.in.ProjectApplicationUseCase;
import com.devmatch.api.security.infrastructure.out.adapter.UserPrincipalAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/v1/project-applications")
@RequiredArgsConstructor
@Tag(name = "project-application-controller", description = "Endpoints para gestionar aplicaciones a proyectos")
@SecurityRequirement(name = "bearerAuth")
public class ProjectApplicationController {

    private final ProjectApplicationUseCase projectApplicationUseCase;

    @Operation(summary = "Aplicar a proyecto", description = "Permite a un usuario autenticado aplicar a un proyecto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Aplicación enviada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "404", description = "Proyecto no encontrado"),
        @ApiResponse(responseCode = "409", description = "Ya has aplicado a este proyecto")
    })
    @PostMapping("/apply/{projectId}")
    public ResponseEntity<Void> applyToProject(
            @Parameter(description = "ID del proyecto al que se quiere aplicar", example = "1")
            @PathVariable Long projectId,
            @Parameter(description = "Datos de la aplicación")
            @Valid @RequestBody ProjectApplicationRequestDto request,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        projectApplicationUseCase.applyToProject(
                projectId, 
                userPrincipal.getUserId(), 
                request.getMotivationMessage()
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Obtener aplicaciones de proyecto", description = "Permite al owner de un proyecto ver todas las aplicaciones recibidas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de aplicaciones obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo el owner puede ver las aplicaciones"),
        @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    })
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<ProjectApplicationResponseDto>> getProjectApplications(
            @Parameter(description = "ID del proyecto", example = "1")
            @PathVariable Long projectId,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        List<ProjectApplicationResponseDto> applications = 
                projectApplicationUseCase.getProjectApplications(projectId, userPrincipal.getUserId());
        
        return ResponseEntity.ok(applications);
    }

    @Operation(summary = "Obtener mis aplicaciones", description = "Permite a un usuario ver todas sus candidaturas a proyectos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de candidaturas obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado")
    })
    @GetMapping("/applications/my")
    public ResponseEntity<List<ProjectApplicationResponseDto>> getMyApplications(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        List<ProjectApplicationResponseDto> applications = 
                projectApplicationUseCase.getUserApplications(userPrincipal.getUserId());
        
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

    /**
     * Permite a un usuario cancelar su propia aplicación a un proyecto.
     * Solo se puede cancelar si la aplicación está pendiente.
     * 
     * @param applicationId ID de la aplicación a cancelar
     * @param userPrincipal Usuario autenticado (debe ser el que aplicó)
     * @return Respuesta HTTP 200 si la aplicación se canceló exitosamente
     */
    @DeleteMapping("/applications/{applicationId}/cancel")
    public ResponseEntity<Void> cancelApplication(
            @PathVariable Long applicationId,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        projectApplicationUseCase.cancelApplication(applicationId, userPrincipal.getUserId());
        
        return ResponseEntity.ok().build();
    }
} 