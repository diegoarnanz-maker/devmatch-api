# 📚 Guía de Documentación DevMatch

## 🎯 **Estrategia de Documentación Híbrida Inteligente**

### **Principio de Responsabilidad Única**
Cada tipo de documentación tiene un propósito específico y una audiencia diferente:

- **JavaDoc** → Desarrolladores que mantienen el código
- **Markdown** → Arquitectos, product managers, documentación conceptual
- **Swagger** → Desarrolladores frontend, testing, integración

## 📋 **Cuándo Usar Cada Tipo**

### **✅ JavaDoc - Usar para:**

#### **Clases Principales:**
```java
/**
 * Entidad de dominio que representa un logro en la plataforma DevMatch.
 * 
 * <p>Un logro es una meta o hito que los usuarios pueden alcanzar.
 * Corresponde a la tabla achievement_catalog del DDL.</p>
 * 
 * <p>Características principales:</p>
 * <ul>
 *   <li>Código único identificador</li>
 *   <li>Sistema de puntos por desbloqueo</li>
 *   <li>Categorización por tipo de actividad</li>
 * </ul>
 * 
 * @see <a href="../../../../docs/domain/achievement.md">Documentación completa del dominio</a>
 * @author DevMatch Team
 * @version 1.0
 * @since 2024
 */
public class Achievement extends BaseDomainEntity {
    // ...
}
```

#### **Métodos Públicos:**
```java
/**
 * Verifica si un usuario puede desbloquear este logro.
 * 
 * @param userId ID del usuario que intenta desbloquear
 * @return true si puede desbloquearlo, false en caso contrario
 */
public boolean canBeUnlockedBy(Long userId) {
    return isActive && !isDeleted;
}
```

#### **Constructores:**
```java
/**
 * Constructor para crear un nuevo logro.
 * 
 * @param code Código único identificador del logro
 * @param title Título del logro
 * @param description Descripción detallada del logro
 * @param points Puntos que otorga el logro
 * @param type Tipo/categoría del logro
 * @param icon URL del icono del logro
 */
public Achievement(AchievementCode code, AchievementTitle title, AchievementDescription description,
                  AchievementPoints points, AchievementType type, AchievementIcon icon) {
    // ...
}
```

### **✅ Markdown - Usar para:**

#### **Documentación Conceptual:**
```markdown
# 🏆 Dominio de Achievement

## Reglas de Negocio
1. **Logro activo**: Solo se pueden desbloquear logros activos
2. **No duplicado**: Un usuario no puede tener el mismo logro dos veces
3. **Requisitos cumplidos**: Debe cumplir los requisitos específicos del logro

## Flujos de Proceso
[Diagramas de flujo...]

## Ejemplos de Uso
[Código completo...]
```

#### **Arquitectura:**
```markdown
## Arquitectura del Sistema
- **Domain Layer**: Entidades, value objects, servicios de dominio
- **Application Layer**: Casos de uso y servicios de aplicación
- **Infrastructure Layer**: Adaptadores y persistencia
```

### **✅ Swagger - Usar para:**

#### **APIs REST:**
```java
@RestController
@RequestMapping("/api/v1/achievements")
@Tag(name = "achievement-controller", description = "Gestión de logros")
public class AchievementController {
    
    @Operation(summary = "Obtener logro por ID", description = "Retorna un logro específico")
    @ApiResponse(responseCode = "200", description = "Logro encontrado")
    @ApiResponse(responseCode = "404", description = "Logro no encontrado")
    public ResponseEntity<AchievementResponseDto> getAchievement(@PathVariable Long id) {
        // ...
    }
}
```

## 🚫 **Cuándo NO Usar**

### **❌ Evitar Duplicación:**
```java
// ❌ MALO - Duplicar información entre JavaDoc y Markdown
/**
 * Un logro es una meta o hito que los usuarios pueden alcanzar.
 * Corresponde a la tabla achievement_catalog del DDL.
 * Un logro tiene código, título, descripción, puntos, tipo e icono.
 * Los logros pueden ser de diferentes tipos como PROFILE, PROJECT_CREATION, etc.
 */
```

