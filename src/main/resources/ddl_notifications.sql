-- =====================================================
-- Script DDL para la tabla de notificaciones
-- =====================================================

-- Crear la tabla de notificaciones
CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    message VARCHAR(500) NOT NULL,
    notification_type VARCHAR(50) NOT NULL,
    project_id BIGINT NULL,
    review_id BIGINT NULL,
    achievement_code VARCHAR(100) NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Índices para mejorar el rendimiento
    INDEX idx_user_id (user_id),
    INDEX idx_user_read (user_id, is_read),
    INDEX idx_user_type (user_id, notification_type),
    INDEX idx_user_project (user_id, project_id),
    INDEX idx_user_review (user_id, review_id),
    INDEX idx_user_achievement (user_id, achievement_code),
    INDEX idx_created_at (created_at),
    INDEX idx_user_created (user_id, created_at),
    INDEX idx_user_deleted (user_id, is_deleted),
    
    -- Restricciones
    CONSTRAINT chk_message_length CHECK (LENGTH(message) >= 1 AND LENGTH(message) <= 500),
    CONSTRAINT chk_notification_type CHECK (notification_type IN (
        'PROJECT_APPLICATION',
        'PROJECT_APPLICATION_ACCEPTED',
        'PROJECT_APPLICATION_REJECTED',
        'PROJECT_MEMBER_JOINED',
        'PROJECT_REVIEW_RECEIVED',
        'PROJECT_REVIEW_RESPONSE',
        'ACHIEVEMENT_UNLOCKED',
        'WELCOME',
        'SYSTEM'
    )),
    CONSTRAINT chk_achievement_code_length CHECK (achievement_code IS NULL OR LENGTH(achievement_code) <= 100)
);

-- Comentarios en la tabla
ALTER TABLE notifications COMMENT = 'Tabla para almacenar las notificaciones de los usuarios';

-- Comentarios en las columnas
ALTER TABLE notifications 
    MODIFY COLUMN id BIGINT AUTO_INCREMENT COMMENT 'Identificador único de la notificación',
    MODIFY COLUMN user_id BIGINT NOT NULL COMMENT 'ID del usuario destinatario de la notificación',
    MODIFY COLUMN message VARCHAR(500) NOT NULL COMMENT 'Mensaje de la notificación',
    MODIFY COLUMN notification_type VARCHAR(50) NOT NULL COMMENT 'Tipo de notificación',
    MODIFY COLUMN project_id BIGINT NULL COMMENT 'ID del proyecto relacionado (si aplica)',
    MODIFY COLUMN review_id BIGINT NULL COMMENT 'ID de la review relacionada (si aplica)',
    MODIFY COLUMN achievement_code VARCHAR(100) NULL COMMENT 'Código del logro relacionado (si aplica)',
    MODIFY COLUMN is_read BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Indica si la notificación ha sido leída',
    MODIFY COLUMN is_active BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Indica si la notificación está activa',
    MODIFY COLUMN is_deleted BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Indica si la notificación ha sido eliminada (soft delete)',
    MODIFY COLUMN created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha y hora de creación',
    MODIFY COLUMN updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha y hora de última actualización';

-- Crear vista para notificaciones activas (no eliminadas)
CREATE OR REPLACE VIEW v_notifications_active AS
SELECT 
    id,
    user_id,
    message,
    notification_type,
    project_id,
    review_id,
    achievement_code,
    is_read,
    is_active,
    created_at,
    updated_at
FROM notifications 
WHERE is_deleted = FALSE;

-- Crear vista para notificaciones no leídas
CREATE OR REPLACE VIEW v_notifications_unread AS
SELECT 
    id,
    user_id,
    message,
    notification_type,
    project_id,
    review_id,
    achievement_code,
    is_active,
    created_at,
    updated_at
FROM notifications 
WHERE is_deleted = FALSE AND is_read = FALSE;

-- Crear vista para estadísticas de notificaciones por usuario
CREATE OR REPLACE VIEW v_notification_stats AS
SELECT 
    user_id,
    COUNT(*) as total_notifications,
    SUM(CASE WHEN is_read = FALSE THEN 1 ELSE 0 END) as unread_notifications,
    SUM(CASE WHEN notification_type = 'PROJECT_APPLICATION' THEN 1 ELSE 0 END) as project_applications,
    SUM(CASE WHEN notification_type = 'PROJECT_APPLICATION_ACCEPTED' THEN 1 ELSE 0 END) as applications_accepted,
    SUM(CASE WHEN notification_type = 'PROJECT_APPLICATION_REJECTED' THEN 1 ELSE 0 END) as applications_rejected,
    SUM(CASE WHEN notification_type = 'PROJECT_MEMBER_JOINED' THEN 1 ELSE 0 END) as members_joined,
    SUM(CASE WHEN notification_type = 'PROJECT_REVIEW_RECEIVED' THEN 1 ELSE 0 END) as reviews_received,
    SUM(CASE WHEN notification_type = 'ACHIEVEMENT_UNLOCKED' THEN 1 ELSE 0 END) as achievements_unlocked,
    SUM(CASE WHEN notification_type = 'WELCOME' THEN 1 ELSE 0 END) as welcome_notifications,
    SUM(CASE WHEN notification_type = 'SYSTEM' THEN 1 ELSE 0 END) as system_notifications,
    MAX(created_at) as last_notification_date
