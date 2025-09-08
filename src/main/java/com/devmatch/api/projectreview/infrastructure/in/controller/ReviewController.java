package com.devmatch.api.projectreview.infrastructure.in.controller;

import com.devmatch.api.projectreview.application.dto.ReviewRequestDto;
import com.devmatch.api.projectreview.application.dto.ReviewResponseDto;
import com.devmatch.api.projectreview.application.dto.ReviewResponseRequestDto;
import com.devmatch.api.projectreview.application.port.in.ReviewManagementUseCase;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

/**
 * Controlador para operaciones de reseñas de proyectos.
 * 
 * Este controlador expone endpoints para que los usuarios puedan gestionar reseñas.
 */
@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Tag(name = "review-controller", description = "Endpoints para gestión de reseñas de proyectos")
@SecurityRequirement(name = "bearerAuth")
public class ReviewController {

    private final ReviewManagementUseCase reviewManagementUseCase;

    @Operation(summary = "Obtener reseñas de proyecto", description = "Obtiene todas las reseñas de un proyecto específico de forma paginada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de reseñas obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    })
    @GetMapping("/project/{projectId}")
    public ResponseEntity<Page<ReviewResponseDto>> getReviewsByProject(
            @Parameter(description = "ID del proyecto", example = "1")
            @PathVariable Long projectId,
            @Parameter(description = "Parámetros de paginación y ordenación", example = "page=0&size=10&sort=createdAt,desc")
            Pageable pageable) {
        Page<ReviewResponseDto> reviews = reviewManagementUseCase.getReviewsByProject(projectId, pageable);
        return ResponseEntity.ok(reviews);
    }

    @Operation(summary = "Obtener reseña por ID", description = "Obtiene los detalles de una reseña específica por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reseña obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> getReviewById(
            @Parameter(description = "ID de la reseña", example = "1")
            @PathVariable Long reviewId) {
        ReviewResponseDto review = reviewManagementUseCase.getReviewById(reviewId);
        return ResponseEntity.ok(review);
    }

    @Operation(summary = "Crear reseña", description = "Crea una nueva reseña para un proyecto (solo proyectos completados)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reseña creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo se permiten reseñas en proyectos completados"),
        @ApiResponse(responseCode = "404", description = "Proyecto no encontrado"),
        @ApiResponse(responseCode = "409", description = "Ya has reseñado este proyecto")
    })
    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @Parameter(description = "Datos de la reseña")
            @Valid @RequestBody ReviewRequestDto request) {
        ReviewResponseDto createdReview = reviewManagementUseCase.createReview(userPrincipal.getUserId(), request);
        

        
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    @Operation(summary = "Actualizar reseña", description = "Actualiza una reseña existente (solo el autor)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reseña actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo el autor puede actualizar la reseña"),
        @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @Parameter(description = "ID de la reseña a actualizar", example = "1")
            @PathVariable Long reviewId,
            @Parameter(description = "Datos actualizados de la reseña")
            @Valid @RequestBody ReviewRequestDto request) {
        ReviewResponseDto updatedReview = reviewManagementUseCase.updateReview(userPrincipal.getUserId(), reviewId, request);
        return ResponseEntity.ok(updatedReview);
    }

    @Operation(summary = "Eliminar reseña", description = "Elimina una reseña del sistema (solo el autor)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Reseña eliminada exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo el autor puede eliminar la reseña"),
        @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @Parameter(description = "ID de la reseña a eliminar", example = "1")
            @PathVariable Long reviewId) {
        reviewManagementUseCase.deleteReview(userPrincipal.getUserId(), reviewId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener mis reseñas", description = "Obtiene todas las reseñas del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de reseñas del usuario obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado")
    })
    @GetMapping("/my-reviews")
    public ResponseEntity<Page<ReviewResponseDto>> getMyReviews(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @Parameter(description = "Parámetros de paginación y ordenación", example = "page=0&size=10&sort=createdAt,desc")
            Pageable pageable) {
        Page<ReviewResponseDto> reviews = reviewManagementUseCase.getMyReviews(userPrincipal.getUserId(), pageable);
        return ResponseEntity.ok(reviews);
    }
    
    @Operation(summary = "Responder a reseña", description = "Permite al propietario del proyecto responder a una reseña")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Respuesta agregada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo el propietario del proyecto puede responder"),
        @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @PostMapping("/{reviewId}/response")
    public ResponseEntity<ReviewResponseDto> respondToReview(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @Parameter(description = "ID de la reseña a responder", example = "1")
            @PathVariable Long reviewId,
            @Parameter(description = "Mensaje de respuesta del propietario")
            @Valid @RequestBody ReviewResponseRequestDto request) {
        ReviewResponseDto updatedReview = reviewManagementUseCase.respondToReview(userPrincipal.getUserId(), reviewId, request);
        return ResponseEntity.ok(updatedReview);
    }
} 