```markdown
<!-- ❌ MALO - Duplicar la misma información -->
## Achievement
Un logro es una meta o hito que los usuarios pueden alcanzar.
Corresponde a la tabla achievement_catalog del DDL.
Un logro tiene código, título, descripción, puntos, tipo e icono.
Los logros pueden ser de diferentes tipos como PROFILE, PROJECT_CREATION, etc.
```

### **✅ Mejor Enfoque:**
```java
/**
 * Entidad de dominio que representa un logro en la plataforma DevMatch.
 * 
 * @see <a href="../../../../docs/domain/achievement.md">Documentación completa del dominio</a>
 */
public class Achievement extends BaseDomainEntity {
    // ...
}
```

```markdown
## Achievement
Entidad principal que representa un logro en el catálogo.

### Características:
- **Identificador único**: Código alfanumérico (ej: `FIRST_PROJECT`)
- **Información descriptiva**: Título, descripción, icono
- **Sistema de puntos**: Puntos que otorga al desbloquearse
```

## 📁 **Estructura de Documentación**

```
docs/
├── README.md                    # Índice principal
├── documentation-guide.md       # Esta guía
├── api/                        # Documentación de APIs
│   ├── achievements.md
│   ├── users.md
│   └── ...
├── architecture/               # Documentación de arquitectura
│   ├── security.md
│   ├── shared.md
│   └── ...
├── domain/                     # Documentación de dominio
│   ├── achievement.md
│   ├── user.md
│   └── ...
└── examples/                  # Herramientas de testing
    ├── postman-collection.json
    └── curl-examples.md
```

## 🔗 **Enlaces Cruzados**

### **JavaDoc → Markdown:**
```java
/**
 * @see <a href="../../../../docs/domain/achievement.md">Documentación completa del dominio</a>
 */
```

### **Markdown → JavaDoc:**
```markdown
Para más detalles técnicos, ver:
- `Achievement.canBeUnlockedBy()` - Método de validación
- `AchievementDomainService.getAchievementTier()` - Servicio de dominio
```

## 📝 **Templates para el Equipo**

### **Template para Clases de Dominio:**
```java
/**
 * [Descripción breve de la clase].
 * 
 * <p>[Descripción detallada si es necesaria].</p>
 * 
 * <p>Características principales:</p>
 * <ul>
 *   <li>[Característica 1]</li>
 *   <li>[Característica 2]</li>
 *   <li>[Característica 3]</li>
 * </ul>
 * 
 * @see <a href="../../../../docs/domain/[modulo].md">Documentación completa del dominio</a>
 * @author DevMatch Team
 * @version 1.0
 * @since 2024
 */
public class [NombreClase] extends BaseDomainEntity {
    // ...
}
```

### **Template para Métodos Públicos:**
```java
/**
 * [Descripción breve del método].
 * 
 * @param [parametro] [Descripción del parámetro]
 * @return [Descripción del valor de retorno]
 * @throws [Excepcion] [Cuándo se lanza la excepción]
 */
public [TipoRetorno] [nombreMetodo]([Parametros]) {
    // ...
}
```

### **Template para Markdown de Dominio:**
```markdown
# 🎯 Dominio de [Modulo]

## Descripción General
[Descripción del módulo y su propósito]

## 🏗️ Modelos de Dominio
[Documentación de entidades principales]

## 🎯 Value Objects
[Documentación de value objects]

## 🎉 Eventos de Dominio
[Documentación de eventos]

## ⚠️ Excepciones de Dominio
[Documentación de excepciones]

## 🔧 Servicios de Dominio
[Documentación de servicios]

## 📋 Reglas de Negocio
[Reglas específicas del dominio]

## 🔄 Flujos de Dominio
[Diagramas de flujo]

## 🧪 Ejemplos de Uso
[Código de ejemplo]

## 📊 Métricas y Análisis
[Métricas del sistema]

## 🔧 Configuración y Personalización
[Configuración específica]

## 📝 Mejores Prácticas
[Recomendaciones para el equipo]
```

