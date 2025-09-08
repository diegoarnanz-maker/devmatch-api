# 🔔 API de Notificaciones

## Descripción General
Los endpoints de notificaciones permiten gestionar el sistema de notificaciones del usuario, incluyendo notificaciones internas del sistema y funcionalidades de usuario para consultar y gestionar sus notificaciones.

## 🔧 Endpoints Internos del Sistema

### POST `/api/v1/notifications/internal/project-application/{userId}/{projectId}`
**Crear notificación de aplicación a proyecto (Interno)**

Crea una notificación de aplicación a proyecto (llamada interna del sistema).

**Parámetros:**
- `userId` (requerido): ID del usuario
- `projectId` (requerido): ID del proyecto

### POST `/api/v1/notifications/internal/project-application-accepted/{userId}/{projectId}`
**Crear notificación de aplicación aceptada (Interno)**

Crea una notificación de aplicación aceptada (llamada interna del sistema).

**Parámetros:**
- `userId` (requerido): ID del usuario
- `projectId` (requerido): ID del proyecto

### POST `/api/v1/notifications/internal/project-application-rejected/{userId}/{projectId}`
**Crear notificación de aplicación rechazada (Interno)**

Crea una notificación de aplicación rechazada (llamada interna del sistema).

**Parámetros:**
- `userId` (requerido): ID del usuario
- `projectId` (requerido): ID del proyecto

**Body:**
```json
{
  "reason": "No cumple con los requisitos del proyecto"
}
```

### POST `/api/v1/notifications/internal/project-member-joined/{userId}/{projectId}`
**Crear notificación de nuevo miembro (Interno)**

Crea una notificación de nuevo miembro en proyecto (llamada interna del sistema).

**Parámetros:**
- `userId` (requerido): ID del usuario
- `projectId` (requerido): ID del proyecto

**Body:**
```json
{
  "memberName": "Juan Pérez"
}
```

### POST `/api/v1/notifications/internal/project-application-cancelled/{userId}/{projectId}`
**Crear notificación de aplicación cancelada (Interno)**

Crea una notificación de aplicación cancelada (llamada interna del sistema).

**Parámetros:**
- `userId` (requerido): ID del usuario
- `projectId` (requerido): ID del proyecto

### POST `/api/v1/notifications/internal/project-application-expired/{userId}/{projectId}`
**Crear notificación de aplicación expirada (Interno)**

Crea una notificación de aplicación expirada (llamada interna del sistema).

**Parámetros:**
- `userId` (requerido): ID del usuario
- `projectId` (requerido): ID del proyecto

### POST `/api/v1/notifications/internal/project-review-received/{userId}/{projectId}/{reviewId}`
**Crear notificación de review recibida (Interno)**

Crea una notificación de review recibida (llamada interna del sistema).

**Parámetros:**
- `userId` (requerido): ID del usuario
- `projectId` (requerido): ID del proyecto
- `reviewId` (requerido): ID del review

**Body:**
```json
{
  "reviewerName": "María García"
}
```

### POST `/api/v1/notifications/internal/achievement-unlocked/{userId}/{achievementCode}`
**Crear notificación de logro desbloqueado (Interno)**

Crea una notificación de logro desbloqueado (llamada interna del sistema).

**Parámetros:**
- `userId` (requerido): ID del usuario
- `achievementCode` (requerido): Código del logro

**Body:**
```json
{
  "achievementName": "Primer Proyecto"
}
```

### POST `/api/v1/notifications/internal/welcome/{userId}`
**Crear notificación de bienvenida (Interno)**

Crea una notificación de bienvenida (llamada interna del sistema).

**Parámetros:**
- `userId` (requerido): ID del usuario

## 👤 Endpoints de Usuario (Requieren Autenticación)

### GET `/api/v1/notifications/my-notifications`
**Obtener mis notificaciones**

Obtiene las notificaciones del usuario autenticado con paginación.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

**Parámetros de consulta:**
- `page` (opcional): Número de página (default: 0)
- `size` (opcional): Tamaño de página (default: 20)

### GET `/api/v1/notifications/my-notifications/unread`
**Obtener mis notificaciones no leídas**

Obtiene las notificaciones no leídas del usuario autenticado con paginación.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

**Parámetros de consulta:**
- `page` (opcional): Número de página (default: 0)
- `size` (opcional): Tamaño de página (default: 20)

### GET `/api/v1/notifications/my-notifications/type/{notificationType}`
**Obtener mis notificaciones por tipo**

Obtiene las notificaciones del usuario autenticado por tipo con paginación.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

**Parámetros:**
- `notificationType` (requerido): Tipo de notificación (ej: PROJECT_APPLICATION)

