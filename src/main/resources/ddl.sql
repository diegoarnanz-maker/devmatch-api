CREATE DATABASE IF NOT EXISTS devmatch_db
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE devmatch_db;

-- ==============================================================================
-- CATÁLOGOS BASE
-- ==============================================================================

-- ROLES
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE, -- Ej: USER, ADMIN, MODERATOR
    description TEXT
);

-- PROFILE TYPES
CREATE TABLE profile_types (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE,         -- Ej: BACKEND, FRONTEND, FULLSTACK
    description TEXT                          -- Descripción opcional para mostrar en frontend
);

-- TAGS
CREATE TABLE tags (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,           -- Ej: Java, Angular, UX/UI, Docker
    tag_type VARCHAR(30) NOT NULL,              -- Ej: LANGUAGE, FRAMEWORK, TOOL...
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
);

-- ACHIEVEMENT CATALOG
CREATE TABLE achievement_catalog (
    code VARCHAR(50) PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    icon_url VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
);

-- ==============================================================================
-- USERS
-- ==============================================================================
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    country VARCHAR(60) NOT NULL,
    province VARCHAR(60),
    city VARCHAR(60),
    github_url VARCHAR(255),
    linkedin_url VARCHAR(255),
    portfolio_url VARCHAR(255),
    avatar_url VARCHAR(255),
    bio TEXT,
    role_id BIGINT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_users_role_id
        FOREIGN KEY (role_id)
        REFERENCES roles(id)
        ON DELETE SET NULL
);

CREATE INDEX idx_users_province ON users(province);
CREATE INDEX idx_users_city ON users(city);
CREATE INDEX idx_users_is_active ON users(is_active);
CREATE INDEX idx_users_is_deleted ON users(is_deleted);
CREATE INDEX idx_users_role_id ON users(role_id);

-- ==============================================================================
-- USER PROFILE TYPES
-- ==============================================================================
CREATE TABLE user_profile_types (
    user_id BIGINT NOT NULL,
    profile_type_id BIGINT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, profile_type_id),
    CONSTRAINT fk_user_profile_types_user_id
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_profile_types_profile_type_id
        FOREIGN KEY (profile_type_id) REFERENCES profile_types(id) ON DELETE CASCADE
);

CREATE INDEX idx_user_profile_types_user_id ON user_profile_types(user_id);
CREATE INDEX idx_user_profile_types_profile_type_id ON user_profile_types(profile_type_id);

-- ==============================================================================
-- USER TAGS
-- ==============================================================================
CREATE TABLE user_tags (
    user_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, tag_id),
    CONSTRAINT fk_user_tags_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_tags_tag_id FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
);

CREATE INDEX idx_user_tags_user_id ON user_tags(user_id);
CREATE INDEX idx_user_tags_tag_id ON user_tags(tag_id);

-- ==============================================================================
-- USER ACHIEVEMENTS
-- ==============================================================================
CREATE TABLE user_achievements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    achievement_code VARCHAR(50) NOT NULL,
    achieved_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uq_user_achievement (user_id, achievement_code),
    CONSTRAINT fk_user_achievements_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_achievements_achievement_code FOREIGN KEY (achievement_code) REFERENCES achievement_catalog(code) ON DELETE CASCADE
);

CREATE INDEX idx_user_achievements_user_id ON user_achievements(user_id);
CREATE INDEX idx_user_achievements_achievement_code ON user_achievements(achievement_code);
CREATE INDEX idx_user_achievements_is_active ON user_achievements(is_active);

-- ==============================================================================
-- PROJECTS
-- ==============================================================================
CREATE TABLE projects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    status VARCHAR(20) NOT NULL,
    owner_id BIGINT,
    repo_url VARCHAR(255),
    cover_image_url VARCHAR(255),
    estimated_duration_weeks INT,
    max_team_size INT,
    is_public BOOLEAN NOT NULL DEFAULT TRUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_projects_owner_id FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE SET NULL
);

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
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (project_id, tag_id),
    CONSTRAINT fk_project_tags_project_id FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    CONSTRAINT fk_project_tags_tag_id FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
);

