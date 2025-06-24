package com.devmatch.api.role.application.service;

import com.devmatch.api.role.application.dto.RoleRequestDto;
import com.devmatch.api.role.application.dto.RoleResponseDto;
import com.devmatch.api.role.application.mapper.RoleMapper;
import com.devmatch.api.role.application.port.in.RoleManagementUseCase;
import com.devmatch.api.role.application.port.out.RoleRepositoryPort;
import com.devmatch.api.role.domain.exception.RoleAlreadyExistsException;
import com.devmatch.api.role.domain.exception.RoleInUseException;
import com.devmatch.api.role.domain.exception.RoleNotFoundException;
import com.devmatch.api.role.domain.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del caso de uso para la gestión de roles
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RoleManagementUseCaseImpl implements RoleManagementUseCase {
    
    private final RoleRepositoryPort roleRepositoryPort;
    private final RoleMapper roleMapper;
    
    @Override
    public RoleResponseDto createRole(RoleRequestDto request) {
        // Validar que no exista un rol con el mismo nombre
        if (roleRepositoryPort.existsByName(request.getName())) {
            throw new RoleAlreadyExistsException(request.getName());
        }
        
        // Crear el rol
        Role role = roleMapper.toDomain(request);
        Role savedRole = roleRepositoryPort.save(role);
        
        return roleMapper.toResponseDto(savedRole);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RoleResponseDto> getAllRoles() {
        List<Role> roles = roleRepositoryPort.findAll();
        return roleMapper.toResponseDtoList(roles);
    }
    
    @Override
    @Transactional(readOnly = true)
    public RoleResponseDto getRoleById(Long id) {
        Role role = roleRepositoryPort.findById(id)
                .orElseThrow(() -> new RoleNotFoundException(id));
        
        return roleMapper.toResponseDto(role);
    }
    
    @Override
    public RoleResponseDto updateRole(Long id, RoleRequestDto request) {
        // Verificar que el rol existe
        Role existingRole = roleRepositoryPort.findById(id)
                .orElseThrow(() -> new RoleNotFoundException(id));
        
        // Si el nombre cambió, validar que no exista otro rol con el nuevo nombre
        if (!existingRole.getName().equals(request.getName()) && 
            roleRepositoryPort.existsByName(request.getName())) {
            throw new RoleAlreadyExistsException(request.getName());
        }
        
        // Crear el rol actualizado
        Role updatedRole = new Role(id, request.getName(), request.getDescription());
        Role savedRole = roleRepositoryPort.save(updatedRole);
        
        return roleMapper.toResponseDto(savedRole);
    }
    
    @Override
    public void deleteRole(Long id) {
        // Verificar que el rol existe
        Role role = roleRepositoryPort.findById(id)
                .orElseThrow(() -> new RoleNotFoundException(id));
        
        // Verificar que el rol no esté en uso
        long usersWithRole = roleRepositoryPort.countUsersByRoleName(role.getName());
        if (usersWithRole > 0) {
            throw new RoleInUseException(role.getName());
        }
        
        // Eliminar el rol
        roleRepositoryPort.deleteById(id);
    }
} 