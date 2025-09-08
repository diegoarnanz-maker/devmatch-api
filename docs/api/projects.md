# 🚀 API de Proyectos

## Descripción General
Los endpoints de proyectos permiten gestionar proyectos, colaboraciones, mensajes y reseñas dentro de la plataforma.

## 📋 Endpoints de Proyectos

### GET `/api/v1/projects`
**Obtener proyectos públicos**

Retorna una lista paginada de proyectos públicos disponibles.

**Parámetros:**
- `page` (opcional): Número de página (default: 0)
- `size` (opcional): Tamaño de página (default: 20)
- `search` (opcional): Búsqueda por título o descripción
- `tags` (opcional): Filtro por tags (separados por coma)
- `sort` (opcional): Ordenación (ej: `createdAt,desc`)

**Respuesta:**
```json
{
  "content": [
    {
      "id": 1,
      "title": "Mi Proyecto Web",
      "description": "Una aplicación web moderna con React y Spring Boot",
      "status": "ACTIVE",
      "visibility": "PUBLIC",
      "technologies": ["React", "Spring Boot", "MySQL"],
      "tags": ["web", "react", "spring"],
      "owner": {
        "id": 1,
        "username": "usuario@ejemplo.com",
        "firstName": "Juan",
        "lastName": "Pérez",
        "profileImageUrl": "https://example.com/avatar.jpg"
      },
      "createdAt": "2025-01-01T00:00:00",
      "updatedAt": "2025-09-08T10:00:00"
    }
  ],
  "totalElements": 50,
  "totalPages": 3,
  "size": 20,
  "number": 0
}
```

### GET `/api/v1/projects/{projectId}`
**Obtener proyecto por ID**

Retorna los detalles completos de un proyecto específico.

### POST `/api/v1/projects`
**Crear nuevo proyecto**

Crea un nuevo proyecto en la plataforma.

**Headers:**
- `Authorization: Bearer {token}`

**Body:**
```json
{
  "title": "Mi Nuevo Proyecto",
  "description": "Descripción detallada del proyecto",
  "visibility": "PUBLIC",
  "technologies": ["React", "Node.js", "MongoDB"],
  "tags": ["web", "javascript", "mongodb"],
  "requirements": "Conocimientos en React y Node.js"
}
```

**Respuesta:**
```json
{
  "id": 15,
  "title": "Mi Nuevo Proyecto",
  "description": "Descripción detallada del proyecto",
  "status": "ACTIVE",
  "visibility": "PUBLIC",
  "technologies": ["React", "Node.js", "MongoDB"],
  "tags": ["web", "javascript", "mongodb"],
  "owner": {
    "id": 1,
    "username": "usuario@ejemplo.com",
    "firstName": "Juan",
    "lastName": "Pérez"
  },
  "createdAt": "2025-09-08T10:00:00",
  "updatedAt": "2025-09-08T10:00:00"
}
```

### PUT `/api/v1/projects/{projectId}`
**Actualizar proyecto**

Actualiza un proyecto existente (solo el propietario).

### DELETE `/api/v1/projects/{projectId}`
**Eliminar proyecto**

Elimina un proyecto (solo el propietario).

## 👥 Endpoints de Colaboración

### POST `/api/v1/projects/{projectId}/join`
**Unirse a proyecto**

Solicita unirse a un proyecto como colaborador.

**Headers:**
- `Authorization: Bearer {token}`

**Body:**
```json
{
  "message": "Me interesa colaborar en este proyecto. Tengo experiencia en React."
}
```

### GET `/api/v1/projects/{projectId}/collaborators`
**Obtener colaboradores**

Retorna la lista de colaboradores del proyecto.

### PUT `/api/v1/projects/{projectId}/collaborators/{userId}/approve`
**Aprobar colaborador**

Aprueba la solicitud de un colaborador (solo el propietario).

### DELETE `/api/v1/projects/{projectId}/collaborators/{userId}`
**Remover colaborador**

Remueve un colaborador del proyecto (solo el propietario).

## 💬 Endpoints de Mensajes

### GET `/api/v1/projects/{projectId}/messages`
**Obtener mensajes del proyecto**

Retorna los mensajes del proyecto con paginación.

**Parámetros:**
- `page` (opcional): Número de página
- `size` (opcional): Tamaño de página
- `thread` (opcional): ID del hilo específico

### POST `/api/v1/projects/{projectId}/messages`
**Enviar mensaje**

Envía un mensaje al proyecto.

**Headers:**
- `Authorization: Bearer {token}`

**Body:**
```json
{
  "content": "Hola equipo, ¿cómo va el desarrollo?",
  "messageType": "TEXT"
}
```

### POST `/api/v1/projects/{projectId}/messages/{messageId}/reply`
**Responder a mensaje**

Responde a un mensaje específico.

**Body:**
```json
{
  "content": "Todo va bien, estamos avanzando con el frontend",
  "messageType": "TEXT"
}
```

### GET `/api/v1/projects/{projectId}/messages/{messageId}/thread`
**Obtener hilo de mensajes**

Retorna el hilo completo de un mensaje y sus respuestas.

