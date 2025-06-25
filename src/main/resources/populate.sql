-- ROLES
INSERT INTO roles (name, description) VALUES
('ADMIN', 'Administrador con acceso total'),
('USER', 'Usuario estándar con permisos limitados');

-- PROFILE TYPES
INSERT INTO profile_types (name, description) VALUES
('BACKEND', 'Especializado en desarrollo del lado servidor'),
('FRONTEND', 'Especializado en interfaces y experiencia de usuario'),
('FULLSTACK', 'Domina tanto frontend como backend'),
('UX_UI', 'Diseño de experiencia e interfaz de usuario'),
('QA', 'Pruebas y aseguramiento de calidad'),
('DEVOPS', 'Despliegue continuo e infraestructura');

INSERT INTO user_profile_types (user_id, profile_type_id) VALUES
(1, 1), -- Usuario 1 como BACKEND
(2, 3); -- Usuario 2 como FULLSTACK

-- TAGS
-- Lenguajes
INSERT INTO tags (name, tag_type) VALUES
('Java', 'LANGUAGE'),
('JavaScript', 'LANGUAGE'),
('TypeScript', 'LANGUAGE'),
('Python', 'LANGUAGE'),
('PHP', 'LANGUAGE'),
('Kotlin', 'LANGUAGE'),
('Swift', 'LANGUAGE'),
('C#', 'LANGUAGE'),
('Dart', 'LANGUAGE');

-- Frameworks
INSERT INTO tags (name, tag_type) VALUES
('Spring Boot', 'FRAMEWORK'),
('Laravel', 'FRAMEWORK'),
('Symfony', 'FRAMEWORK'),
('Django', 'FRAMEWORK'),
('Flask', 'FRAMEWORK'),
('Angular', 'FRAMEWORK'),
('React', 'FRAMEWORK'),
('Vue.js', 'FRAMEWORK'),
('Next.js', 'FRAMEWORK'),
('ASP.NET Core', 'FRAMEWORK'),
('Node.js', 'FRAMEWORK'),
('Express', 'FRAMEWORK');

-- Herramientas
INSERT INTO tags (name, tag_type) VALUES
('Docker', 'TOOL'),
('Kubernetes', 'TOOL'),
('Git', 'TOOL'),
('Figma', 'TOOL'),
('Postman', 'TOOL'),
('Swagger', 'TOOL'),
('Jenkins', 'TOOL'),
('VS Code', 'TOOL'),
('Firebase', 'TOOL');

-- Bases de datos
INSERT INTO tags (name, tag_type) VALUES
('MySQL', 'DATABASE'),
('PostgreSQL', 'DATABASE'),
('MongoDB', 'DATABASE'),
('SQLite', 'DATABASE'),
('Oracle DB', 'DATABASE'),
('SQL Server', 'DATABASE');

-- Mobile
INSERT INTO tags (name, tag_type) VALUES
('Flutter', 'MOBILE'),
('React Native', 'MOBILE'),
('Android Studio', 'MOBILE'),
('SwiftUI', 'MOBILE'),
('Jetpack Compose', 'MOBILE');

