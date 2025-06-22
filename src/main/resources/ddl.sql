CREATE DATABASE IF NOT EXISTS devmatch_db
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE devmatch_db;

-- ==============================================================================
-- USERS
-- ==============================================================================
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    -- Autenticación y credenciales
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,

    -- Datos personales
    first_name VARCHAR(50),
    last_name VARCHAR(50),

    -- Localización
    country VARCHAR(60) NOT NULL,
    province VARCHAR(60),
    city VARCHAR(60),

    -- Enlaces y descripción profesional
    github_url VARCHAR(255),
    linkedin_url VARCHAR(255),
    portfolio_url VARCHAR(255),
    avatar_url VARCHAR(255),
    bio TEXT,

    -- Rol del usuario (relación directa)
    role_id BIGINT,

    -- Estado de la cuenta
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    -- Trazabilidad
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,

    -- Clave foránea al rol
    CONSTRAINT fk_users_role_id
        FOREIGN KEY (role_id)
        REFERENCES roles(id)
        ON DELETE SET NULL
);

-- Índices
CREATE INDEX idx_users_province ON users(province);
CREATE INDEX idx_users_city ON users(city);
CREATE INDEX idx_users_is_active ON users(is_active);
CREATE INDEX idx_users_is_deleted ON users(is_deleted);
CREATE INDEX idx_users_role_id ON users(role_id);

-- ==============================================================================
-- PROFILE TYPES
-- ==============================================================================
CREATE TABLE profile_types (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE,         -- Ej: BACKEND, FRONTEND, FULLSTACK
    description TEXT                          -- Descripción opcional para mostrar en frontend
);

-- ==============================================================================
-- USER PROFILE TYPES
-- ==============================================================================
CREATE TABLE user_profile_types (
    user_id BIGINT NOT NULL,
    profile_type_id BIGINT NOT NULL,

    -- Estado
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    -- Trazabilidad
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (user_id, profile_type_id),

    CONSTRAINT fk_user_profile_types_user_id
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user_profile_types_profile_type_id
        FOREIGN KEY (profile_type_id)
        REFERENCES profile_types(id)
        ON DELETE CASCADE
);

-- Índices
CREATE INDEX idx_user_profile_types_user_id ON user_profile_types(user_id);
CREATE INDEX idx_user_profile_types_profile_type_id ON user_profile_types(profile_type_id);

-- ==============================================================================
-- TAGS
-- ==============================================================================
CREATE TABLE tags (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,           -- Ej: Java, Angular, UX/UI, Docker
    tag_type VARCHAR(30) NOT NULL,              -- Ej: LANGUAGE, FRAMEWORK, TOOL...

    -- Estado
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    -- Trazabilidad
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
);

-- Índices
CREATE INDEX idx_tags_tag_type ON tags(tag_type);
CREATE INDEX idx_tags_is_active ON tags(is_active);

-- ==============================================================================
-- USER TAGS
-- ==============================================================================
CREATE TABLE user_tags (
    user_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,

    -- Estado
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    -- Trazabilidad
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (user_id, tag_id),

    CONSTRAINT fk_user_tags_user_id
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user_tags_tag_id
        FOREIGN KEY (tag_id)
        REFERENCES tags(id)
        ON DELETE CASCADE
);

-- Índices
CREATE INDEX idx_user_tags_user_id ON user_tags(user_id);
CREATE INDEX idx_user_tags_tag_id ON user_tags(tag_id);

-- ==============================================================================
-- ACHIEVEMENT CATALOG
-- ==============================================================================
CREATE TABLE achievement_catalog (
    code VARCHAR(50) PRIMARY KEY,              -- Ej: FIRST_PROJECT_COMPLETED
    title VARCHAR(100) NOT NULL,               -- Título visible: "¡Primer proyecto completado!"
    description TEXT,                          -- Descripción larga: "Has participado activamente en un proyecto hasta su finalización."
    icon_url VARCHAR(255),                     -- Icono o insignia (opcional)
    
	-- Estado
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    
	-- Trazabilidad
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
);

-- ==============================================================================
-- USER ACHIEVEMENTS
-- ==============================================================================
CREATE TABLE user_achievements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    -- Relación con el usuario y el logro obtenido
    user_id BIGINT NOT NULL,
    achievement_code VARCHAR(50) NOT NULL,  -- Clave al catálogo de logros

    -- Fecha en la que se otorgó el logro
    achieved_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Estado lógico
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    -- Trazabilidad
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,

    -- Restricción para evitar logros duplicados en el mismo usuario
    UNIQUE KEY uq_user_achievement (user_id, achievement_code),

    -- Claves foráneas
    CONSTRAINT fk_user_achievements_user_id
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user_achievements_achievement_code
        FOREIGN KEY (achievement_code)
        REFERENCES achievement_catalog(code)
        ON DELETE CASCADE
);

