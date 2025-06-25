package com.devmatch.api.project.application.dto;

import com.devmatch.api.project.domain.model.valueobject.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitudes de creación y actualización de proyectos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequestDto {
    
    @NotBlank(message = "El título del proyecto es obligatorio")
    @Size(min = 5, max = 100, message = "El título debe tener entre 5 y 100 caracteres")
    private String title;
    
    @NotBlank(message = "La descripción del proyecto es obligatoria")
    @Size(min = 20, max = 2000, message = "La descripción debe tener entre 20 y 2000 caracteres")
    private String description;
    
    @NotNull(message = "El estado del proyecto es obligatorio")
    private ProjectStatus status;
    
    @Pattern(
        regexp = "^(https?://)?(www\\.)?github\\.com/[a-zA-Z0-9-]+/[a-zA-Z0-9-]+/?$",
        message = "La URL del repositorio debe ser una URL válida de GitHub"
    )
    private String repoUrl;
    
    @Pattern(
        regexp = "^(https?://).*",
        message = "La URL de la imagen de portada debe comenzar con http:// o https://"
    )
    private String coverImageUrl;
    
    @jakarta.validation.constraints.Min(value = 1, message = "La duración estimada debe ser al menos 1 semana")
    @jakarta.validation.constraints.Max(value = 104, message = "La duración estimada no puede exceder 2 años (104 semanas)")
    private Integer estimatedDurationWeeks;
    
    @jakarta.validation.constraints.Min(value = 1, message = "El tamaño máximo del equipo debe ser al menos 1")
    @jakarta.validation.constraints.Max(value = 20, message = "El tamaño máximo del equipo no puede exceder 20 personas")
    private Integer maxTeamSize;
    
    private boolean isPublic = true;
} 