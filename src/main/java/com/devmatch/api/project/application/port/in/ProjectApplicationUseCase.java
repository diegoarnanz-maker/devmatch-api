package com.devmatch.api.project.application.port.in;

import com.devmatch.api.project.application.dto.ProjectApplicationResponseDto;
import java.util.List;

/**
 * Puerto de entrada para gestión de aplicaciones a proyectos.
 * 
 * @author diegoarnanz-maker
 * @since 2025
 */
public interface ProjectApplicationUseCase {

    void applyToProject(Long projectId, Long userId, String motivationMessage);
    List<ProjectApplicationResponseDto> getProjectApplications(Long projectId, Long ownerId);
    List<ProjectApplicationResponseDto> getUserApplications(Long userId);
    void acceptApplication(Long projectId, Long applicationId, Long ownerId);
    void rejectApplication(Long projectId, Long applicationId, Long ownerId);
    void cancelApplication(Long applicationId, Long userId);
    
}
