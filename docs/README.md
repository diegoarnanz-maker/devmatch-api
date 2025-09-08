# 📚 Documentación de la API DevMatch

## 🎯 Descripción General
DevMatch es una plataforma colaborativa para desarrolladores que permite crear proyectos, colaborar, obtener logros y gestionar una comunidad de desarrolladores.

## 📋 Índice de Documentación

### 🔗 APIs por Módulo
- [**👤 Usuarios**](./api/users.md) - Autenticación, perfiles y gestión de usuarios
- [**🔔 Notificaciones**](./api/notifications.md) - Sistema de notificaciones del usuario
- [**🏆 Logros**](./api/achievements.md) - Sistema de logros y gamificación
- [**🚀 Proyectos**](./api/projects.md) - Gestión de proyectos y colaboración
- [**🔧 Administración**](./api/admin.md) - Endpoints administrativos del sistema

### 📊 Estado de Documentación OpenAPI
**✅ Controladores Documentados (18/18):**
- `auth-controller` - Autenticación y registro
- `profile-controller` - Gestión de perfil de usuario
- `admin-user-controller` - Administración de usuarios
- `notification-controller` - Gestión de notificaciones del sistema
- `achievement-controller` - Catálogo de logros públicos
- `user-achievement-controller` - Logros del usuario
- `admin-achievement-controller` - Administración de logros
- `admin-user-achievement-controller` - Logros de usuarios específicos
- `achievement-trigger-controller` - Triggers automáticos
- `project-controller` - Gestión de proyectos
- `project-application-controller` - Aplicaciones a proyectos
- `project-message-controller` - Mensajes en proyectos
- `review-controller` - Reseñas de proyectos
- `admin-review-controller` - Administración de reseñas
- `role-controller` - Gestión de roles del sistema
- `user-tag-controller` - Gestión de tags de usuario
- `admin-tag-controller` - Administración de tags

### 🧪 Herramientas de Testing
- [**Postman Collection**](./examples/postman-collection.json) - Colección completa para Postman
- [**Ejemplos de cURL**](./examples/curl-examples.md) - Comandos de cURL para testing

### 📖 Guías
- [**Guía de Inicio Rápido**](./guides/quick-start.md) - Configuración y primeros pasos
- [**Guía de Autenticación**](./guides/authentication.md) - Cómo usar JWT y autenticación
- [**Guía de Desarrollo**](./guides/development.md) - Configuración del entorno de desarrollo

## 🚀 Inicio Rápido

### 1. Configuración del Entorno
```bash
# Clonar el repositorio
git clone https://github.com/tu-usuario/devmatch-api.git

# Instalar dependencias
mvn clean install

# Configurar base de datos
# Editar application.properties con tus credenciales de MySQL

# Ejecutar la aplicación
mvn spring-boot:run
```

### 2. Acceder a la Documentación Interactiva
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

### 3. Obtener Token de Autenticación
```bash
curl -X POST "http://localhost:8080/api/v1/users/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin@devmatch.com",
    "password": "admin123"
  }'
```

## 🔐 Autenticación

La API utiliza **JWT (JSON Web Tokens)** para autenticación. Incluye el token en el header `Authorization`:

```bash
Authorization: Bearer {tu_token_jwt}
```

### Roles Disponibles
- **ADMIN**: Acceso completo al sistema
- **DEVELOPER**: Usuario estándar con acceso a funcionalidades básicas
- **OWNER**: Propietario de proyectos

## 📊 Estructura de la API

### Endpoints Públicos
- `GET /api/v1/achievements` - Catálogo de logros
- `GET /api/v1/projects` - Proyectos públicos
- `POST /api/v1/users/auth/login` - Iniciar sesión
- `POST /api/v1/users/auth/register` - Registrarse

### Endpoints de Usuario (Requieren Autenticación)
- `GET /api/v1/users/me` - Mi perfil
- `GET /api/v1/users/me/achievements` - Mis logros
- `POST /api/v1/projects` - Crear proyecto
- `POST /api/v1/projects/{id}/messages` - Enviar mensaje

### Endpoints Administrativos (Requieren Rol ADMIN)
- `GET /api/v1/admin/users` - Gestión de usuarios
- `POST /api/v1/admin/achievements` - Crear logros
- `GET /api/v1/admin/stats` - Estadísticas del sistema

## 🧪 Testing

### Usando Postman
1. Importa la colección: `docs/examples/postman-collection.json`
2. Configura la variable `base_url`: `http://localhost:8080`
3. Ejecuta el endpoint de login para obtener el token
4. Configura la variable `jwt_token` con el token obtenido

### Usando cURL
```bash
# Obtener token
TOKEN=$(curl -s -X POST "http://localhost:8080/api/v1/users/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin@devmatch.com","password":"admin123"}' | \
  jq -r '.token')

# Usar token en requests
curl -X GET "http://localhost:8080/api/v1/users/me" \
  -H "Authorization: Bearer $TOKEN"
```

## 📈 Códigos de Estado HTTP

| Código | Descripción |
|--------|-------------|
| 200 | Éxito |
| 201 | Creado exitosamente |
| 204 | Eliminado exitosamente |
| 400 | Datos de entrada inválidos |
| 401 | No autorizado |
| 403 | Prohibido - Acceso denegado |
| 404 | Recurso no encontrado |
| 409 | Conflicto - Recurso ya existe |
| 500 | Error interno del servidor |

## 🔧 Configuración

### Variables de Entorno
```properties
# Base de datos
DB_HOST=localhost
DB_PORT=3306
DB_NAME=devmatch
DB_USERNAME=root
DB_PASSWORD=password

# JWT
JWT_SECRET=tu_secreto_jwt_muy_seguro
JWT_EXPIRATION=86400

# Swagger
SWAGGER_ENABLED=true
```

### Perfiles de Spring
- **dev**: Desarrollo local
- **test**: Testing
- **prod**: Producción

## 🤝 Contribuir

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -m 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un Pull Request

## 📞 Soporte

- **Documentación**: [docs/](./)
- **Issues**: [GitHub Issues](https://github.com/tu-usuario/devmatch-api/issues)
- **Email**: soporte@devmatch.com

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo [LICENSE](../LICENSE) para más detalles.

---

**Desarrollado con ❤️ por el equipo de DevMatch**
