package com.devmatch.api.projectreview.infrastructure.in.controller;

import com.devmatch.api.projectreview.application.dto.ReviewResponseDto;
import com.devmatch.api.projectreview.application.port.in.AdminReviewManagementUseCase;

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
public class AdminReviewController {

    private final AdminReviewManagementUseCase reviewService;


    /**
     * Obtiene todas las reseñas del sistema, opcionalmente filtradas por proyecto, de forma paginada.
     * @param projectId ID del proyecto (opcional)
     * @param pageable Parámetros de paginación
     * @return Página de reseñas
     */
    @GetMapping
    public ResponseEntity<Page<ReviewResponseDto>> getAllReviews(
            @RequestParam(value = "projectId", required = false) Long projectId,
            Pageable pageable) {
        Page<ReviewResponseDto> page = reviewService.getAllReviews(projectId, pageable);
        return ResponseEntity.ok(page);
    }

    /**
     * Elimina una reseña por su ID (admin).
     * @param id ID de la reseña
     * @return Respuesta vacía indicando éxito
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
} 