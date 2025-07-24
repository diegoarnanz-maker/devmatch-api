package com.devmatch.api.projectreview.domain.service;

import org.springframework.stereotype.Service;
import com.devmatch.api.projectreview.domain.exception.ReviewLimitExceededException;
import com.devmatch.api.projectreview.domain.exception.ReviewOperationNotAllowedException;

/**
 * Servicio de dominio que encapsula reglas de negocio y validaciones
 * que no pertenecen naturalmente a la entidad Review.
 */
@Service
public class ReviewDomainService {
    /**
     * Valida que un usuario solo pueda dejar una review por proyecto.
     * @param userId ID del usuario
     * @param projectId ID del proyecto
     * @param alreadyReviewed true si ya existe una review de este usuario para este proyecto
     * @throws ReviewLimitExceededException si el usuario ya dejó una review para este proyecto
     */
    public void validateSingleReviewPerUserProject(Long userId, Long projectId, boolean alreadyReviewed) {
        if (alreadyReviewed) {
            throw new ReviewLimitExceededException("El usuario " + userId + " ya ha dejado una review para el proyecto " + projectId);
        }
    }

    /**
     * Valida que solo ciertos roles puedan dejar reviews (por ejemplo, solo miembros, no invitados).
     * @param userId ID del usuario
     * @param projectId ID del proyecto
     * @param isMember true si el usuario es miembro del proyecto
     * @throws ReviewOperationNotAllowedException si el usuario no tiene permisos para dejar una review
     */
    public void validateUserCanReview(Long userId, Long projectId, boolean isMember) {
        if (!isMember) {
            throw new ReviewOperationNotAllowedException("El usuario " + userId + " no tiene permisos para dejar una review en el proyecto " + projectId);
        }
    }
} 