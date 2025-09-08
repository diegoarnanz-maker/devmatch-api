# 🔧 API Administrativa

## Descripción General
Los endpoints administrativos permiten gestionar usuarios, logros, tags y otras funcionalidades del sistema. Requieren rol de administrador.

## 👥 Gestión de Usuarios

### GET `/api/v1/admin/users`
**Obtener todos los usuarios**

Retorna una lista paginada de todos los usuarios del sistema.

**Headers:**
- `Authorization: Bearer {admin_token}`

**Parámetros:**
- `page` (opcional): Número de página
- `size` (opcional): Tamaño de página
- `search` (opcional): Búsqueda por nombre o email
- `role` (opcional): Filtro por rol
- `status` (opcional): Filtro por estado

**Respuesta:**
```json
{
  "content": [
    {
      "id": 1,
      "username": "usuario@ejemplo.com",
      "email": "usuario@ejemplo.com",
      "firstName": "Juan",
      "lastName": "Pérez",
      "role": "DEVELOPER",
      "status": "ACTIVE",
      "createdAt": "2025-01-01T00:00:00",
      "lastLoginAt": "2025-09-08T09:00:00"
    }
  ],
  "totalElements": 150,
  "totalPages": 8,
  "size": 20,
  "number": 0
}
```

### PUT `/api/v1/admin/users/{userId}/role`
**Cambiar rol de usuario**

Cambia el rol de un usuario específico.

**Body:**
```json
{
  "role": "ADMIN"
}
```

### DELETE `/api/v1/admin/users/{userId}`
**Eliminar usuario**

Elimina un usuario del sistema usando soft delete.

### GET `/api/v1/admin/users/{userId}/achievements`
**Obtener logros de usuario**

Retorna todos los logros de un usuario específico.

## 🏆 Gestión de Logros

### GET `/api/v1/admin/achievements`
**Obtener todos los logros**

Retorna TODOS los logros del sistema, incluyendo inactivos y eliminados.

### POST `/api/v1/admin/achievements`
**Crear nuevo logro**

Crea un nuevo logro en el sistema.

**Body:**
```json
{
  "code": "NEW_ACHIEVEMENT",
  "title": "Nuevo Logro",
  "description": "Descripción del nuevo logro",
  "points": 200,
  "type": "SOCIAL",
  "iconUrl": "https://example.com/icons/new.png"
}
```

### PUT `/api/v1/admin/achievements/{achievementId}`
**Actualizar logro**

Actualiza un logro existente.

### PATCH `/api/v1/admin/achievements/{achievementId}/toggle-status`
**Activar/desactivar logro**

Cambia el estado activo/inactivo de un logro.

### DELETE `/api/v1/admin/achievements/{achievementId}`
**Eliminar logro**

Elimina un logro del sistema (soft delete).

### POST `/api/v1/admin/users/{userId}/achievements/{achievementId}`
**Otorgar logro a usuario**

Otorga un logro específico a un usuario.

### DELETE `/api/v1/admin/users/{userId}/achievements/{achievementId}`
**Revocar logro de usuario**

Revoca un logro específico de un usuario.

## 🏷️ Gestión de Tags

### GET `/api/v1/admin/tags`
**Obtener todos los tags**

Retorna todos los tags del sistema.

### POST `/api/v1/admin/tags`
**Crear nuevo tag**

Crea un nuevo tag en el sistema.

**Body:**
```json
{
  "name": "nuevo-tag",
  "description": "Descripción del nuevo tag",
  "color": "#FF5733"
}
```

### PUT `/api/v1/admin/tags/{tagId}`
**Actualizar tag**

Actualiza un tag existente.

### DELETE `/api/v1/admin/tags/{tagId}`
**Eliminar tag**

Elimina un tag del sistema.

## 📊 Estadísticas del Sistema

### GET `/api/v1/admin/stats`
**Obtener estadísticas generales**

Retorna estadísticas generales del sistema.

**Respuesta:**
```json
{
  "totalUsers": 150,
  "activeUsers": 120,
  "totalProjects": 45,
  "activeProjects": 38,
  "totalAchievements": 25,
  "totalMessages": 1250,
  "totalReviews": 89,
  "averageRating": 4.2
}
```

### GET `/api/v1/admin/stats/users`
**Estadísticas de usuarios**

Retorna estadísticas detalladas de usuarios.

### GET `/api/v1/admin/stats/projects`
**Estadísticas de proyectos**

Retorna estadísticas detalladas de proyectos.

### GET `/api/v1/admin/stats/achievements`
**Estadísticas de logros**

Retorna estadísticas detalladas de logros.

## 🔍 Búsquedas Avanzadas

### GET `/api/v1/admin/search/users`
**Búsqueda avanzada de usuarios**