CREATE INDEX idx_project_tags_project_id ON project_tags(project_id);
CREATE INDEX idx_project_tags_tag_id ON project_tags(tag_id);

-- ==============================================================================
-- PROJECT MEMBERS
-- ==============================================================================
CREATE TABLE project_members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    member_role VARCHAR(30) NOT NULL,
    is_owner BOOLEAN NOT NULL DEFAULT FALSE,
    joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    left_at TIMESTAMP NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_project_members_project_id FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    CONSTRAINT fk_project_members_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY uq_project_members_project_user (project_id, user_id)
);

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
    project_id BIGINT NOT NULL,
    sender_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    sent_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_project_messages_project_id FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    CONSTRAINT fk_project_messages_sender_id FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_project_messages_project_id ON project_messages(project_id);
CREATE INDEX idx_project_messages_sender_id ON project_messages(sender_id);
CREATE INDEX idx_project_messages_is_deleted ON project_messages(is_deleted);

-- ==============================================================================
-- PROJECT APPLICATIONS
-- ==============================================================================
CREATE TABLE project_applications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    motivation_message TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    seen_by_owner BOOLEAN NOT NULL DEFAULT FALSE,
    submitted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uq_project_user (project_id, user_id),
    CONSTRAINT fk_project_applications_project_id FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    CONSTRAINT fk_project_applications_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_project_applications_status ON project_applications(status);
CREATE INDEX idx_project_applications_project_id ON project_applications(project_id);
CREATE INDEX idx_project_applications_user_id ON project_applications(user_id);
CREATE INDEX idx_project_applications_seen_by_owner ON project_applications(seen_by_owner);

-- ==============================================================================
-- PROJECT REVIEWS
-- ==============================================================================
CREATE TABLE project_reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    is_public BOOLEAN NOT NULL DEFAULT TRUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uq_project_user_review (project_id, user_id),
    CONSTRAINT fk_project_reviews_project_id FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    CONSTRAINT fk_project_reviews_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_project_reviews_project_id ON project_reviews(project_id);
CREATE INDEX idx_project_reviews_user_id ON project_reviews(user_id);
CREATE INDEX idx_project_reviews_is_public ON project_reviews(is_public);

-- ==============================================================================
-- PROJECT REVIEW RESPONSES
-- ==============================================================================
CREATE TABLE project_review_responses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    review_id BIGINT NOT NULL,
    responder_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_project_review_responses_review_id FOREIGN KEY (review_id) REFERENCES project_reviews(id) ON DELETE CASCADE,
    CONSTRAINT fk_project_review_responses_responder_id FOREIGN KEY (responder_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_project_review_responses_review_id ON project_review_responses(review_id);
CREATE INDEX idx_project_review_responses_responder_id ON project_review_responses(responder_id);
CREATE INDEX idx_project_review_responses_is_active ON project_review_responses(is_active);

-- ==============================================================================
-- USER NOTIFICATIONS
-- ==============================================================================
CREATE TABLE user_notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    message TEXT NOT NULL,
    notification_type VARCHAR(30) NOT NULL,
    project_id BIGINT DEFAULT NULL,
    review_id BIGINT DEFAULT NULL,
    achievement_code VARCHAR(50) DEFAULT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_notifications_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_notifications_project_id FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE SET NULL,
    CONSTRAINT fk_user_notifications_review_id FOREIGN KEY (review_id) REFERENCES project_reviews(id) ON DELETE SET NULL,
    CONSTRAINT fk_user_notifications_achievement_code FOREIGN KEY (achievement_code) REFERENCES achievement_catalog(code) ON DELETE SET NULL
);

CREATE INDEX idx_user_notifications_user_id ON user_notifications(user_id);
CREATE INDEX idx_user_notifications_notification_type ON user_notifications(notification_type);
CREATE INDEX idx_user_notifications_is_read ON user_notifications(is_read);

-- Eliminar la constraint actual
ALTER TABLE project_reviews DROP CONSTRAINT uq_project_user_review;