## ⭐ Endpoints de Reseñas

### GET `/api/v1/projects/{projectId}/reviews`
**Obtener reseñas del proyecto**

Retorna las reseñas del proyecto.

### POST `/api/v1/projects/{projectId}/reviews`
**Crear reseña**

Crea una reseña del proyecto.

**Headers:**
- `Authorization: Bearer {token}`

**Body:**
```json
{
  "rating": 5,
  "comment": "Excelente proyecto, muy bien organizado y buena comunicación del equipo."
}
```

### PUT `/api/v1/projects/{projectId}/reviews/{reviewId}`
**Actualizar reseña**

Actualiza una reseña existente (solo el autor).

### DELETE `/api/v1/projects/{projectId}/reviews/{reviewId}`
**Eliminar reseña**

Elimina una reseña (solo el autor).

## 📊 Códigos de Error

| Código | Descripción |
|--------|-------------|
| 200 | Éxito |
| 201 | Creado exitosamente |
| 204 | Eliminado exitosamente |
| 400 | Datos de entrada inválidos |
| 401 | No autorizado - Token JWT inválido |
| 403 | Prohibido - Acceso denegado |
| 404 | Proyecto no encontrado |
| 409 | Conflicto - Ya eres colaborador |

## 📝 Aplicaciones a Proyectos

### POST `/api/v1/project-applications/apply/{projectId}`
**Aplicar a proyecto**

Permite a un usuario autenticado aplicar a un proyecto específico.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

**Body:**
```json
{
  "motivationMessage": "Me interesa mucho este proyecto porque..."
}
```

**Respuesta:**
- `201` - Aplicación enviada exitosamente
- `400` - Datos de entrada inválidos
- `401` - No autorizado
- `404` - Proyecto no encontrado
- `409` - Ya has aplicado a este proyecto

### GET `/api/v1/project-applications/project/{projectId}`
**Obtener aplicaciones de proyecto**

Permite al owner de un proyecto ver todas las aplicaciones recibidas.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

**Respuesta:**
```json
[
  {
    "id": 1,
    "projectId": 1,
    "applicantId": 2,
    "applicantUsername": "usuario@ejemplo.com",
    "motivationMessage": "Me interesa mucho este proyecto...",
    "status": "PENDING",
    "appliedAt": "2025-09-08T10:00:00"
  }
]
```

### GET `/api/v1/project-applications/applications/my`
**Obtener mis aplicaciones**

Permite a un usuario ver todas sus candidaturas a proyectos.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

## 💬 Mensajes en Proyectos

### GET `/api/v1/projects/{projectId}/messages`
**Obtener mensajes del proyecto**

Obtiene todos los mensajes de un proyecto de forma paginada.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

**Parámetros de consulta:**
- `page` (opcional): Número de página (default: 0)
- `size` (opcional): Tamaño de página (default: 20)
- `sort` (opcional): Ordenación (ej: `sentAt,desc`)

### POST `/api/v1/projects/{projectId}/messages`
**Enviar mensaje**

Envía un nuevo mensaje en un proyecto.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

**Body:**
```json
{
  "content": "Hola equipo, ¿cómo va el proyecto?",
  "messageType": "TEXT"
}
```

### POST `/api/v1/projects/{projectId}/messages/{messageId}/reply`
**Responder a mensaje**

Responde a un mensaje específico en un proyecto.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

**Body:**
```json
{
  "content": "Esta es mi respuesta al mensaje anterior",
  "messageType": "TEXT"
}
```

### GET `/api/v1/projects/{projectId}/messages/{messageId}/thread`
**Obtener hilo de conversación**

Obtiene el historial completo de mensajes de un hilo de conversación.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

### GET `/api/v1/projects/{projectId}/messages/unread`
**Obtener mensajes no leídos**

Obtiene los mensajes no leídos de un usuario en un proyecto.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

### PATCH `/api/v1/projects/{projectId}/messages/{messageId}/read`
**Marcar mensaje como leído**

Marca un mensaje específico como leído.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

### PATCH `/api/v1/projects/{projectId}/messages/read-all`
**Marcar todos los mensajes como leídos**

Marca todos los mensajes de un proyecto como leídos para un usuario.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

### PUT `/api/v1/projects/{projectId}/messages/{messageId}`
**Editar mensaje**

Edita el contenido de un mensaje existente (solo el autor).

**Headers:**
- `Authorization: Bearer {token}` (requerido)

**Body:**
```json
{
  "content": "Mensaje editado con nuevo contenido"
}
```

### DELETE `/api/v1/projects/{projectId}/messages/{messageId}`
**Eliminar mensaje**

Elimina un mensaje (soft delete, solo el autor).

**Headers:**
- `Authorization: Bearer {token}` (requerido)

## ⭐ Reseñas de Proyectos

### GET `/api/v1/reviews/project/{projectId}`
**Obtener reseñas de proyecto**

Obtiene todas las reseñas de un proyecto específico de forma paginada.

**Parámetros de consulta:**
- `page` (opcional): Número de página (default: 0)
- `size` (opcional): Tamaño de página (default: 10)
- `sort` (opcional): Ordenación (ej: `createdAt,desc`)

