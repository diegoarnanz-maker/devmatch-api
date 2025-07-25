package com.devmatch.api.projectreview.application.port.in;

import com.devmatch.api.projectreview.application.dto.ReviewResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminReviewManagementUseCase {
    /**
     * Listar todas las reviews del sistema, opcionalmente filtradas por proyecto, de forma paginada.
     */
    Page<ReviewResponseDto> getAllReviews(Long projectId, Pageable pageable);

    /**
     * Eliminar una review como admin (borrado lógico o físico según política).
     */
    void deleteReview(Long reviewId);
} 