## ✅ **Checklist de Documentación**

### **Para cada nueva clase:**
- [ ] JavaDoc en la clase con descripción y características
- [ ] JavaDoc en métodos públicos importantes
- [ ] Enlace a documentación Markdown si existe
- [ ] Información de autor, versión y fecha

### **Para cada nuevo módulo:**
- [ ] Documentación Markdown en `docs/domain/`
- [ ] Actualización del `README.md`
- [ ] Ejemplos de uso en Markdown
- [ ] Reglas de negocio documentadas

### **Para cada nueva API:**
- [ ] Anotaciones Swagger en el controlador
- [ ] Documentación Markdown en `docs/api/`
- [ ] Ejemplos de cURL y Postman
- [ ] Casos de error documentados

## 🚀 **Herramientas Recomendadas**

### **Para JavaDoc:**
- **IDE**: IntelliJ IDEA, Eclipse, VS Code
- **Generación**: Maven Javadoc Plugin
- **Visualización**: IDE integrado

### **Para Markdown:**
- **Editor**: Typora, Mark Text, VS Code
- **Preview**: GitHub, GitLab, Bitbucket
- **Diagramas**: Mermaid, PlantUML

### **Para Swagger:**
- **UI**: Swagger UI (http://localhost:8080/swagger-ui)
- **Testing**: Postman, Insomnia
- **Generación**: SpringDoc OpenAPI

## 📊 **Métricas de Documentación**

### **Cobertura de JavaDoc:**
- **Clases públicas**: 100%
- **Métodos públicos**: 80%+
- **Constructores**: 100%

### **Cobertura de Markdown:**
- **Módulos de dominio**: 100%
- **APIs principales**: 100%
- **Arquitectura**: 100%

### **Cobertura de Swagger:**
- **Endpoints públicos**: 100%
- **Casos de error**: 80%+
- **Ejemplos**: 100%

## 🎯 **Beneficios de la Estrategia Híbrida**

### **Para Desarrolladores:**
- **IDE Integration** - JavaDoc visible en el editor
- **Navegación rápida** - Enlaces a documentación detallada
- **Mantenimiento** - Cambios en código se reflejan en JavaDoc
- **Completitud** - Información técnica + conceptual

### **Para el Equipo:**
- **Consistencia** - Misma estrategia en toda la aplicación
- **Escalabilidad** - Fácil de mantener y actualizar
- **Colaboración** - Diferentes tipos de documentación para diferentes necesidades
- **Onboarding** - Nuevos desarrolladores pueden entender rápidamente

### **Para el Negocio:**
- **Transparencia** - Reglas de negocio claramente documentadas
- **Mantenibilidad** - Fácil de actualizar y evolucionar
- **Calidad** - Documentación consistente y completa
- **Eficiencia** - Menos tiempo perdido buscando información

## 🔄 **Mantenimiento de la Documentación**

### **Frecuencia de Actualización:**
- **JavaDoc**: Con cada cambio en el código
- **Markdown**: Con cambios en reglas de negocio o arquitectura
- **Swagger**: Con cambios en APIs

### **Responsabilidades:**
- **Desarrolladores**: JavaDoc y Swagger
- **Arquitectos**: Markdown de arquitectura
- **Product Managers**: Markdown de reglas de negocio
- **Tech Leads**: Revisión y consistencia

## 📚 **Recursos Adicionales**

### **Documentación de JavaDoc:**
- [Oracle JavaDoc Guide](https://www.oracle.com/technical-resources/articles/java/javadoc-tool.html)
- [Maven Javadoc Plugin](https://maven.apache.org/plugins/maven-javadoc-plugin/)

### **Documentación de Markdown:**
- [Markdown Guide](https://www.markdownguide.org/)
- [Mermaid Diagrams](https://mermaid-js.github.io/mermaid/)

### **Documentación de Swagger:**
- [OpenAPI Specification](https://swagger.io/specification/)
- [SpringDoc OpenAPI](https://springdoc.org/)

---

**¡Mantén la documentación actualizada y tu equipo te lo agradecerá!** 🚀
