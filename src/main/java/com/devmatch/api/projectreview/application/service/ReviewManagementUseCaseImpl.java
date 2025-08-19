package com.devmatch.api.projectreview.application.service;

import com.devmatch.api.projectreview.application.dto.ReviewRequestDto;
import com.devmatch.api.projectreview.application.dto.ReviewResponseDto;
import com.devmatch.api.projectreview.application.mapper.ReviewMapper;
import com.devmatch.api.projectreview.application.port.in.ReviewManagementUseCase;
import com.devmatch.api.projectreview.application.port.out.ReviewRepositoryPort;
import com.devmatch.api.projectreview.domain.exception.ReviewNotFoundException;
import com.devmatch.api.projectreview.domain.exception.ReviewOperationNotAllowedException;
import com.devmatch.api.projectreview.domain.model.Review;
import com.devmatch.api.projectreview.domain.service.ReviewDomainService;
import com.devmatch.api.project.application.port.out.ProjectRepositoryPort;
import com.devmatch.api.project.domain.model.valueobject.ProjectStatus;
import com.devmatch.api.shared.application.port.out.DomainEventPublisher;
import com.devmatch.api.projectreview.domain.event.ProjectReviewReceivedEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReviewManagementUseCaseImpl implements ReviewManagementUseCase {

    private final ReviewRepositoryPort reviewRepositoryPort;
    private final ReviewMapper reviewMapper;
    private final ReviewDomainService reviewDomainService;
    private final ProjectRepositoryPort projectRepositoryPort;
    private final DomainEventPublisher domainEventPublisher;

    public ReviewManagementUseCaseImpl(
        @Qualifier("reviewRepositoryAdapter") ReviewRepositoryPort reviewRepositoryPort,
        ReviewMapper reviewMapper,
        ReviewDomainService reviewDomainService,
        ProjectRepositoryPort projectRepositoryPort,
        DomainEventPublisher domainEventPublisher
    ) {
        this.reviewRepositoryPort = reviewRepositoryPort;
        this.reviewMapper = reviewMapper;
        this.reviewDomainService = reviewDomainService;
        this.projectRepositoryPort = projectRepositoryPort;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    @Transactional
    public ReviewResponseDto createReview(Long userId, ReviewRequestDto requestDto) {
        // Validar que el proyecto existe y está en estado COMPLETED
        var project = projectRepositoryPort.findById(requestDto.getProjectId())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + requestDto.getProjectId()));
        
        if (project.getStatus() != ProjectStatus.COMPLETED) {
            throw ReviewOperationNotAllowedException.projectNotCompleted(requestDto.getProjectId());
        }
        
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
        
        // IMPORTANTE: Publicar evento DESPUÉS de guardar la review
        // para que el review_id esté disponible en la base de datos
        domainEventPublisher.publish(new ProjectReviewReceivedEvent(
            requestDto.getProjectId(),
            project.getTitle().getValue(), // Usar el título real del proyecto
            project.getOwnerId(),         // ID del propietario del proyecto
            userId,                       // ID del revisor
            "Usuario " + userId,          // Fallback para el nombre del revisor
            saved.getId()                 // ID de la review guardada
        ));
        
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
    public Page<ReviewResponseDto> getReviewsByProject(Long projectId, Pageable pageable) {
        Page<Review> reviews = reviewRepositoryPort.findByProjectId(projectId, pageable);
        return reviews.map(reviewMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviewsByUser(Long userId) {
        List<Review> reviews = reviewRepositoryPort.findByUserId(userId);
        return reviews.stream().map(reviewMapper::toResponseDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponseDto> getMyReviews(Long userId, Pageable pageable) {
        Page<Review> reviews = reviewRepositoryPort.findByUserId(userId, pageable);
        return reviews.map(reviewMapper::toResponseDto);
    }

    @Override
    public ReviewResponseDto updateReview(Long userId, Long reviewId, ReviewRequestDto requestDto) {
        Review existing = reviewRepositoryPort.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
        
        // Validar que el usuario es el autor
        if (!existing.getUserId().equals(userId)) {
            throw ReviewOperationNotAllowedException.insufficientPermissions(userId, reviewId);
        }
        
        // Validar que el proyecto sigue en estado COMPLETED
        var project = projectRepositoryPort.findById(existing.getProjectId())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + existing.getProjectId()));
        
        if (project.getStatus() != ProjectStatus.COMPLETED) {
            throw ReviewOperationNotAllowedException.projectNotCompleted(existing.getProjectId());
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
        
        // Validar que el usuario es el autor
        if (!existing.getUserId().equals(userId)) {
            throw ReviewOperationNotAllowedException.insufficientPermissions(userId, reviewId);
        }
        
        // Borrado lógico
        existing.setActive(false);
        existing.setDeleted(true);
        reviewRepositoryPort.save(existing);
    }
}
