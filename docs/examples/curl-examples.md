# 🧪 Ejemplos de cURL para DevMatch API

## 🔐 Autenticación

### Iniciar Sesión
```bash
curl -X POST "http://localhost:8080/api/v1/users/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin@devmatch.com",
    "password": "admin123"
  }'
```

### Registrar Usuario
```bash
curl -X POST "http://localhost:8080/api/v1/users/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "nuevo_usuario",
    "email": "nuevo@ejemplo.com",
    "password": "password123",
    "firstName": "María",
    "lastName": "García",
    "profileType": "DEVELOPER"
  }'
```

## 👤 Usuarios

### Obtener Mi Perfil
```bash
curl -X GET "http://localhost:8080/api/v1/users/me" \
  -H "Authorization: Bearer {token}"
```

### Actualizar Mi Perfil
```bash
curl -X PUT "http://localhost:8080/api/v1/users/me" \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Juan Carlos",
    "lastName": "Pérez García",
    "profileImageUrl": "https://example.com/nuevo-avatar.jpg"
  }'
```

### Obtener Perfil Público
```bash
curl -X GET "http://localhost:8080/api/v1/users/1"
```

## 🏆 Logros

### Obtener Todos los Logros
```bash
curl -X GET "http://localhost:8080/api/v1/achievements?page=0&size=10&sort=title,asc"
```

### Obtener Logro por ID
```bash
curl -X GET "http://localhost:8080/api/v1/achievements/1"
```

### Obtener Logro por Código
```bash
curl -X GET "http://localhost:8080/api/v1/achievements/code/FIRST_PROJECT"
```

### Obtener Logros por Tipo
```bash
curl -X GET "http://localhost:8080/api/v1/achievements/type/PROJECT"
```

### Obtener Mis Logros
```bash
curl -X GET "http://localhost:8080/api/v1/users/me/achievements" \
  -H "Authorization: Bearer {token}"
```

### Verificar si Tengo un Logro
```bash
curl -X GET "http://localhost:8080/api/v1/users/me/achievements/FIRST_PROJECT/has" \
  -H "Authorization: Bearer {token}"
```

### Obtener Total de Puntos
```bash
curl -X GET "http://localhost:8080/api/v1/users/me/achievements/points/total" \
  -H "Authorization: Bearer {token}"
```

## 🚀 Proyectos

### Obtener Proyectos Públicos
```bash
curl -X GET "http://localhost:8080/api/v1/projects?page=0&size=10&search=react"
```

### Obtener Proyecto por ID
```bash
curl -X GET "http://localhost:8080/api/v1/projects/1"
```

### Crear Nuevo Proyecto
```bash
curl -X POST "http://localhost:8080/api/v1/projects" \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Mi Nuevo Proyecto",
    "description": "Descripción detallada del proyecto",
    "visibility": "PUBLIC",
    "technologies": ["React", "Node.js", "MongoDB"],
    "tags": ["web", "javascript", "mongodb"],
    "requirements": "Conocimientos en React y Node.js"
  }'
```

### Actualizar Proyecto
```bash
curl -X PUT "http://localhost:8080/api/v1/projects/1" \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Proyecto Actualizado",
    "description": "Nueva descripción del proyecto"
  }'
```

### Eliminar Proyecto
```bash
curl -X DELETE "http://localhost:8080/api/v1/projects/1" \
  -H "Authorization: Bearer {token}"
```

## 💬 Mensajes de Proyecto

### Obtener Mensajes del Proyecto
```bash
curl -X GET "http://localhost:8080/api/v1/projects/1/messages?page=0&size=10" \
  -H "Authorization: Bearer {token}"
```

### Enviar Mensaje
```bash
curl -X POST "http://localhost:8080/api/v1/projects/1/messages" \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "content": "Hola equipo, ¿cómo va el desarrollo?",
    "messageType": "TEXT"
  }'
```

### Responder a Mensaje
```bash
curl -X POST "http://localhost:8080/api/v1/projects/1/messages/5/reply" \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "content": "Todo va bien, estamos avanzando con el frontend",
    "messageType": "TEXT"
  }'
```

### Obtener Hilo de Mensajes
```bash
curl -X GET "http://localhost:8080/api/v1/projects/1/messages/5/thread" \
  -H "Authorization: Bearer {token}"
```

