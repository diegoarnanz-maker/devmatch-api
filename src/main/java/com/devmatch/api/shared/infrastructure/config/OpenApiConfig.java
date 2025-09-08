package com.devmatch.api.shared.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración centralizada de OpenAPI 3.0 para DevMatch API.
 * Define la información de la API, servidores, seguridad y configuración general.
 */
@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name:devmatch-api}")
    private String applicationName;

    @Value("${server.port:8080}")
    private String serverPort;

    /**
     * Configuración principal de OpenAPI.
     * Define la información de la API, servidores y esquemas de seguridad.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(servers())
                .addSecurityItem(securityRequirement())
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth", securityScheme()));
    }

    /**
     * Información de la API.
     */
    private Info apiInfo() {
        return new Info()
                .title("DevMatch API")
                .description("""
                        **Plataforma colaborativa para desarrolladores**
                        
                        DevMatch es una API REST que facilita la conexión entre desarrolladores 
                        para proyectos colaborativos. Incluye funcionalidades de:
                        
                        - 👥 **Gestión de usuarios** con perfiles y roles
                        - 🚀 **Gestión de proyectos** colaborativos
                        - 💬 **Sistema de mensajería** en tiempo real
                        - ⭐ **Sistema de reviews** y calificaciones
                        - 🏆 **Sistema de logros** y gamificación
                        - 🔔 **Notificaciones** personalizadas
                        - 🏷️ **Sistema de tags** para categorización
                        
                        **Arquitectura:** Hexagonal (Ports & Adapters) + DDD
                        **Tecnología:** Spring Boot 3.5.0 + Java 21
                        """)
                .version("1.0.0")
                .contact(contactInfo())
                .license(licenseInfo());
    }

    /**
     * Información de contacto.
     */
    private Contact contactInfo() {
        return new Contact()
                .name("DevMatch Team")
                .email("devmatch@example.com")
                .url("https://devmatch.com");
    }

    /**
     * Información de licencia.
     */
    private License licenseInfo() {
        return new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");
    }

    /**
     * Configuración de servidores.
     */
    private List<Server> servers() {
        return List.of(
                new Server()
                        .url("http://localhost:" + serverPort)
                        .description("Servidor de Desarrollo"),
                new Server()
                        .url("https://api-dev.devmatch.com")
                        .description("Servidor de Staging"),
                new Server()
                        .url("https://api.devmatch.com")
                        .description("Servidor de Producción")
        );
    }

    /**
     * Esquema de seguridad JWT.
     */
    private SecurityScheme securityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name("Authorization")
                .description("""
                        **Autenticación JWT**
                        
                        Para usar la API, necesitas obtener un token JWT mediante el endpoint de login:
                        
                        1. **POST** `/api/v1/users/auth/login` con credenciales
                        2. Copia el token del response
                        3. Haz clic en **Authorize** y pega el token
                        4. El token expira en 24 horas por defecto
                        
                        **Formato:** `Bearer <tu_token_aqui>`
                        """);
    }

    /**
     * Requerimiento de seguridad global.
     */
    private SecurityRequirement securityRequirement() {
        return new SecurityRequirement().addList("bearerAuth");
    }
}