Permite búsquedas complejas de usuarios con múltiples filtros.

**Parámetros:**
- `query` (opcional): Texto de búsqueda
- `role` (opcional): Filtro por rol
- `status` (opcional): Filtro por estado
- `createdFrom` (opcional): Fecha de creación desde
- `createdTo` (opcional): Fecha de creación hasta
- `lastLoginFrom` (opcional): Último login desde
- `lastLoginTo` (opcional): Último login hasta

### GET `/api/v1/admin/search/projects`
**Búsqueda avanzada de proyectos**

Permite búsquedas complejas de proyectos.

## 🔐 Gestión de Roles

### GET `/api/v1/roles/admin`
**Obtener todos los roles**

Obtiene todos los roles disponibles en el sistema.

**Headers:**
- `Authorization: Bearer {admin_token}` (requerido - Solo ADMIN)

**Respuesta:**
```json
[
  {
    "id": 1,
    "name": "ADMIN",
    "description": "Administrador del sistema",
    "createdAt": "2025-01-01T00:00:00",
    "updatedAt": "2025-01-01T00:00:00"
  },
  {
    "id": 2,
    "name": "DEVELOPER",
    "description": "Desarrollador",
    "createdAt": "2025-01-01T00:00:00",
    "updatedAt": "2025-01-01T00:00:00"
  }
]
```

### GET `/api/v1/roles/admin/{roleId}`
**Obtener rol por ID**

Obtiene los detalles de un rol específico por su ID.

**Headers:**
- `Authorization: Bearer {admin_token}` (requerido - Solo ADMIN)

### POST `/api/v1/roles/admin`
**Crear nuevo rol**

Crea un nuevo rol en el sistema.

**Headers:**
- `Authorization: Bearer {admin_token}` (requerido - Solo ADMIN)

**Body:**
```json
{
  "name": "MODERATOR",
  "description": "Moderador de la comunidad"
}
```

### PUT `/api/v1/roles/admin/{roleId}`
**Actualizar rol**

Actualiza los datos de un rol existente.

**Headers:**
- `Authorization: Bearer {admin_token}` (requerido - Solo ADMIN)

**Body:**
```json
{
  "name": "SENIOR_DEVELOPER",
  "description": "Desarrollador senior con más permisos"
}
```

### DELETE `/api/v1/roles/admin/{roleId}`
**Eliminar rol**

Elimina un rol del sistema (solo si no está siendo utilizado por usuarios).

**Headers:**
- `Authorization: Bearer {admin_token}` (requerido - Solo ADMIN)

## 🏷️ Gestión de Tags

### GET `/api/v1/tags/admin`
**Obtener todos los tags (Admin)**

Obtiene todos los tags disponibles en el sistema (incluyendo eliminados).

**Headers:**
- `Authorization: Bearer {admin_token}` (requerido - Solo ADMIN)

**Parámetros de consulta:**
- `page` (opcional): Número de página (default: 0)
- `size` (opcional): Tamaño de página (default: 20)
- `sort` (opcional): Ordenación (ej: `name,asc`)

### GET `/api/v1/tags/admin/active`
**Obtener tags activos (Admin)**

Obtiene solo los tags activos (no eliminados).

**Headers:**
- `Authorization: Bearer {admin_token}` (requerido - Solo ADMIN)

### POST `/api/v1/tags/admin`
**Crear nuevo tag (Admin)**

Crea un nuevo tag en el sistema.

**Headers:**
- `Authorization: Bearer {admin_token}` (requerido - Solo ADMIN)

**Body:**
```json
{
  "name": "React",
  "description": "Biblioteca de JavaScript para interfaces de usuario",
  "type": "TECHNOLOGY",
  "color": "#61DAFB"
}
```

### PUT `/api/v1/tags/admin/{tagId}`
**Actualizar tag (Admin)**

Actualiza un tag existente.

**Headers:**
- `Authorization: Bearer {admin_token}` (requerido - Solo ADMIN)

**Body:**
```json
{
  "name": "React.js",
  "description": "Biblioteca de JavaScript para interfaces de usuario",
  "type": "TECHNOLOGY",
  "color": "#61DAFB"
}
```

### DELETE `/api/v1/tags/admin/{tagId}`
**Eliminar tag (Admin)**

Elimina un tag del sistema.

**Headers:**
- `Authorization: Bearer {admin_token}` (requerido - Solo ADMIN)

### PUT `/api/v1/tags/admin/{tagId}/reactivate`
**Reactivar tag (Admin)**

Reactiva un tag eliminado.

**Headers:**
- `Authorization: Bearer {admin_token}` (requerido - Solo ADMIN)

### GET `/api/v1/tags/admin/users/{userId}`
**Obtener tags de usuario (Admin)**