-- ACHIEVEMENT CATALOG
INSERT INTO achievement_catalog (code, title, description, icon_url) VALUES
('PROFILE_COMPLETE', 'Perfil completado', 'Has rellenado todos los campos de tu perfil.', 'https://cdn.example.com/icons/profile_complete.png'),
('FIRST_LOGIN', 'Primer inicio de sesión', 'Te has conectado por primera vez a DevMatch.', 'https://cdn.example.com/icons/first_login.png'),
('FIRST_PROJECT_CREATED', 'Primer proyecto creado', 'Has publicado tu primer proyecto en DevMatch.', 'https://cdn.example.com/icons/project_created.png'),
('THREE_PROJECTS_CREATED', 'Productor activo', 'Has creado tres proyectos en la plataforma.', 'https://cdn.example.com/icons/three_projects.png'),
('FIVE_PROJECTS_CREATED', '¡Eres un constructor!', 'Cinco proyectos creados. Vas a toda máquina.', 'https://cdn.example.com/icons/five_projects.png'),
('PROJECT_COMPLETED', 'Proyecto completado', 'Has finalizado con éxito un proyecto en el que participabas.', 'https://cdn.example.com/icons/project_completed.png'),
('FIRST_PROJECT_JOINED', 'Primer proyecto unido', 'Te has unido como miembro a un proyecto.', 'https://cdn.example.com/icons/project_joined.png'),
('FIVE_PROJECTS_PARTICIPATED', 'Miembro activo', 'Has participado en al menos 5 proyectos.', 'https://cdn.example.com/icons/active_member.png'),
('TEN_PROJECTS_PARTICIPATED', 'Veterano de DevMatch', 'Diez proyectos en tu historial. ¡Impresionante!', 'https://cdn.example.com/icons/veteran.png'),
('FIRST_APPLICATION_SENT', 'Primera solicitud', 'Has enviado tu primera aplicación a un proyecto.', 'https://cdn.example.com/icons/application_sent.png'),
('THREE_APPLICATIONS_SENT', 'Aplicador en serie', 'Has enviado tres solicitudes a proyectos distintos.', 'https://cdn.example.com/icons/three_applications.png'),
('FIVE_APPLICATIONS_SENT', 'A la caza del equipo', 'Cinco solicitudes enviadas. ¡Sigue buscando tu equipo ideal!', 'https://cdn.example.com/icons/five_applications.png'),
('FIRST_REVIEW_WRITTEN', 'Primera review', 'Has valorado tu primer proyecto.', 'https://cdn.example.com/icons/review_written.png'),
('FIVE_REVIEWS_WRITTEN', 'Crítico constructivo', 'Has escrito cinco reviews en proyectos distintos.', 'https://cdn.example.com/icons/five_reviews.png'),
('RECEIVED_FIRST_REVIEW', 'Recibiste tu primera review', 'Un proyecto donde participaste fue valorado por otro usuario.', 'https://cdn.example.com/icons/review_received.png'),
('RECEIVED_TEN_REVIEWS', 'Feedbackador estrella', 'Tus proyectos han recibido al menos 10 reviews.', 'https://cdn.example.com/icons/ten_reviews.png'),
('FIRST_RESPONSE_WRITTEN', 'Primera respuesta', 'Has respondido a una review recibida.', 'https://cdn.example.com/icons/response_written.png'),
('THREE_RESPONSES_WRITTEN', 'Colaborador activo', 'Has respondido a tres reviews de tus proyectos.', 'https://cdn.example.com/icons/three_responses.png'),
('FIRST_TIME_LEADER', 'Líder de equipo', 'Has liderado un proyecto con éxito.', 'https://cdn.example.com/icons/team_leader.png'),
('FIVE_TIMES_LEADER', 'Líder nato', 'Has sido líder en cinco proyectos distintos.', 'https://cdn.example.com/icons/natural_leader.png'),
('FIVE_TAGS_SELECTED', 'Multidisciplinar', 'Has seleccionado al menos cinco habilidades en tu perfil.', 'https://cdn.example.com/icons/multidisciplinary.png'),
('PROFILE_WITH_LINKS', 'Perfil profesional', 'Has completado tu perfil con GitHub, LinkedIn o portfolio.', 'https://cdn.example.com/icons/professional_profile.png');

INSERT INTO projects (
    title, description, status, owner_id, repo_url, cover_image_url,
    estimated_duration_weeks, max_team_size, is_public, is_active, is_deleted,
    created_at, updated_at
)
VALUES
(
    'DevMatch',
    'Plataforma colaborativa para desarrolladores donde pueden compartir proyectos y recibir feedback.',
    'OPEN',
    1,
    'https://github.com/diegoarnanz-maker/devmatch',
    'https://cdn.example.com/covers/devmatch.jpg',
    6,
    5,
    TRUE,
    TRUE,
    FALSE,
    CURRENT_TIMESTAMP,
    NULL
),
(
    'Indimetra',
    'Aplicación de gestión de cortometrajes independientes, con exploración y reseñas.',
    'IN_PROGRESS',
    2,
    'https://github.com/diegoarnanz-maker/indimetra-api',
    'https://cdn.example.com/covers/indimetra.jpg',
    8,
    4,
    TRUE,
    TRUE,
    FALSE,
    CURRENT_TIMESTAMP,
    NULL
),
(
    'FishingManager',
    'Aplicación móvil para registrar capturas y rutas de pesca integrando GPS y condiciones climáticas.',
    'UNDER_REVIEW',
    1,
    'https://github.com/diegoarnanz-maker/fishingmanager',
    'https://cdn.example.com/covers/fishingmanager.jpg',
    4,
    3,
    FALSE, -- Este proyecto es privado
    TRUE,
    FALSE,
    CURRENT_TIMESTAMP,
    NULL
);

