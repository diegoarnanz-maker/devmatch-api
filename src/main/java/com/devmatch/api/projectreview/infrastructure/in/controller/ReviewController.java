package com.devmatch.api.projectreview.infrastructure.in.controller;

import com.devmatch.api.projectreview.application.dto.ReviewRequestDto;
import com.devmatch.api.projectreview.application.dto.ReviewResponseDto;
import com.devmatch.api.projectreview.application.port.in.ReviewManagementUseCase;
import com.devmatch.api.security.infrastructure.out.adapter.UserPrincipalAdapter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

/**
 * Controlador para operaciones de reseñas de proyectos.
 * 
 * Este controlador expone endpoints para que los usuarios puedan gestionar reseñas.
 */
@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewManagementUseCase reviewManagementUseCase;

    /**
     * Obtiene todas las reseñas de un proyecto específico.
     * 
     * @param projectId ID del proyecto
     * @return Lista de reseñas del proyecto
     */
    @GetMapping
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByProject(@RequestParam Long projectId) {
        List<ReviewResponseDto> reviews = reviewManagementUseCase.getReviewsByProject(projectId);
        return ResponseEntity.ok(reviews);
    }

    /**
     * Obtiene los detalles de una reseña específica por su ID.
     * 
     * @param reviewId ID de la reseña
     * @return Detalles de la reseña
     */
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> getReviewById(@PathVariable Long reviewId) {
        ReviewResponseDto review = reviewManagementUseCase.getReviewById(reviewId);
        return ResponseEntity.ok(review);
    }

    /**
     * Crea una nueva reseña para un proyecto.
     * 
     * @param userPrincipal Usuario autenticado
     * @param request DTO con los datos de la reseña
     * @return Reseña creada
     */
    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @Valid @RequestBody ReviewRequestDto request) {
        ReviewResponseDto createdReview = reviewManagementUseCase.createReview(userPrincipal.getUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    /**
     * Actualiza una reseña existente.
     * 
     * @param userPrincipal Usuario autenticado
     * @param reviewId ID de la reseña a actualizar
     * @param request DTO con los nuevos datos de la reseña
     * @return Reseña actualizada
     */
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequestDto request) {
        ReviewResponseDto updatedReview = reviewManagementUseCase.updateReview(userPrincipal.getUserId(), reviewId, request);
        return ResponseEntity.ok(updatedReview);
    }

    /**
     * Elimina una reseña del sistema.
     * 
     * @param userPrincipal Usuario autenticado
     * @param reviewId ID de la reseña a eliminar
     * @return Respuesta vacía indicando éxito
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @AuthenticationPrincipal UserPrincipalAdapter userPrincipal,
            @PathVariable Long reviewId) {
        reviewManagementUseCase.deleteReview(userPrincipal.getUserId(), reviewId);
        return ResponseEntity.noContent().build();
    }
} 