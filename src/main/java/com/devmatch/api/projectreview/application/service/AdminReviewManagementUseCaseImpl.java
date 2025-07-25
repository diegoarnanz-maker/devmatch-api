package com.devmatch.api.projectreview.application.service;

import com.devmatch.api.projectreview.application.dto.ReviewResponseDto;
import com.devmatch.api.projectreview.application.mapper.ReviewMapper;
import com.devmatch.api.projectreview.application.port.in.AdminReviewManagementUseCase;
import com.devmatch.api.projectreview.domain.model.Review;
import com.devmatch.api.projectreview.domain.exception.ReviewNotFoundException;
import com.devmatch.api.projectreview.domain.service.ReviewDomainService;
import com.devmatch.api.projectreview.application.port.out.ReviewRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
@Transactional
public class AdminReviewManagementUseCaseImpl implements AdminReviewManagementUseCase {

    private final ReviewRepositoryPort reviewRepositoryPort;
    private final ReviewMapper reviewMapper;
    private final ReviewDomainService reviewDomainService;

    public AdminReviewManagementUseCaseImpl(
        @Qualifier("adminReviewRepositoryAdapter") ReviewRepositoryPort reviewRepositoryPort,
        ReviewMapper reviewMapper,
        ReviewDomainService reviewDomainService) {
        this.reviewRepositoryPort = reviewRepositoryPort;
        this.reviewMapper = reviewMapper;
        this.reviewDomainService = reviewDomainService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponseDto> getAllReviews(Long projectId, Pageable pageable) {
        Page<Review> page;
        if (projectId != null) {
            page = reviewRepositoryPort.findByProjectId(projectId, pageable);
        } else {
            page = reviewRepositoryPort.findAll(pageable);
        }
        return page.map(reviewMapper::toResponseDto);
    }

    @Override
    public void deleteReview(Long reviewId) {
        Review review = reviewRepositoryPort.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
        // Borrado lógico
        review.setActive(false);
        review.setDeleted(true);
        reviewRepositoryPort.save(review);
    }
} 