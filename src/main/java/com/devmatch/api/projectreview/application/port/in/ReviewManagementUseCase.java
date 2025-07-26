package com.devmatch.api.projectreview.application.port.in;

import com.devmatch.api.projectreview.application.dto.ReviewRequestDto;
import com.devmatch.api.projectreview.application.dto.ReviewResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewManagementUseCase {
    /**
     * Crear una nueva review
     */
    ReviewResponseDto createReview(Long userId, ReviewRequestDto requestDto);

    /**
     * Actualizar una review existente
     */
    ReviewResponseDto updateReview(Long userId, Long reviewId, ReviewRequestDto requestDto);

    /**
     * Eliminar una review (borrado lógico)
     */
    void deleteReview(Long userId, Long reviewId);

    /**
     * Obtener una review por su ID
     */
    ReviewResponseDto getReviewById(Long reviewId);

    /**
     * Listar reviews de un proyecto de forma paginada
     */
    Page<ReviewResponseDto> getReviewsByProject(Long projectId, Pageable pageable);

    /**
     * Listar reviews de un usuario
     */
    List<ReviewResponseDto> getReviewsByUser(Long userId);
}