-- Índices
CREATE INDEX idx_user_achievements_user_id ON user_achievements(user_id);
CREATE INDEX idx_user_achievements_achievement_code ON user_achievements(achievement_code);
CREATE INDEX idx_user_achievements_is_active ON user_achievements(is_active);

-- ==============================================================================
-- ROLES
-- ==============================================================================
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE, -- Ej: USER, ADMIN, MODERATOR
    description TEXT
);

-- ==============================================================================
-- PROJECTS
-- ==============================================================================
CREATE TABLE projects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    -- Información principal
    title VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    status VARCHAR(20) NOT NULL, -- Validated via enum in Java

    -- Propiedad
    owner_id BIGINT,

    -- Metadatos opcionales
    repo_url VARCHAR(255),
    cover_image_url VARCHAR(255),
    estimated_duration_weeks INT,
    max_team_size INT,

    -- Visibilidad y control
    is_public BOOLEAN NOT NULL DEFAULT TRUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    -- Trazabilidad
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,

    -- Clave foránea
    CONSTRAINT fk_projects_owner_id
        FOREIGN KEY (owner_id)
        REFERENCES users(id)
        ON DELETE SET NULL
);

-- Índices
CREATE INDEX idx_projects_status ON projects(status);
CREATE INDEX idx_projects_is_public ON projects(is_public);
CREATE INDEX idx_projects_is_active ON projects(is_active);
CREATE INDEX idx_projects_is_deleted ON projects(is_deleted);
CREATE INDEX idx_projects_owner_id ON projects(owner_id);

-- ==============================================================================
-- PROJECT TAGS
-- ==============================================================================

