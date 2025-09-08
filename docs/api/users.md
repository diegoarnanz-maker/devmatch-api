# 👤 API de Usuarios

## Descripción General
Los endpoints de usuarios permiten gestionar perfiles, autenticación y funcionalidades relacionadas con los usuarios de la plataforma.

## 🔐 Endpoints de Autenticación

### POST `/api/v1/users/auth/login`
**Iniciar sesión**

Autentica un usuario y retorna un token JWT.

**Body:**
```json
{
  "username": "usuario@ejemplo.com",
  "password": "mi_password_seguro"
}
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "expiresIn": 86400,
  "user": {
    "id": 1,
    "username": "usuario@ejemplo.com",
    "email": "usuario@ejemplo.com",
    "firstName": "Juan",
    "lastName": "Pérez",
    "profileImageUrl": "https://example.com/avatar.jpg",
    "role": "DEVELOPER",
    "createdAt": "2025-01-01T00:00:00"
  }
}
```

### POST `/api/v1/users/auth/register`
**Registrar nuevo usuario**

Crea una nueva cuenta de usuario en la plataforma.

**Body:**
```json
{
  "username": "nuevo_usuario",
  "email": "nuevo@ejemplo.com",
  "password": "password_seguro",
  "firstName": "María",
  "lastName": "García",
  "profileType": "DEVELOPER"
}
```

**Respuesta:**
```json
{
  "id": 2,
  "username": "nuevo_usuario",
  "email": "nuevo@ejemplo.com",
  "firstName": "María",
  "lastName": "García",
  "profileImageUrl": null,
  "role": "DEVELOPER",
  "createdAt": "2025-09-08T10:00:00"
}
```

### POST `/api/v1/users/auth/refresh`
**Renovar token**

Renueva un token JWT expirado usando un refresh token.

**Body:**
```json
{
  "refreshToken": "refresh_token_aqui"
}
```

## 👤 Endpoints de Perfil

### GET `/api/v1/users/profile/me`
**Obtener mi perfil**

Retorna la información del usuario autenticado.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

**Respuesta:**
```json
{
  "id": 1,
  "username": "usuario@ejemplo.com",
  "email": "usuario@ejemplo.com",
  "firstName": "Juan",
  "lastName": "Pérez",
  "profileImageUrl": "https://example.com/avatar.jpg",
  "role": "DEVELOPER",
  "profileType": "DEVELOPER",
  "createdAt": "2025-01-01T00:00:00",
  "updatedAt": "2025-09-08T10:00:00"
}
```

### GET `/api/v1/users/{userId}/profile`
**Obtener perfil público**

Retorna la información pública de un usuario específico.

**Respuesta:**
```json
{
  "id": 1,
  "username": "usuario@ejemplo.com",
  "firstName": "Juan",
  "lastName": "Pérez",
  "profileImageUrl": "https://example.com/avatar.jpg",
  "role": "DEVELOPER",
  "profileType": "DEVELOPER",
  "createdAt": "2025-01-01T00:00:00"
}
```

### PUT `/api/v1/users/profile`
**Actualizar mi perfil**

Actualiza la información del usuario autenticado.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

**Body:**
```json
{
  "firstName": "Juan Carlos",
  "lastName": "Pérez García",
  "profileImageUrl": "https://example.com/nuevo-avatar.jpg"
}
```

### PUT `/api/v1/users/profile/password`
**Cambiar contraseña**

Cambia la contraseña del usuario autenticado.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

**Body:**
```json
{
  "currentPassword": "password_actual",
  "newPassword": "nueva_password_segura"
}
```

### PUT `/api/v1/users/profile/email`
**Cambiar email**

Cambia el email del usuario autenticado.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

**Body:**
```json
{
  "newEmail": "nuevo@ejemplo.com",
  "password": "password_actual"
}
```

### PUT `/api/v1/users/profile/avatar`
**Cambiar avatar**

Cambia el avatar del usuario autenticado.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

**Body:**
```json
{
  "profileImageUrl": "https://example.com/nuevo-avatar.jpg"
}
```

## 🏷️ Gestión de Tipos de Perfil

### GET `/api/v1/users/profile/types`
**Obtener mis tipos de perfil**

Obtiene los tipos de perfil del usuario autenticado.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

### POST `/api/v1/users/profile/types`
**Agregar tipo de perfil**

Agrega un tipo de perfil al usuario autenticado.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

**Body:**
```json
{
  "profileTypeId": 1
}
```

### DELETE `/api/v1/users/profile/types/{profileTypeId}`
**Remover tipo de perfil**

Remueve un tipo de perfil del usuario autenticado.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

## 🔧 Endpoints Administrativos (Requieren Rol ADMIN)

### POST `/api/v1/users/admin/search`
**Buscar usuarios (Admin)**

Busca usuarios por múltiples criterios opcionales, incluyendo estado.

**Headers:**
- `Authorization: Bearer {admin_token}` (requerido - Solo ADMIN)

**Body:**
```json
{
  "email": "usuario@ejemplo.com",
  "username": "usuario1",
  "firstName": "Juan",
  "lastName": "Pérez",
  "status": "ACTIVE"
}
```

**Parámetros de consulta:**
- `page` (opcional): Número de página (default: 0)
- `size` (opcional): Tamaño de página (default: 20)
- `sort` (opcional): Ordenación (ej: `username,asc`)

### GET `/api/v1/users/{userId}/admin`
**Obtener detalles de usuario (Admin)**

Obtiene los detalles de un usuario para administradores.

**Headers:**
- `Authorization: Bearer {admin_token}` (requerido - Solo ADMIN)

### PUT `/api/v1/users/{userId}/admin/role`
**Cambiar rol de usuario (Admin)**