**Parámetros de consulta:**
- `page` (opcional): Número de página (default: 0)
- `size` (opcional): Tamaño de página (default: 20)

### GET `/api/v1/notifications/my-notifications/project/{projectId}`
**Obtener mis notificaciones por proyecto**

Obtiene las notificaciones del usuario autenticado por proyecto con paginación.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

**Parámetros:**
- `projectId` (requerido): ID del proyecto

**Parámetros de consulta:**
- `page` (opcional): Número de página (default: 0)
- `size` (opcional): Tamaño de página (default: 20)

### GET `/api/v1/notifications/{notificationId}`
**Obtener notificación por ID**

Obtiene una notificación específica del usuario autenticado.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

**Parámetros:**
- `notificationId` (requerido): ID de la notificación

## 🔧 Endpoints de Gestión de Usuario

### PUT `/api/v1/notifications/{notificationId}/read`
**Marcar notificación como leída**

Marca una notificación del usuario autenticado como leída.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

**Parámetros:**
- `notificationId` (requerido): ID de la notificación

### PUT `/api/v1/notifications/read-multiple`
**Marcar múltiples notificaciones como leídas**

Marca múltiples notificaciones del usuario autenticado como leídas.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

**Body:**
```json
[1, 2, 3, 4, 5]
```

### PUT `/api/v1/notifications/read-all`
**Marcar todas las notificaciones como leídas**

Marca todas las notificaciones del usuario autenticado como leídas.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

### DELETE `/api/v1/notifications/{notificationId}`
**Eliminar notificación**

Elimina una notificación del usuario autenticado.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

**Parámetros:**
- `notificationId` (requerido): ID de la notificación

### DELETE `/api/v1/notifications/multiple`
**Eliminar múltiples notificaciones**

Elimina múltiples notificaciones del usuario autenticado.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

**Body:**
```json
[1, 2, 3, 4, 5]
```

### DELETE `/api/v1/notifications/all`
**Eliminar todas las notificaciones**

Elimina todas las notificaciones del usuario autenticado.

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
| 403 | Prohibido - Acceso denegado |
| 404 | Notificación no encontrada |

## 🧪 Ejemplos de Uso

### Obtener mis notificaciones
```bash
curl -X GET "http://localhost:8080/api/v1/notifications/my-notifications?page=0&size=20" \
  -H "Authorization: Bearer {token}"
```

### Obtener notificaciones no leídas
```bash
curl -X GET "http://localhost:8080/api/v1/notifications/my-notifications/unread?page=0&size=20" \
  -H "Authorization: Bearer {token}"
```

### Obtener notificaciones por tipo
```bash
curl -X GET "http://localhost:8080/api/v1/notifications/my-notifications/type/PROJECT_APPLICATION?page=0&size=20" \
  -H "Authorization: Bearer {token}"
```

### Marcar notificación como leída
```bash
curl -X PUT "http://localhost:8080/api/v1/notifications/1/read" \
  -H "Authorization: Bearer {token}"
```

### Marcar múltiples notificaciones como leídas
```bash
curl -X PUT "http://localhost:8080/api/v1/notifications/read-multiple" \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '[1, 2, 3, 4, 5]'
```

### Marcar todas las notificaciones como leídas
```bash
curl -X PUT "http://localhost:8080/api/v1/notifications/read-all" \
  -H "Authorization: Bearer {token}"
```

### Eliminar notificación
```bash
curl -X DELETE "http://localhost:8080/api/v1/notifications/1" \
  -H "Authorization: Bearer {token}"
```

### Eliminar múltiples notificaciones
```bash
curl -X DELETE "http://localhost:8080/api/v1/notifications/multiple" \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '[1, 2, 3, 4, 5]'
```

### Eliminar todas las notificaciones
```bash
curl -X DELETE "http://localhost:8080/api/v1/notifications/all" \
  -H "Authorization: Bearer {token}"
```

### Crear notificación de aplicación a proyecto (Interno)
```bash
curl -X POST "http://localhost:8080/api/v1/notifications/internal/project-application/1/1"
```

### Crear notificación de aplicación rechazada (Interno)
```bash
curl -X POST "http://localhost:8080/api/v1/notifications/internal/project-application-rejected/1/1" \
  -H "Content-Type: application/json" \
  -d '{
    "reason": "No cumple con los requisitos del proyecto"
  }'
```

### Crear notificación de logro desbloqueado (Interno)
```bash
curl -X POST "http://localhost:8080/api/v1/notifications/internal/achievement-unlocked/1/FIRST_PROJECT" \
  -H "Content-Type: application/json" \
  -d '{
    "achievementName": "Primer Proyecto"
  }'
```