-- Crear una nueva constraint que considere solo reviews activas
ALTER TABLE project_reviews 
ADD CONSTRAINT uq_project_user_review_active 
UNIQUE (project_id, user_id, is_active, is_deleted);

ALTER TABLE users ADD COLUMN notifications_enabled BOOLEAN NOT NULL DEFAULT TRUE;

SHOW CREATE TABLE project_reviews;

-- Ejecutar en MySQL después de detener la aplicación
USE devmatch_db;

ALTER TABLE project_reviews 
ADD COLUMN owner_response TEXT NULL AFTER comment,
ADD COLUMN owner_response_public BOOLEAN NOT NULL DEFAULT TRUE AFTER owner_response,
ADD COLUMN owner_response_date TIMESTAMP NULL AFTER owner_response_public;

ALTER TABLE achievement_catalog 
ADD COLUMN points INT NOT NULL DEFAULT 10 AFTER description,
ADD COLUMN type VARCHAR(50) NOT NULL DEFAULT 'GENERAL' AFTER points;

USE devmatch_db;

-- 1. Deshabilitar safe update
SET SQL_SAFE_UPDATES = 0;

-- 2. Eliminar la restricción de clave foránea existente
ALTER TABLE user_notifications DROP FOREIGN KEY fk_user_notifications_achievement_code;

-- 3. Eliminar la tabla
DROP TABLE achievement_catalog;

-- 4. Crear la nueva tabla con la estructura correcta
CREATE TABLE achievement_catalog (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    points INT NOT NULL DEFAULT 10,
    type VARCHAR(50) NOT NULL DEFAULT 'GENERAL',
    icon_url VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
);

-- 5. Recrear la restricción de clave foránea
ALTER TABLE user_notifications 
ADD CONSTRAINT fk_user_notifications_achievement_code 
FOREIGN KEY (achievement_code) REFERENCES achievement_catalog(code) ON DELETE SET NULL;

-- 6. Resetear auto-increment
ALTER TABLE achievement_catalog AUTO_INCREMENT = 1;

-- 7. Volver a habilitar safe update
SET SQL_SAFE_UPDATES = 1;

-- 8. Verificar la nueva estructura
DESCRIBE achievement_catalog;

