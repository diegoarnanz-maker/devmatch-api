# Puertos de Entrada (Port/In)

Los puertos de entrada definen cómo el exterior puede interactuar con nuestro dominio. Son interfaces que representan los casos de uso de la aplicación.

## Estructura Actual

- `UserLifecycleUseCase`: Gestiona el ciclo de vida de los usuarios (activar, desactivar, eliminar)
- `UserQueryUseCase`: Maneja las consultas y búsquedas de usuarios
- `AuthUseCase`: Gestiona la autenticación y autorización
- `ProfileUseCase`: Maneja la gestión de perfiles de usuario
- `AdminUserManagementUseCase`: Operaciones específicas para administradores

## Características Principales

1. **Definen el Contrato**: Especifican qué operaciones puede realizar el exterior con nuestro dominio
2. **Independencia**: No dependen de detalles de implementación
3. **Casos de Uso**: Cada interfaz representa un conjunto de casos de uso relacionados
4. **DTOs**: Trabajan con DTOs en lugar de entidades de dominio

## Ejemplo de Uso

```java
public interface UserLifecycleUseCase {
    void deactivateUser(Long userId);
    void reactivateUser(Long userId);
    void deleteUser(Long userId);
}
```

## Beneficios

- **Claridad**: Define claramente qué puede hacer el exterior con nuestro dominio
- **Mantenibilidad**: Facilita los cambios en la implementación sin afectar a los clientes
- **Testabilidad**: Permite probar los casos de uso de forma aislada
- **Inversión de Dependencias**: El dominio define el contrato, no depende de la infraestructura 