FROM notifications 
WHERE is_deleted = FALSE
GROUP BY user_id;

-- Crear procedimiento almacenado para limpiar notificaciones antiguas
DELIMITER //
CREATE PROCEDURE CleanOldNotifications(IN days_to_keep INT)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    
    START TRANSACTION;
    
    -- Marcar como eliminadas las notificaciones más antiguas que el número de días especificado
    UPDATE notifications 
    SET is_deleted = TRUE, 
        updated_at = CURRENT_TIMESTAMP
    WHERE created_at < DATE_SUB(NOW(), INTERVAL days_to_keep DAY)
    AND is_deleted = FALSE;
    
    COMMIT;
    
    SELECT ROW_COUNT() as notifications_marked_as_deleted;
END //
DELIMITER ;

-- Crear procedimiento almacenado para obtener notificaciones con paginación
DELIMITER //
CREATE PROCEDURE GetUserNotificationsPaginated(
    IN p_user_id BIGINT,
    IN p_page INT,
    IN p_size INT
)
BEGIN
    DECLARE p_offset INT;
    SET p_offset = p_page * p_size;
    
    SELECT 
        id,
        user_id,
        message,
        notification_type,
        project_id,
        review_id,
        achievement_code,
        is_read,
        is_active,
        created_at,
        updated_at
    FROM notifications 
    WHERE user_id = p_user_id 
    AND is_deleted = FALSE
    ORDER BY created_at DESC
    LIMIT p_size OFFSET p_offset;
    
    -- También retornar el conteo total
    SELECT COUNT(*) as total_count
    FROM notifications 
    WHERE user_id = p_user_id 
    AND is_deleted = FALSE;
END //
DELIMITER ;

-- Crear trigger para validar datos antes de insertar
DELIMITER //
CREATE TRIGGER tr_notifications_before_insert
BEFORE INSERT ON notifications
FOR EACH ROW
BEGIN
    -- Validar que el mensaje no esté vacío
    IF NEW.message IS NULL OR TRIM(NEW.message) = '' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El mensaje de la notificación no puede estar vacío';
    END IF;
    
    -- Validar que el tipo de notificación sea válido
    IF NEW.notification_type NOT IN (
        'PROJECT_APPLICATION',
        'PROJECT_APPLICATION_ACCEPTED',
        'PROJECT_APPLICATION_REJECTED',
        'PROJECT_MEMBER_JOINED',
        'PROJECT_REVIEW_RECEIVED',
        'PROJECT_REVIEW_RESPONSE',
        'ACHIEVEMENT_UNLOCKED',
        'WELCOME',
        'SYSTEM'
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Tipo de notificación no válido';
    END IF;
    
    -- Establecer valores por defecto si no se proporcionan
    IF NEW.is_read IS NULL THEN
        SET NEW.is_read = FALSE;
    END IF;
    
    IF NEW.is_active IS NULL THEN
        SET NEW.is_active = TRUE;
    END IF;
    
    IF NEW.is_deleted IS NULL THEN
        SET NEW.is_deleted = FALSE;
    END IF;
    
    IF NEW.created_at IS NULL THEN
        SET NEW.created_at = CURRENT_TIMESTAMP;
    END IF;
    
    IF NEW.updated_at IS NULL THEN
        SET NEW.updated_at = CURRENT_TIMESTAMP;
    END IF;
END //
DELIMITER ;

-- Crear trigger para actualizar updated_at automáticamente
DELIMITER //
CREATE TRIGGER tr_notifications_before_update
BEFORE UPDATE ON notifications
FOR EACH ROW
BEGIN
    SET NEW.updated_at = CURRENT_TIMESTAMP;
END //
DELIMITER ;

-- Insertar datos de ejemplo (opcional)
INSERT INTO notifications (user_id, message, notification_type, project_id, created_at) VALUES
(1, '¡Bienvenido a DevMatch! Estamos emocionados de tenerte con nosotros.', 'WELCOME', NULL, NOW()),
(1, 'Has aplicado al proyecto: Sistema de Gestión de Inventarios', 'PROJECT_APPLICATION', 1, NOW()),
(2, 'Tu aplicación al proyecto "App de Delivery" ha sido aceptada', 'PROJECT_APPLICATION_ACCEPTED', 2, NOW()),
(3, 'Has desbloqueado el logro: Primer Proyecto Completado', 'ACHIEVEMENT_UNLOCKED', NULL, NOW()),
(1, 'Juan Pérez se ha unido al proyecto: Plataforma de E-learning', 'PROJECT_MEMBER_JOINED', 3, NOW()),
(2, 'Has recibido una review en tu proyecto: API REST', 'PROJECT_REVIEW_RECEIVED', 4, NOW()),
(1, 'Mantenimiento programado: El sistema estará offline por 2 horas', 'SYSTEM', NULL, NOW());

-- Mostrar información de la tabla creada
SELECT 
    TABLE_NAME,
    TABLE_ROWS,
    DATA_LENGTH,
    INDEX_LENGTH,
    (DATA_LENGTH + INDEX_LENGTH) as TOTAL_SIZE
FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = DATABASE() 
AND TABLE_NAME = 'notifications'; 