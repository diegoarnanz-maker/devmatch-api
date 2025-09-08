package com.devmatch.api.projectreview.infrastructure.in.controller;

import com.devmatch.api.projectreview.application.dto.ReviewResponseDto;
import com.devmatch.api.projectreview.application.port.in.AdminReviewManagementUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/v1/admin/reviews")
@Tag(name = "admin-review-controller", description = "Endpoints administrativos para gestión de reseñas")
@SecurityRequirement(name = "bearerAuth")
public class AdminReviewController {

    private final AdminReviewManagementUseCase reviewService;


    @Operation(summary = "Obtener todas las reseñas", description = "Obtiene todas las reseñas del sistema, opcionalmente filtradas por proyecto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de reseñas obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo administradores pueden acceder")
    })
    @GetMapping
    public ResponseEntity<Page<ReviewResponseDto>> getAllReviews(
            @Parameter(description = "ID del proyecto para filtrar (opcional)", example = "1")
            @RequestParam(value = "projectId", required = false) Long projectId,
            @Parameter(description = "Parámetros de paginación y ordenación", example = "page=0&size=20&sort=createdAt,desc")
            Pageable pageable) {
        Page<ReviewResponseDto> page = reviewService.getAllReviews(projectId, pageable);
        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Eliminar reseña (Admin)", description = "Elimina una reseña por su ID (solo administradores)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Reseña eliminada exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT inválido o expirado"),
        @ApiResponse(responseCode = "403", description = "Prohibido - Solo administradores pueden eliminar"),
        @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "ID de la reseña a eliminar", example = "1")
            @PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
} 