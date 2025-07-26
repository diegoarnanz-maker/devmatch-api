package com.devmatch.api.projectreview.application.mapper;

import org.springframework.stereotype.Component;
import com.devmatch.api.projectreview.domain.model.Review;
import com.devmatch.api.projectreview.domain.model.valueobject.Rating;
import com.devmatch.api.projectreview.domain.model.valueobject.Comment;
import com.devmatch.api.projectreview.application.dto.ReviewRequestDto;
import com.devmatch.api.projectreview.application.dto.ReviewResponseDto;
import java.time.LocalDateTime;

@Component
public class ReviewMapper {

    public Review toDomain(Long userId, ReviewRequestDto dto) {
        LocalDateTime now = LocalDateTime.now();
        return new Review(
            null, // id (se asigna al guardar)
            dto.getProjectId(),
            userId,
            new Rating(dto.getRating()),
            new Comment(dto.getComment()),
            dto.getIsPublic() != null ? dto.getIsPublic() : true,
            true, // isActive por defecto
            false, // isDeleted por defecto
            now, // createdAt
            now  // updatedAt
        );
    }

    public ReviewResponseDto toResponseDto(Review review) {
        return new ReviewResponseDto(
            review.getId(),
            review.getProjectId(),
            review.getUserId(),
            review.getRating() != null ? review.getRating().getValue() : null,
            review.getComment() != null ? review.getComment().getValue() : null,
            review.isPublic(),
            review.isActive(),
            review.isDeleted(),
            review.getCreatedAt(),
            review.getUpdatedAt()
        );
    }
}