CREATE TABLE project_tags (
    project_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,

    -- Estado de la participación
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

	-- Trazabilidad
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (project_id, tag_id),

    -- Clave foránea
    CONSTRAINT fk_project_tags_project_id
        FOREIGN KEY (project_id)
        REFERENCES projects(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_project_tags_tag_id
        FOREIGN KEY (tag_id)
        REFERENCES tags(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_project_tags_project_id ON project_tags(project_id);
CREATE INDEX idx_project_tags_tag_id ON project_tags(tag_id);


-- ==============================================================================
-- PROJECT MEMBERS
-- ==============================================================================
CREATE TABLE project_members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    -- Relaciones clave
    project_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,

    -- Rol funcional en el equipo
    member_role VARCHAR(30) NOT NULL, -- Ej: BACKEND, FRONTEND, UX_UI, QA, LEAD...

    -- Relación con el proyecto
    is_owner BOOLEAN NOT NULL DEFAULT FALSE,
    joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    left_at TIMESTAMP NULL,

    -- Estado de la participación
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    -- Trazabilidad
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_project_members_project_id
        FOREIGN KEY (project_id)
        REFERENCES projects(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_project_members_user_id
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    UNIQUE KEY uq_project_members_project_user (project_id, user_id)
);

-- Índices
CREATE INDEX idx_project_members_user_id ON project_members(user_id);
CREATE INDEX idx_project_members_project_id ON project_members(project_id);
CREATE INDEX idx_project_members_member_role ON project_members(member_role);
CREATE INDEX idx_project_members_is_active ON project_members(is_active);
CREATE INDEX idx_project_members_is_deleted ON project_members(is_deleted);

-- ==============================================================================
-- PROJECT MESSAGES
-- ==============================================================================
CREATE TABLE project_messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    -- Relación con el proyecto y el autor del mensaje
    project_id BIGINT NOT NULL,
    sender_id BIGINT NOT NULL,

    -- Contenido del mensaje enviado al grupo
    content TEXT NOT NULL,

    -- Fecha de envío
    sent_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Estado lógico
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    -- Trazabilidad
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,

    -- Claves foráneas
    CONSTRAINT fk_project_messages_project_id
        FOREIGN KEY (project_id)
        REFERENCES projects(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_project_messages_sender_id
        FOREIGN KEY (sender_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

-- Índices
CREATE INDEX idx_project_messages_project_id ON project_messages(project_id);
CREATE INDEX idx_project_messages_sender_id ON project_messages(sender_id);
CREATE INDEX idx_project_messages_is_deleted ON project_messages(is_deleted);

-- ==============================================================================
-- PROJECT APPLICATIONS
-- ==============================================================================
CREATE TABLE project_applications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    -- Relación con el proyecto y el usuario solicitante
    project_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,

    -- Contenido de la solicitud
    motivation_message TEXT,

    -- Estado y control de la solicitud
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING', -- Enum controlado en Java: PENDING, ACCEPTED, REJECTED, CANCELLED
    seen_by_owner BOOLEAN NOT NULL DEFAULT FALSE,

    -- Fechas de solicitud y resolución
    submitted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Cuándo se envió
    resolved_at TIMESTAMP NULL,                                -- Cuándo se aceptó o rechazó

    -- Estado lógico
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    -- Trazabilidad
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,

    -- Restricción de clave única para evitar múltiples solicitudes al mismo proyecto
    UNIQUE KEY uq_project_user (project_id, user_id),

    -- Claves foráneas
    CONSTRAINT fk_project_applications_project_id
        FOREIGN KEY (project_id)
        REFERENCES projects(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_project_applications_user_id
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

-- Índices
CREATE INDEX idx_project_applications_status ON project_applications(status);
CREATE INDEX idx_project_applications_project_id ON project_applications(project_id);
CREATE INDEX idx_project_applications_user_id ON project_applications(user_id);
CREATE INDEX idx_project_applications_seen_by_owner ON project_applications(seen_by_owner);

-- ==============================================================================
-- PROJECT REVIEWS
-- ==============================================================================
CREATE TABLE project_reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    -- Relación con el proyecto y el autor de la review
    project_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,

    -- Contenido de la review
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5), -- Puntuación (1 a 5)
    comment TEXT,
    is_public BOOLEAN NOT NULL DEFAULT TRUE,

    -- Estado lógico
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    -- Trazabilidad
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,

    -- Clave única para evitar múltiples valoraciones del mismo usuario
    UNIQUE KEY uq_project_user_review (project_id, user_id),

    -- Relaciones foráneas
    CONSTRAINT fk_project_reviews_project_id
        FOREIGN KEY (project_id)
        REFERENCES projects(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_project_reviews_user_id
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

-- Índices útiles
CREATE INDEX idx_project_reviews_project_id ON project_reviews(project_id);
CREATE INDEX idx_project_reviews_user_id ON project_reviews(user_id);
CREATE INDEX idx_project_reviews_is_public ON project_reviews(is_public);

-- ==============================================================================
-- USER NOTIFICATIONS
-- ==============================================================================
CREATE TABLE user_notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    -- Usuario que recibe la notificación
    user_id BIGINT NOT NULL,

    -- Texto de la notificación (generado desde el backend)
    message TEXT NOT NULL,

    -- Tipo de notificación (controlado desde Java)
    notification_type VARCHAR(30) NOT NULL,
    -- Ej: 'APPLICATION_STATUS', 'NEW_REVIEW', 'NEW_ACHIEVEMENT'

    -- Relaciones opcionales para redirigir desde frontend
    project_id BIGINT DEFAULT NULL,
    review_id BIGINT DEFAULT NULL,
    achievement_code VARCHAR(50) DEFAULT NULL,

    -- Estado de lectura
    is_read BOOLEAN NOT NULL DEFAULT FALSE,

    -- Estado lógico
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    -- Trazabilidad
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,

    -- Relaciones foráneas opcionales
    CONSTRAINT fk_user_notifications_user_id
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user_notifications_project_id
        FOREIGN KEY (project_id)
        REFERENCES projects(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_user_notifications_review_id
        FOREIGN KEY (review_id)
        REFERENCES project_reviews(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_user_notifications_achievement_code
        FOREIGN KEY (achievement_code)
        REFERENCES achievement_catalog(code)
        ON DELETE SET NULL
);

-- Índices útiles
CREATE INDEX idx_user_notifications_user_id ON user_notifications(user_id);
CREATE INDEX idx_user_notifications_notification_type ON user_notifications(notification_type);
CREATE INDEX idx_user_notifications_is_read ON user_notifications(is_read);

-- ==============================================================================
-- PROJECT REVIEW RESPONSES
-- ==============================================================================
CREATE TABLE project_review_responses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    -- Relación con la review a la que se responde
    review_id BIGINT NOT NULL,

    -- Usuario que responde (controlado desde backend que sea miembro activo del proyecto)
    responder_id BIGINT NOT NULL,

    -- Contenido de la respuesta
    content TEXT NOT NULL,

    -- Trazabilidad
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,

    -- Estado lógico
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    -- Restricciones y claves foráneas
    CONSTRAINT fk_project_review_responses_review_id
        FOREIGN KEY (review_id)
        REFERENCES project_reviews(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_project_review_responses_responder_id
        FOREIGN KEY (responder_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

-- Índices útiles
CREATE INDEX idx_project_review_responses_review_id ON project_review_responses(review_id);
CREATE INDEX idx_project_review_responses_responder_id ON project_review_responses(responder_id);
CREATE INDEX idx_project_review_responses_is_active ON project_review_responses(is_active);
