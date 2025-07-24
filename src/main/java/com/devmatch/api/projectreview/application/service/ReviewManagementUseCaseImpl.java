package com.devmatch.api.projectreview.application.service;

import com.devmatch.api.projectreview.application.dto.ReviewRequestDto;
import com.devmatch.api.projectreview.application.dto.ReviewResponseDto;
import com.devmatch.api.projectreview.application.mapper.ReviewMapper;
import com.devmatch.api.projectreview.application.port.in.ReviewManagementUseCase;
import com.devmatch.api.projectreview.application.port.out.ReviewRepositoryPort;
import com.devmatch.api.projectreview.domain.exception.ReviewNotFoundException;
import com.devmatch.api.projectreview.domain.model.Review;
import com.devmatch.api.projectreview.domain.service.ReviewDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewManagementUseCaseImpl implements ReviewManagementUseCase {

    private final ReviewRepositoryPort reviewRepositoryPort;
    private final ReviewMapper reviewMapper;
    private final ReviewDomainService reviewDomainService;

    @Override
    public ReviewResponseDto createReview(Long userId, ReviewRequestDto requestDto) {
        // Validar que el usuario no haya dejado ya una review para este proyecto
        boolean alreadyReviewed = reviewRepositoryPort.existsByProjectIdAndUserId(requestDto.getProjectId(), userId);
        reviewDomainService.validateSingleReviewPerUserProject(userId, requestDto.getProjectId(), alreadyReviewed);
        // Validar permisos (ejemplo: solo miembros pueden dejar review)
        // Aquí deberías consultar si el usuario es miembro del proyecto (isMember)
        // boolean isMember = ...
        // reviewDomainService.validateUserCanReview(userId, requestDto.getProjectId(), isMember);
        // Mapear y guardar
        Review review = reviewMapper.toDomain(userId, requestDto);
        Review saved = reviewRepositoryPort.save(review);
        return reviewMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewResponseDto getReviewById(Long reviewId) {
        Review review = reviewRepositoryPort.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
        return reviewMapper.toResponseDto(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviewsByProject(Long projectId) {
        List<Review> reviews = reviewRepositoryPort.findByProjectId(projectId);
        return reviews.stream().map(reviewMapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviewsByUser(Long userId) {
        List<Review> reviews = reviewRepositoryPort.findByUserId(userId);
        return reviews.stream().map(reviewMapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public ReviewResponseDto updateReview(Long userId, Long reviewId, ReviewRequestDto requestDto) {
        Review existing = reviewRepositoryPort.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
        // Validar que el usuario es el autor (opcional)
        if (!existing.getUserId().equals(userId)) {
            throw new RuntimeException("No tienes permisos para actualizar esta review");
        }
        // Actualizar campos permitidos
        existing.setRating(new com.devmatch.api.projectreview.domain.model.valueobject.Rating(requestDto.getRating()));
        existing.setComment(new com.devmatch.api.projectreview.domain.model.valueobject.Comment(requestDto.getComment()));
        existing.setPublic(requestDto.getIsPublic() != null ? requestDto.getIsPublic() : true);
        Review saved = reviewRepositoryPort.save(existing);
        return reviewMapper.toResponseDto(saved);
    }

    @Override
    public void deleteReview(Long userId, Long reviewId) {
        Review existing = reviewRepositoryPort.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
        // Validar que el usuario es el autor (opcional)
        if (!existing.getUserId().equals(userId)) {
            throw new RuntimeException("No tienes permisos para eliminar esta review");
        }
        // Borrado lógico
        existing.setActive(false);
        existing.setDeleted(true);
        reviewRepositoryPort.save(existing);
    }
}
