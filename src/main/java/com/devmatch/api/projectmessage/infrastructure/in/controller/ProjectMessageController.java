package com.devmatch.api.projectmessage.infrastructure.in.controller;

import com.devmatch.api.projectmessage.application.dto.ProjectMessageRequestDto;
import com.devmatch.api.projectmessage.application.dto.ProjectMessageResponseDto;
import com.devmatch.api.projectmessage.application.dto.ProjectMessageSearchRequestDto;
import com.devmatch.api.projectmessage.application.dto.ProjectMessageUpdateRequestDto;
import com.devmatch.api.projectmessage.application.port.in.ProjectMessageManagementUseCase;
import com.devmatch.api.security.infrastructure.out.adapter.UserPrincipalAdapter;
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
public class ProjectMessageController {
    
    private final ProjectMessageManagementUseCase projectMessageManagementUseCase;

    /**
     * Obtiene todos los mensajes de un proyecto de forma paginada
     */
    @GetMapping("/{projectId}/messages")
    public ResponseEntity<Page<ProjectMessageResponseDto>> getProjectMessages(
            @PathVariable Long projectId,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            Pageable pageable) {
        
        Page<ProjectMessageResponseDto> response = projectMessageManagementUseCase.getProjectMessages(
            projectId, userPrincipal.getUserId(), pageable);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtiene un mensaje específico por su ID
     */
    @GetMapping("/{projectId}/messages/{messageId}")
    public ResponseEntity<ProjectMessageResponseDto> getMessageById(
            @PathVariable Long projectId,
            @PathVariable Long messageId,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        ProjectMessageResponseDto response = projectMessageManagementUseCase.getMessageById(
            messageId, userPrincipal.getUserId());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtiene los mensajes no leídos de un usuario en un proyecto
     */
    @GetMapping("/{projectId}/messages/unread")
    public ResponseEntity<List<ProjectMessageResponseDto>> getUnreadMessages(
            @PathVariable Long projectId,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        List<ProjectMessageResponseDto> response = projectMessageManagementUseCase.getUnreadMessages(
            projectId, userPrincipal.getUserId());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtiene el historial de mensajes de un hilo de conversación
     */
    @GetMapping("/{projectId}/messages/{messageId}/thread")
    public ResponseEntity<List<ProjectMessageResponseDto>> getMessageThread(
            @PathVariable Long projectId,
            @PathVariable Long messageId,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        List<ProjectMessageResponseDto> response = projectMessageManagementUseCase.getMessageThread(
            messageId, userPrincipal.getUserId());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Envía un nuevo mensaje en un proyecto
     */
    @PostMapping("/{projectId}/messages")
    public ResponseEntity<ProjectMessageResponseDto> sendMessage(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectMessageRequestDto request,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        log.info("Usuario {} enviando mensaje en proyecto {}", userPrincipal.getUserId(), projectId);
        
        // Asegurar que el projectId del path coincida con el del request
        request.setProjectId(projectId);
        
        ProjectMessageResponseDto response = projectMessageManagementUseCase.sendMessage(
            userPrincipal.getUserId(), request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Busca mensajes con criterios específicos
     */
    @PostMapping("/{projectId}/messages/search")
    public ResponseEntity<Page<ProjectMessageResponseDto>> searchMessages(
            @PathVariable Long projectId,
            @RequestBody ProjectMessageSearchRequestDto searchRequest,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            Pageable pageable) {
        
        // Asegurar que la búsqueda se limite al proyecto especificado
        searchRequest.setProjectId(projectId);
        
        Page<ProjectMessageResponseDto> response = projectMessageManagementUseCase.searchMessages(
            searchRequest, userPrincipal.getUserId(), pageable);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Edita el contenido de un mensaje existente
     */
    @PutMapping("/{projectId}/messages/{messageId}")
    public ResponseEntity<ProjectMessageResponseDto> editMessage(
            @PathVariable Long projectId,
            @PathVariable Long messageId,
            @Valid @RequestBody ProjectMessageUpdateRequestDto request,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        log.info("Usuario {} editando mensaje {} en proyecto {}", 
                userPrincipal.getUserId(), messageId, projectId);
        
        ProjectMessageResponseDto response = projectMessageManagementUseCase.editMessage(
            userPrincipal.getUserId(), messageId, request);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Marca un mensaje como leído
     */
    @PatchMapping("/{projectId}/messages/{messageId}/read")
    public ResponseEntity<ProjectMessageResponseDto> markMessageAsRead(
            @PathVariable Long projectId,
            @PathVariable Long messageId,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        log.info("Usuario {} marcando mensaje {} como leído en proyecto {}", 
                userPrincipal.getUserId(), messageId, projectId);
        
        ProjectMessageResponseDto response = projectMessageManagementUseCase.markMessageAsRead(
            userPrincipal.getUserId(), messageId);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Marca todos los mensajes de un proyecto como leídos para un usuario
     */
    @PatchMapping("/{projectId}/messages/read-all")
    public ResponseEntity<Void> markAllMessagesAsRead(
            @PathVariable Long projectId,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        log.info("Usuario {} marcando todos los mensajes como leídos en proyecto {}", 
                userPrincipal.getUserId(), projectId);
        
        projectMessageManagementUseCase.markAllMessagesAsRead(projectId, userPrincipal.getUserId());
        
        return ResponseEntity.ok().build();
    }
    
    /**
     * Elimina un mensaje (soft delete)
     */
    @DeleteMapping("/{projectId}/messages/{messageId}")
    public ResponseEntity<Void> deleteMessage(
            @PathVariable Long projectId,
            @PathVariable Long messageId,
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal) {
        
        log.info("Usuario {} eliminando mensaje {} en proyecto {}", 
                userPrincipal.getUserId(), messageId, projectId);
        
        projectMessageManagementUseCase.deleteMessage(userPrincipal.getUserId(), messageId);
        
        return ResponseEntity.noContent().build();
    }
}