-- 9. Insertar los datos
INSERT INTO achievement_catalog (code, title, description, points, type, icon_url) VALUES
('PROFILE_COMPLETE', 'Perfil completado', 'Has rellenado todos los campos de tu perfil.', 10, 'GENERAL', 'https://cdn.example.com/icons/profile_complete.png'),
('FIRST_LOGIN', 'Primer inicio de sesión', 'Te has conectado por primera vez a DevMatch.', 10, 'GENERAL', 'https://cdn.example.com/icons/first_login.png'),
('FIRST_PROJECT_CREATED', 'Primer proyecto creado', 'Has publicado tu primer proyecto en DevMatch.', 10, 'GENERAL', 'https://cdn.example.com/icons/project_created.png'),
('THREE_PROJECTS_CREATED', 'Productor activo', 'Has creado tres proyectos en la plataforma.', 10, 'GENERAL', 'https://cdn.example.com/icons/three_projects.png'),
('FIVE_PROJECTS_CREATED', '¡Eres un constructor!', 'Cinco proyectos creados. Vas a toda máquina.', 10, 'GENERAL', 'https://cdn.example.com/icons/five_projects.png'),
('PROJECT_COMPLETED', 'Proyecto completado', 'Has finalizado con éxito un proyecto en el que participabas.', 10, 'GENERAL', 'https://cdn.example.com/icons/project_completed.png'),
('FIRST_PROJECT_JOINED', 'Primer proyecto unido', 'Te has unido como miembro a un proyecto.', 10, 'GENERAL', 'https://cdn.example.com/icons/project_joined.png'),
('FIVE_PROJECTS_PARTICIPATED', 'Miembro activo', 'Has participado en al menos 5 proyectos.', 10, 'GENERAL', 'https://cdn.example.com/icons/active_member.png'),
('TEN_PROJECTS_PARTICIPATED', 'Veterano de DevMatch', 'Diez proyectos en tu historial. ¡Impresionante!', 10, 'GENERAL', 'https://cdn.example.com/icons/veteran.png'),
('FIRST_APPLICATION_SENT', 'Primera solicitud', 'Has enviado tu primera aplicación a un proyecto.', 10, 'GENERAL', 'https://cdn.example.com/icons/application_sent.png'),
('THREE_APPLICATIONS_SENT', 'Aplicador en serie', 'Has enviado tres solicitudes a proyectos distintos.', 10, 'GENERAL', 'https://cdn.example.com/icons/three_applications.png'),
('FIVE_APPLICATIONS_SENT', 'A la caza del equipo', 'Cinco solicitudes enviadas. ¡Sigue buscando tu equipo ideal!', 10, 'GENERAL', 'https://cdn.example.com/icons/five_applications.png'),
('FIRST_REVIEW_WRITTEN', 'Primera review', 'Has valorado tu primer proyecto.', 10, 'GENERAL', 'https://cdn.example.com/icons/review_written.png'),
('FIVE_REVIEWS_WRITTEN', 'Crítico constructivo', 'Has escrito cinco reviews en proyectos distintos.', 10, 'GENERAL', 'https://cdn.example.com/icons/five_reviews.png'),
('RECEIVED_FIRST_REVIEW', 'Recibiste tu primera review', 'Un proyecto donde participaste fue valorado por otro usuario.', 10, 'GENERAL', 'https://cdn.example.com/icons/review_received.png'),
('RECEIVED_TEN_REVIEWS', 'Feedbackador estrella', 'Tus proyectos han recibido al menos 10 reviews.', 10, 'GENERAL', 'https://cdn.example.com/icons/ten_reviews.png'),
('FIRST_RESPONSE_WRITTEN', 'Primera respuesta', 'Has respondido a una review recibida.', 10, 'GENERAL', 'https://cdn.example.com/icons/response_written.png'),
('THREE_RESPONSES_WRITTEN', 'Colaborador activo', 'Has respondido a tres reviews de tus proyectos.', 10, 'GENERAL', 'https://cdn.example.com/icons/three_responses.png'),
('FIRST_TIME_LEADER', 'Líder de equipo', 'Has liderado un proyecto con éxito.', 10, 'GENERAL', 'https://cdn.example.com/icons/team_leader.png'),
('FIVE_TIMES_LEADER', 'Líder nato', 'Has sido líder en cinco proyectos distintos.', 10, 'GENERAL', 'https://cdn.example.com/icons/natural_leader.png'),
('FIVE_TAGS_SELECTED', 'Multidisciplinar', 'Has seleccionado al menos cinco habilidades en tu perfil.', 10, 'GENERAL', 'https://cdn.example.com/icons/multidisciplinary.png'),
('PROFILE_WITH_LINKS', 'Perfil profesional', 'Has completado tu perfil con GitHub, LinkedIn o portfolio.', 10, 'GENERAL', 'https://cdn.example.com/icons/professional_profile.png');

CREATE TABLE message_mentions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    message_id BIGINT NOT NULL,
    mentioned_user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_mentions_message FOREIGN KEY (message_id) REFERENCES project_messages(id) ON DELETE CASCADE,
    CONSTRAINT fk_mentions_user FOREIGN KEY (mentioned_user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE message_reads (
    message_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    read_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_notified BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (message_id, user_id), -- Clave primaria compuesta
    CONSTRAINT fk_reads_message FOREIGN KEY (message_id) REFERENCES project_messages(id) ON DELETE CASCADE,
    CONSTRAINT fk_reads_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

ALTER TABLE project_messages 
ADD COLUMN message_type VARCHAR(20) NOT NULL DEFAULT 'TEXT';

ALTER TABLE project_messages 
ADD COLUMN reply_to_message_id BIGINT NULL,
ADD CONSTRAINT fk_reply_to FOREIGN KEY (reply_to_message_id) REFERENCES project_messages(id) ON DELETE SET NULL;