Obtiene los tags de un usuario específico.

**Headers:**
- `Authorization: Bearer {admin_token}` (requerido - Solo ADMIN)

## 🏷️ Gestión de Tags de Usuario

### GET `/api/v1/tags`
**Obtener todos los tags activos**

Obtiene todos los tags activos disponibles en el sistema (público).

**Parámetros de consulta:**
- `page` (opcional): Número de página (default: 0)
- `size` (opcional): Tamaño de página (default: 20)
- `sort` (opcional): Ordenación (ej: `name,asc`)

### GET `/api/v1/tags/search/{name}`
**Buscar tags por nombre**

Busca tags por nombre que contengan el texto especificado (público).

### GET `/api/v1/tags/by-type/{tagType}`
**Obtener tags por tipo**

Obtiene tags por tipo específico (público).

### GET `/api/v1/tags/profile/my-tags`
**Obtener mis tags de perfil**

Obtiene todos los tags del perfil del usuario autenticado.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

### POST `/api/v1/tags/profile/add`
**Agregar tag a perfil**

Agrega un tag al perfil del usuario autenticado.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

**Body:**
```json
{
  "tagId": 1
}
```

### DELETE `/api/v1/tags/profile/remove/{tagId}`
**Remover tag de perfil**

Remueve un tag del perfil del usuario autenticado.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

## 📊 Códigos de Error

| Código | Descripción |
|--------|-------------|
| 200 | Éxito |
| 201 | Creado exitosamente |
| 204 | Eliminado exitosamente |
| 400 | Datos de entrada inválidos |
| 401 | No autorizado - Token JWT inválido |
| 403 | Prohibido - Se requiere rol de administrador |
| 404 | Recurso no encontrado |
| 409 | Conflicto - Recurso ya existe |

## 🧪 Ejemplos de Uso

### Obtener estadísticas del sistema
```bash
curl -X GET "http://localhost:8080/api/v1/admin/stats" \
  -H "Authorization: Bearer {admin_token}"
```

### Cambiar rol de usuario
```bash
curl -X PUT "http://localhost:8080/api/v1/admin/users/1/role" \
  -H "Authorization: Bearer {admin_token}" \
  -H "Content-Type: application/json" \
  -d '{
    "role": "ADMIN"
  }'
```

### Crear nuevo logro
```bash
curl -X POST "http://localhost:8080/api/v1/admin/achievements" \
  -H "Authorization: Bearer {admin_token}" \
  -H "Content-Type: application/json" \
  -d '{
    "code": "NEW_ACHIEVEMENT",
    "title": "Nuevo Logro",
    "description": "Descripción del nuevo logro",
    "points": 200,
    "type": "SOCIAL"
  }'
```

### Obtener todos los roles
```bash
curl -X GET "http://localhost:8080/api/v1/roles/admin" \
  -H "Authorization: Bearer {admin_token}"
```

### Crear nuevo rol
```bash
curl -X POST "http://localhost:8080/api/v1/roles/admin" \
  -H "Authorization: Bearer {admin_token}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "MODERATOR",
    "description": "Moderador de la comunidad"
  }'
```

### Actualizar rol
```bash
curl -X PUT "http://localhost:8080/api/v1/roles/admin/2" \
  -H "Authorization: Bearer {admin_token}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "SENIOR_DEVELOPER",
    "description": "Desarrollador senior con más permisos"
  }'
```

### Eliminar rol
```bash
curl -X DELETE "http://localhost:8080/api/v1/roles/admin/3" \
  -H "Authorization: Bearer {admin_token}"
```

### Obtener todos los tags (Admin)
```bash
curl -X GET "http://localhost:8080/api/v1/tags/admin?page=0&size=20&sort=name,asc" \
  -H "Authorization: Bearer {admin_token}"
```

### Crear nuevo tag (Admin)
```bash
curl -X POST "http://localhost:8080/api/v1/tags/admin" \
  -H "Authorization: Bearer {admin_token}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "React",
    "description": "Biblioteca de JavaScript para interfaces de usuario",
    "type": "TECHNOLOGY",
    "color": "#61DAFB"
  }'
```

### Buscar tags por nombre
```bash
curl -X GET "http://localhost:8080/api/v1/tags/search/java"
```

### Obtener mis tags de perfil
```bash
curl -X GET "http://localhost:8080/api/v1/tags/profile/my-tags" \
  -H "Authorization: Bearer {token}"
```

### Agregar tag a perfil
```bash
curl -X POST "http://localhost:8080/api/v1/tags/profile/add" \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "tagId": 1
  }'
```

### Otorgar logro a usuario
```bash
curl -X POST "http://localhost:8080/api/v1/admin/users/1/achievements/5" \
  -H "Authorization: Bearer {admin_token}"
```
