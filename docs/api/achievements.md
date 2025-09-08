# 📋 API de Logros (Achievements)

## Descripción General
Los endpoints de logros permiten consultar el catálogo de logros disponibles en la plataforma y gestionar los logros de los usuarios.

## 🔗 Endpoints Públicos

### GET `/api/v1/achievements`
**Obtener todos los logros activos**

Retorna una lista paginada de todos los logros disponibles en el catálogo.

**Parámetros:**
- `page` (opcional): Número de página (default: 0)
- `size` (opcional): Tamaño de página (default: 20)
- `sort` (opcional): Campo de ordenación (ej: `title,asc`)

**Respuesta:**
```json
{
  "content": [
    {
      "id": 1,
      "code": "FIRST_PROJECT",
      "title": "Primer Proyecto",
      "description": "Crea tu primer proyecto en la plataforma",
      "points": 100,
      "type": "PROJECT",
      "iconUrl": "https://example.com/icons/first-project.png",
      "createdAt": "2025-01-01T00:00:00",
      "updatedAt": "2025-01-01T00:00:00",
      "active": true,
      "deleted": false
    }
  ],
  "totalElements": 25,
  "totalPages": 3,
  "size": 10,
  "number": 0,
  "first": true,
  "last": false
}
```

### GET `/api/v1/achievements/{achievementId}`
**Obtener logro por ID**

Retorna los detalles de un logro específico usando su ID único.

**Parámetros:**
- `achievementId` (path): ID único del logro

**Respuesta:**
```json
{
  "id": 1,
  "code": "FIRST_PROJECT",
  "title": "Primer Proyecto",
  "description": "Crea tu primer proyecto en la plataforma",
  "points": 100,
  "type": "PROJECT",
  "iconUrl": "https://example.com/icons/first-project.png",
  "createdAt": "2025-01-01T00:00:00",
  "updatedAt": "2025-01-01T00:00:00",
  "active": true,
  "deleted": false
}
```

### GET `/api/v1/achievements/code/{code}`
**Obtener logro por código**

Retorna los detalles de un logro específico usando su código único.

**Parámetros:**
- `code` (path): Código único del logro (ej: `FIRST_PROJECT`)

### GET `/api/v1/achievements/type/{type}`
**Obtener logros por tipo**

Retorna una lista de todos los logros de un tipo específico.

**Parámetros:**
- `type` (path): Tipo de logro (`PROJECT`, `MESSAGE`, `REVIEW`, `ACHIEVEMENT`, `SOCIAL`)

## 👤 Endpoints de Usuario (Requieren Autenticación)

### GET `/api/v1/users/me/achievements`
**Obtener mis logros**

Retorna todos los logros obtenidos por el usuario autenticado.

**Headers:**
- `Authorization: Bearer {token}`

**Respuesta:**
```json
[
  {
    "id": 1,
    "userId": 1,
    "achievementId": 1,
    "achievementCode": "FIRST_PROJECT",
    "achievementTitle": "Primer Proyecto",
    "achievementDescription": "Crea tu primer proyecto en la plataforma",
    "points": 100,
    "obtainedAt": "2025-09-01T10:30:00",
    "createdAt": "2025-09-01T10:30:00",
    "updatedAt": "2025-09-01T10:30:00"
  }
]
```

### GET `/api/v1/users/me/achievements/{achievementCode}`
**Obtener un logro específico**

Retorna los detalles de un logro específico obtenido por el usuario.

### GET `/api/v1/users/me/achievements/{achievementCode}/has`
**Verificar si tengo un logro**

Verifica si el usuario ha obtenido un logro específico.

**Respuesta:**
```json
true
```

### GET `/api/v1/users/me/achievements/points/total`
**Obtener total de puntos**

Retorna el total de puntos acumulados por el usuario.

**Respuesta:**
```json
1250
```

## 🔧 Endpoints Administrativos (Requieren Rol ADMIN)

### GET `/api/v1/admin/achievements`
**Obtener todos los logros (Admin)**

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
**Actualizar logro existente**

Actualiza un logro existente en el sistema.

### PATCH `/api/v1/admin/achievements/{achievementId}/toggle-status`
**Activar/desactivar logro**

Cambia el estado activo/inactivo de un logro.

### DELETE `/api/v1/admin/achievements/{achievementId}`
**Eliminar logro (Soft Delete)**

Elimina un logro del sistema. Los usuarios que ya lo tienen NO lo pierden.

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

## 🧪 Ejemplos de Uso

### Obtener logros con paginación
```bash
curl -X GET "http://localhost:8080/api/v1/achievements?page=0&size=10&sort=title,asc"
```

### Verificar si tengo un logro
```bash
curl -X GET "http://localhost:8080/api/v1/users/me/achievements/FIRST_PROJECT/has" \
  -H "Authorization: Bearer {token}"
```

### Crear nuevo logro (Admin)
```bash
curl -X POST "http://localhost:8080/api/v1/admin/achievements" \
  -H "Authorization: Bearer {admin_token}" \
  -H "Content-Type: application/json" \
  -d '{
    "code": "NEW_ACHIEVEMENT",
    "title": "Nuevo Logro",
    "description": "Descripción del nuevo logro",
    "points": 200,
    "type": "SOCIAL",
    "iconUrl": "https://example.com/icons/new.png"
  }'
```