## ⭐ Reseñas de Proyecto

### Obtener Reseñas del Proyecto
```bash
curl -X GET "http://localhost:8080/api/v1/projects/1/reviews" \
  -H "Authorization: Bearer {token}"
```

### Crear Reseña
```bash
curl -X POST "http://localhost:8080/api/v1/projects/1/reviews" \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "rating": 5,
    "comment": "Excelente proyecto, muy bien organizado y buena comunicación del equipo."
  }'
```

### Actualizar Reseña
```bash
curl -X PUT "http://localhost:8080/api/v1/projects/1/reviews/1" \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "rating": 4,
    "comment": "Muy buen proyecto, solo faltó un poco más de documentación."
  }'
```

### Eliminar Reseña
```bash
curl -X DELETE "http://localhost:8080/api/v1/projects/1/reviews/1" \
  -H "Authorization: Bearer {token}"
```

## 🔧 Administración

### Obtener Estadísticas del Sistema
```bash
curl -X GET "http://localhost:8080/api/v1/admin/stats" \
  -H "Authorization: Bearer {admin_token}"
```

### Obtener Todos los Usuarios
```bash
curl -X GET "http://localhost:8080/api/v1/admin/users?page=0&size=10" \
  -H "Authorization: Bearer {admin_token}"
```

### Cambiar Rol de Usuario
```bash
curl -X PUT "http://localhost:8080/api/v1/admin/users/1/role" \
  -H "Authorization: Bearer {admin_token}" \
  -H "Content-Type: application/json" \
  -d '{
    "role": "ADMIN"
  }'
```

### Crear Nuevo Logro
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

### Actualizar Logro
```bash
curl -X PUT "http://localhost:8080/api/v1/admin/achievements/1" \
  -H "Authorization: Bearer {admin_token}" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Logro Actualizado",
    "description": "Nueva descripción del logro",
    "points": 250
  }'
```

### Activar/Desactivar Logro
```bash
curl -X PATCH "http://localhost:8080/api/v1/admin/achievements/1/toggle-status" \
  -H "Authorization: Bearer {admin_token}"
```

### Eliminar Logro
```bash
curl -X DELETE "http://localhost:8080/api/v1/admin/achievements/1" \
  -H "Authorization: Bearer {admin_token}"
```

### Otorgar Logro a Usuario
```bash
curl -X POST "http://localhost:8080/api/v1/admin/users/1/achievements/5" \
  -H "Authorization: Bearer {admin_token}"
```

### Revocar Logro de Usuario
```bash
curl -X DELETE "http://localhost:8080/api/v1/admin/users/1/achievements/5" \
  -H "Authorization: Bearer {admin_token}"
```

## 🔍 Búsquedas Avanzadas

### Búsqueda de Usuarios
```bash
curl -X GET "http://localhost:8080/api/v1/admin/search/users?query=Juan&role=DEVELOPER&status=ACTIVE" \
  -H "Authorization: Bearer {admin_token}"
```

### Búsqueda de Proyectos
```bash
curl -X GET "http://localhost:8080/api/v1/admin/search/projects?query=React&tags=web,javascript" \
  -H "Authorization: Bearer {admin_token}"
```

## 📝 Notas Importantes

1. **Reemplaza `{token}`** con el token JWT obtenido del login
2. **Reemplaza `{admin_token}`** con el token de un usuario administrador
3. **Los IDs numéricos** (1, 5, etc.) son ejemplos, usa los IDs reales de tu base de datos
4. **Algunos endpoints** requieren autenticación, otros son públicos
5. **Los endpoints administrativos** requieren rol de administrador

## 🚀 Scripts de Automatización

### Script para Obtener Token
```bash
#!/bin/bash
# get_token.sh

TOKEN=$(curl -s -X POST "http://localhost:8080/api/v1/users/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin@devmatch.com","password":"admin123"}' | \
  jq -r '.token')

echo "Token obtenido: $TOKEN"
export JWT_TOKEN=$TOKEN
```

### Script para Probar Endpoints
```bash
#!/bin/bash
# test_api.sh

# Obtener token
source get_token.sh

# Probar endpoint
curl -X GET "http://localhost:8080/api/v1/users/me" \
  -H "Authorization: Bearer $JWT_TOKEN"
```