Cambia el rol de un usuario de forma flexible.

**Headers:**
- `Authorization: Bearer {admin_token}` (requerido - Solo ADMIN)

**Body:**
```json
{
  "role": "ADMIN"
}
```

### PUT `/api/v1/users/{userId}/admin/status`
**Actualizar estado de usuario (Admin)**

Actualiza el estado de activación de un usuario.

**Headers:**
- `Authorization: Bearer {admin_token}` (requerido - Solo ADMIN)

**Parámetros de consulta:**
- `active` (requerido): true para activar, false para desactivar

### DELETE `/api/v1/users/{userId}/admin`
**Eliminar usuario (Admin)**

Elimina un usuario (soft delete).

**Headers:**
- `Authorization: Bearer {admin_token}` (requerido - Solo ADMIN)

## 🏷️ Gestión de Tipos de Perfil (Admin)

### GET `/api/v1/users/admin/profile-types`
**Obtener todos los tipos de perfil (Admin)**

Obtiene todos los tipos de perfil disponibles.

**Headers:**
- `Authorization: Bearer {admin_token}` (requerido - Solo ADMIN)

### POST `/api/v1/users/admin/profile-types`
**Crear tipo de perfil (Admin)**

Crea un nuevo tipo de perfil.

**Headers:**
- `Authorization: Bearer {admin_token}` (requerido - Solo ADMIN)

**Body:**
```json
{
  "name": "SENIOR_DEVELOPER",
  "description": "Desarrollador senior con experiencia avanzada"
}
```

### PUT `/api/v1/users/admin/profile-types/{profileTypeId}`
**Actualizar tipo de perfil (Admin)**

Actualiza un tipo de perfil existente.

**Headers:**
- `Authorization: Bearer {admin_token}` (requerido - Solo ADMIN)

### DELETE `/api/v1/users/admin/profile-types/{profileTypeId}`
**Eliminar tipo de perfil (Admin)**

Elimina un tipo de perfil.

**Headers:**
- `Authorization: Bearer {admin_token}` (requerido - Solo ADMIN)

### GET `/api/v1/users/{userId}/admin/profile-types`
**Obtener tipos de perfil de usuario (Admin)**

Obtiene los tipos de perfil de un usuario específico.

**Headers:**
- `Authorization: Bearer {admin_token}` (requerido - Solo ADMIN)

### POST `/api/v1/users/{userId}/admin/profile-types`
**Agregar tipo de perfil a usuario (Admin)**

Agrega un tipo de perfil a un usuario.

**Headers:**
- `Authorization: Bearer {admin_token}` (requerido - Solo ADMIN)

**Body:**
```json
{
  "profileTypeId": 1
}
```

## 📊 Códigos de Error

| Código | Descripción |
|--------|-------------|
| 200 | Éxito |
| 201 | Creado exitosamente |
| 400 | Datos de entrada inválidos |
| 401 | No autorizado - Token JWT inválido |
| 403 | Prohibido - Acceso denegado |
| 404 | Usuario no encontrado |
| 409 | Conflicto - Usuario ya existe |

## 🧪 Ejemplos de Uso

### Iniciar sesión
```bash
curl -X POST "http://localhost:8080/api/v1/users/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "usuario@ejemplo.com",
    "password": "mi_password_seguro"
  }'
```

### Registrar nuevo usuario
```bash
curl -X POST "http://localhost:8080/api/v1/users/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "nuevo_usuario",
    "email": "nuevo@ejemplo.com",
    "password": "password_seguro",
    "firstName": "María",
    "lastName": "García",
    "profileType": "DEVELOPER"
  }'
```

### Obtener mi perfil
```bash
curl -X GET "http://localhost:8080/api/v1/users/profile/me" \
  -H "Authorization: Bearer {token}"
```

### Obtener perfil público
```bash
curl -X GET "http://localhost:8080/api/v1/users/1/profile"
```

### Actualizar perfil
```bash
curl -X PUT "http://localhost:8080/api/v1/users/profile" \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Juan Carlos",
    "lastName": "Pérez García"
  }'
```

### Cambiar contraseña
```bash
curl -X PUT "http://localhost:8080/api/v1/users/profile/password" \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "currentPassword": "password_actual",
    "newPassword": "nueva_password_segura"
  }'
```

### Cambiar email
```bash
curl -X PUT "http://localhost:8080/api/v1/users/profile/email" \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "newEmail": "nuevo@ejemplo.com",
    "password": "password_actual"
  }'
```

### Obtener mis tipos de perfil
```bash
curl -X GET "http://localhost:8080/api/v1/users/profile/types" \
  -H "Authorization: Bearer {token}"
```

### Agregar tipo de perfil
```bash
curl -X POST "http://localhost:8080/api/v1/users/profile/types" \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "profileTypeId": 1
  }'
```

### Buscar usuarios (Admin)
```bash
curl -X POST "http://localhost:8080/api/v1/users/admin/search?page=0&size=20&sort=username,asc" \
  -H "Authorization: Bearer {admin_token}" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "usuario@ejemplo.com",
    "status": "ACTIVE"
  }'
```

### Cambiar rol de usuario (Admin)
```bash
curl -X PUT "http://localhost:8080/api/v1/users/1/admin/role" \
  -H "Authorization: Bearer {admin_token}" \
  -H "Content-Type: application/json" \
  -d '{
    "role": "ADMIN"
  }'
```

### Crear tipo de perfil (Admin)
```bash
curl -X POST "http://localhost:8080/api/v1/users/admin/profile-types" \
  -H "Authorization: Bearer {admin_token}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "SENIOR_DEVELOPER",
    "description": "Desarrollador senior con experiencia avanzada"
  }'
```
