package com.devmatch.api.project.application.port.in;

import com.devmatch.api.project.application.dto.ProjectApplicationResponseDto;
import java.util.List;

public interface ProjectApplicationUseCase {

    void applyToProject(Long projectId, Long userId, String motivationMessage);
    List<ProjectApplicationResponseDto> getProjectApplications(Long projectId, Long ownerId);
    void acceptApplication(Long projectId, Long applicationId, Long ownerId);
    void rejectApplication(Long projectId, Long applicationId, Long ownerId);
    
}
