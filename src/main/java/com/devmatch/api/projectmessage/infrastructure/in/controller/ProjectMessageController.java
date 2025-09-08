package com.devmatch.api.projectmessage.infrastructure.in.controller;

import com.devmatch.api.projectmessage.application.dto.ProjectMessageRequestDto;
import com.devmatch.api.projectmessage.application.dto.ProjectMessageResponseDto;
import com.devmatch.api.projectmessage.application.dto.ProjectMessageSearchRequestDto;
import com.devmatch.api.projectmessage.application.dto.ProjectMessageUpdateRequestDto;
import com.devmatch.api.projectmessage.application.port.in.ProjectMessageManagementUseCase;
import com.devmatch.api.security.infrastructure.out.adapter.UserPrincipalAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de mensajes de proyecto.
 * Permite a los usuarios enviar, editar, eliminar y consultar mensajes en proyectos.
 */
@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "project-message-controller", description = "Endpoints para gestión de mensajes en proyectos")
@SecurityRequirement(name = "bearerAuth")
public class ProjectMessageController {
    
    private final ProjectMessageManagementUseCase projectMessageManagementUseCase;

    @Operation(summary = "Obtener mensajes del proyecto", description = "Obtiene todos los mensajes de un proyecto de forma paginada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de mensajes obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - No tienes acceso a este proyecto"),
        @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    })
    @GetMapping("/{projectId}/messages")
    public ResponseEntity<Page<ProjectMessageResponseDto>> getProjectMessages(
            @Parameter(description = "ID del proyecto", example = "1")
            @PathVariable Long projectId,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @Parameter(description = "Parámetros de paginación y ordenación", example = "page=0&size=20&sort=sentAt,desc")
            Pageable pageable) {
        
        Page<ProjectMessageResponseDto> response = projectMessageManagementUseCase.getProjectMessages(
            projectId, userPrincipal.getUserId(), pageable);
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Obtener mensaje por ID", description = "Obtiene un mensaje específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mensaje obtenido exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - No tienes acceso a este mensaje"),
        @ApiResponse(responseCode = "404", description = "Mensaje no encontrado")
    })
    @GetMapping("/{projectId}/messages/{messageId}")
    public ResponseEntity<ProjectMessageResponseDto> getMessageById(
            @Parameter(description = "ID del proyecto", example = "1")
            @PathVariable Long projectId,
            @Parameter(description = "ID del mensaje", example = "5")
            @PathVariable Long messageId,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        ProjectMessageResponseDto response = projectMessageManagementUseCase.getMessageById(
            messageId, userPrincipal.getUserId());
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Obtener mensajes no leídos", description = "Obtiene los mensajes no leídos de un usuario en un proyecto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de mensajes no leídos obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - No tienes acceso a este proyecto"),
        @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    })
    @GetMapping("/{projectId}/messages/unread")
    public ResponseEntity<List<ProjectMessageResponseDto>> getUnreadMessages(
            @Parameter(description = "ID del proyecto", example = "1")
            @PathVariable Long projectId,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        List<ProjectMessageResponseDto> response = projectMessageManagementUseCase.getUnreadMessages(
            projectId, userPrincipal.getUserId());
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Obtener hilo de conversación", description = "Obtiene el historial completo de mensajes de un hilo de conversación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Hilo de conversación obtenido exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - No tienes acceso a este mensaje"),
        @ApiResponse(responseCode = "404", description = "Mensaje no encontrado")
    })
    @GetMapping("/{projectId}/messages/{messageId}/thread")
    public ResponseEntity<List<ProjectMessageResponseDto>> getMessageThread(
            @Parameter(description = "ID del proyecto", example = "1")
            @PathVariable Long projectId,
            @Parameter(description = "ID del mensaje raíz del hilo", example = "5")
            @PathVariable Long messageId,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        List<ProjectMessageResponseDto> response = projectMessageManagementUseCase.getMessageThread(
            messageId, userPrincipal.getUserId());
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Enviar mensaje", description = "Envía un nuevo mensaje en un proyecto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Mensaje enviado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - No tienes acceso a este proyecto"),
        @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    })
    @PostMapping("/{projectId}/messages")
    public ResponseEntity<ProjectMessageResponseDto> sendMessage(
            @Parameter(description = "ID del proyecto", example = "1")
            @PathVariable Long projectId,
            @Parameter(description = "Datos del mensaje a enviar")
            @Valid @RequestBody ProjectMessageRequestDto request,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        
        // Asegurar que el projectId del path coincida con el del request
        request.setProjectId(projectId);
        
        ProjectMessageResponseDto response = projectMessageManagementUseCase.sendMessage(
            userPrincipal.getUserId(), request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Operation(summary = "Responder a mensaje", description = "Responde a un mensaje específico en un proyecto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Respuesta enviada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - No tienes acceso a este proyecto"),
        @ApiResponse(responseCode = "404", description = "Proyecto o mensaje no encontrado")
    })
    @PostMapping("/{projectId}/messages/{messageId}/reply")
    public ResponseEntity<ProjectMessageResponseDto> replyToMessage(
            @Parameter(description = "ID del proyecto", example = "1")
            @PathVariable Long projectId,
            @Parameter(description = "ID del mensaje al que responder", example = "5")
            @PathVariable Long messageId,
            @Parameter(description = "Datos de la respuesta")
            @Valid @RequestBody ProjectMessageRequestDto request,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        // Asegurar que el projectId del path coincida con el del request
        request.setProjectId(projectId);
        
        ProjectMessageResponseDto response = projectMessageManagementUseCase.replyToMessage(
            userPrincipal.getUserId(), messageId, request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Operation(summary = "Buscar mensajes", description = "Busca mensajes con criterios específicos en un proyecto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Criterios de búsqueda inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - No tienes acceso a este proyecto"),
        @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    })
    @PostMapping("/{projectId}/messages/search")
    public ResponseEntity<Page<ProjectMessageResponseDto>> searchMessages(
            @Parameter(description = "ID del proyecto", example = "1")
            @PathVariable Long projectId,
            @Parameter(description = "Criterios de búsqueda")
            @RequestBody ProjectMessageSearchRequestDto searchRequest,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @Parameter(description = "Parámetros de paginación y ordenación", example = "page=0&size=20&sort=sentAt,desc")
            Pageable pageable) {
        
        // Asegurar que la búsqueda se limite al proyecto especificado
        searchRequest.setProjectId(projectId);
        
        Page<ProjectMessageResponseDto> response = projectMessageManagementUseCase.searchMessages(
            searchRequest, userPrincipal.getUserId(), pageable);
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Editar mensaje", description = "Edita el contenido de un mensaje existente (solo el autor)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mensaje editado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo el autor puede editar el mensaje"),
        @ApiResponse(responseCode = "404", description = "Mensaje no encontrado")
    })
    @PutMapping("/{projectId}/messages/{messageId}")
    public ResponseEntity<ProjectMessageResponseDto> editMessage(
            @Parameter(description = "ID del proyecto", example = "1")
            @PathVariable Long projectId,
            @Parameter(description = "ID del mensaje a editar", example = "5")
            @PathVariable Long messageId,
            @Parameter(description = "Datos actualizados del mensaje")
            @Valid @RequestBody ProjectMessageUpdateRequestDto request,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        
        ProjectMessageResponseDto response = projectMessageManagementUseCase.editMessage(
            userPrincipal.getUserId(), messageId, request);
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Marcar mensaje como leído", description = "Marca un mensaje específico como leído")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mensaje marcado como leído exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - No tienes acceso a este mensaje"),
        @ApiResponse(responseCode = "404", description = "Mensaje no encontrado")
    })
    @PatchMapping("/{projectId}/messages/{messageId}/read")
    public ResponseEntity<ProjectMessageResponseDto> markMessageAsRead(
            @Parameter(description = "ID del proyecto", example = "1")
            @PathVariable Long projectId,
            @Parameter(description = "ID del mensaje a marcar como leído", example = "5")
            @PathVariable Long messageId,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        
        ProjectMessageResponseDto response = projectMessageManagementUseCase.markMessageAsRead(
            userPrincipal.getUserId(), messageId);
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Marcar todos los mensajes como leídos", description = "Marca todos los mensajes de un proyecto como leídos para un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Todos los mensajes marcados como leídos exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - No tienes acceso a este proyecto"),
        @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    })
    @PatchMapping("/{projectId}/messages/read-all")
    public ResponseEntity<Void> markAllMessagesAsRead(
            @Parameter(description = "ID del proyecto", example = "1")
            @PathVariable Long projectId,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        
        projectMessageManagementUseCase.markAllMessagesAsRead(projectId, userPrincipal.getUserId());
        
        return ResponseEntity.ok().build();
    }
    
    @Operation(summary = "Eliminar mensaje", description = "Elimina un mensaje (soft delete, solo el autor)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Mensaje eliminado exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo el autor puede eliminar el mensaje"),
        @ApiResponse(responseCode = "404", description = "Mensaje no encontrado")
    })
    @DeleteMapping("/{projectId}/messages/{messageId}")
    public ResponseEntity<Void> deleteMessage(
            @Parameter(description = "ID del proyecto", example = "1")
            @PathVariable Long projectId,
            @Parameter(description = "ID del mensaje a eliminar", example = "5")
            @PathVariable Long messageId,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        
        projectMessageManagementUseCase.deleteMessage(userPrincipal.getUserId(), messageId);
        
        return ResponseEntity.noContent().build();
    }
}