### GET `/api/v1/reviews/{reviewId}`
**Obtener reseña por ID**

Obtiene los detalles de una reseña específica por su ID.

### POST `/api/v1/reviews`
**Crear reseña**

Crea una nueva reseña para un proyecto (solo proyectos completados).

**Headers:**
- `Authorization: Bearer {token}` (requerido)

**Body:**
```json
{
  "projectId": 1,
  "rating": 5,
  "comment": "Excelente proyecto, muy bien organizado y buena comunicación del equipo."
}
```

### PUT `/api/v1/reviews/{reviewId}`
**Actualizar reseña**

Actualiza una reseña existente (solo el autor).

**Headers:**
- `Authorization: Bearer {token}` (requerido)

**Body:**
```json
{
  "rating": 4,
  "comment": "Muy buen proyecto, aunque podría mejorar en algunos aspectos."
}
```

### DELETE `/api/v1/reviews/{reviewId}`
**Eliminar reseña**

Elimina una reseña del sistema (solo el autor).

**Headers:**
- `Authorization: Bearer {token}` (requerido)

### GET `/api/v1/reviews/my-reviews`
**Obtener mis reseñas**

Obtiene todas las reseñas del usuario autenticado.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

### POST `/api/v1/reviews/{reviewId}/response`
**Responder a reseña**

Permite al propietario del proyecto responder a una reseña.

**Headers:**
- `Authorization: Bearer {token}` (requerido)

**Body:**
```json
{
  "responseMessage": "Gracias por tu reseña. Estamos trabajando en mejorar esos aspectos."
}
```

## 🔧 Administración de Reseñas

### GET `/api/v1/admin/reviews`
**Obtener todas las reseñas (Admin)**

Obtiene todas las reseñas del sistema, opcionalmente filtradas por proyecto.

**Headers:**
- `Authorization: Bearer {token}` (requerido - Solo ADMIN)

**Parámetros de consulta:**
- `projectId` (opcional): ID del proyecto para filtrar
- `page` (opcional): Número de página (default: 0)
- `size` (opcional): Tamaño de página (default: 20)
- `sort` (opcional): Ordenación (ej: `createdAt,desc`)

### DELETE `/api/v1/admin/reviews/{reviewId}`
**Eliminar reseña (Admin)**

Elimina una reseña por su ID (solo administradores).

**Headers:**
- `Authorization: Bearer {token}` (requerido - Solo ADMIN)

## 🧪 Ejemplos de Uso

### Crear proyecto
```bash
curl -X POST "http://localhost:8080/api/v1/projects" \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Mi Nuevo Proyecto",
    "description": "Descripción del proyecto",
    "visibility": "PUBLIC",
    "technologies": ["React", "Node.js"],
    "tags": ["web", "javascript"]
  }'
```

### Enviar mensaje
```bash
curl -X POST "http://localhost:8080/api/v1/projects/1/messages" \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "content": "Hola equipo!",
    "messageType": "TEXT"
  }'
```

### Aplicar a proyecto
```bash
curl -X POST "http://localhost:8080/api/v1/project-applications/apply/1" \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "motivationMessage": "Me interesa mucho este proyecto porque..."
  }'
```

### Enviar mensaje
```bash
curl -X POST "http://localhost:8080/api/v1/projects/1/messages" \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "content": "Hola equipo, ¿cómo va el proyecto?",
    "messageType": "TEXT"
  }'
```

### Responder a mensaje
```bash
curl -X POST "http://localhost:8080/api/v1/projects/1/messages/5/reply" \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "content": "Esta es mi respuesta al mensaje anterior",
    "messageType": "TEXT"
  }'
```

### Obtener hilo de conversación
```bash
curl -X GET "http://localhost:8080/api/v1/projects/1/messages/5/thread" \
  -H "Authorization: Bearer {token}"
```

### Marcar mensaje como leído
```bash
curl -X PATCH "http://localhost:8080/api/v1/projects/1/messages/5/read" \
  -H "Authorization: Bearer {token}"
```

### Crear reseña
```bash
curl -X POST "http://localhost:8080/api/v1/reviews" \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "projectId": 1,
    "rating": 5,
    "comment": "Excelente proyecto, muy bien organizado!"
  }'
```

### Obtener reseñas de proyecto
```bash
curl -X GET "http://localhost:8080/api/v1/reviews/project/1?page=0&size=10&sort=createdAt,desc"
```

### Responder a reseña
```bash
curl -X POST "http://localhost:8080/api/v1/reviews/1/response" \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "responseMessage": "Gracias por tu reseña. Estamos trabajando en mejorar esos aspectos."
  }'
```

### Obtener mis reseñas
```bash
curl -X GET "http://localhost:8080/api/v1/reviews/my-reviews?page=0&size=10" \
  -H "Authorization: Bearer {token}"
```

### Obtener todas las reseñas (Admin)
```bash
curl -X GET "http://localhost:8080/api/v1/admin/reviews?projectId=1&page=0&size=20" \
  -H "Authorization: Bearer {token}"
```
