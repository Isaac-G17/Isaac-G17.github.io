# 04 - Arquitectura

El backend usa **arquitectura hexagonal** (puertos y adaptadores). El frontend usa una estructura por *features*.

---

## 1. Por qué hexagonal

- **El dominio no depende de frameworks.** Las reglas de negocio (publicar, programar, validar un slug) son Java puro y se prueban sin Spring ni base de datos.
- **Los proveedores se pueden cambiar.** Cloudinary → S3, EmailJS → SES, eventos en memoria → RabbitMQ: solo cambia el adaptador, no el negocio.
- **Encaja con el roadmap:** Redis, RabbitMQ y AWS llegan después del MVP como adaptadores nuevos.

---

## 2. Capas y regla de dependencias

```
        ┌──────────────── infrastructure ────────────────┐
        │  adaptadores de entrada     adaptadores salida │
        │  (REST controllers)         (JPA, Cloudinary,  │
        │          │                   RabbitMQ, Redis)  │
        │          ▼                         ▲           │
        │   ┌──────────── application ───────┴──────┐    │
        │   │  port/in (casos de uso)   port/out    │    │
        │   │  services                             │    │
        │   │        ┌────── domain ──────┐         │    │
        │   │        │ modelos, reglas,   │         │    │
        │   │        │ value objects      │         │    │
        │   │        └────────────────────┘         │    │
        │   └───────────────────────────────────────┘    │
        └────────────────────────────────────────────────┘
```

**Las dependencias siempre apuntan hacia adentro:**

| Capa | Puede depender de | Nunca depende de |
|---|---|---|
| `domain` | Nada (Java puro) | Spring, JPA, Jackson, otras capas |
| `application` | `domain` | `infrastructure`, controladores, entidades JPA |
| `infrastructure` | `application` y `domain` | — |

---

## 3. Estructura de paquetes

Un módulo por contexto de negocio, cada uno con sus tres capas:

```
backend/src/main/java/dev/isaacguzman/portfolio/
├── project/
│   ├── domain/
│   │   ├── Project.java              # modelo de dominio (sin anotaciones JPA)
│   │   ├── ProjectType.java          # WORDPRESS | CODE
│   │   ├── Slug.java                 # value object
│   │   └── ProjectNotFoundException.java
│   ├── application/
│   │   ├── port/in/
│   │   │   ├── CreateProjectUseCase.java
│   │   │   ├── PublishProjectUseCase.java
│   │   │   └── GetPublishedProjectsQuery.java
│   │   ├── port/out/
│   │   │   └── ProjectRepositoryPort.java
│   │   └── service/
│   │       └── ProjectService.java   # implementa los casos de uso
│   └── infrastructure/
│       ├── in/web/
│       │   ├── PublicProjectController.java   # /api/public/projects
│       │   ├── AdminProjectController.java    # /api/admin/projects
│       │   ├── dto/
│       │   └── ProjectWebMapper.java
│       └── out/persistence/
│           ├── ProjectJpaEntity.java
│           ├── SpringDataProjectRepository.java
│           ├── ProjectPersistenceAdapter.java  # implementa ProjectRepositoryPort
│           └── ProjectPersistenceMapper.java
├── post/          # misma estructura (blog)
├── media/         # subida de imágenes
├── contact/       # formulario de contacto
├── auth/          # login del administrador
└── shared/        # configuración, seguridad, errores comunes
```

---

## 4. Puertos de salida y sus adaptadores por versión

| Puerto (`port/out`) | MVP (v1.0) | Después |
|---|---|---|
| `ProjectRepositoryPort`, `PostRepositoryPort` | JPA + PostgreSQL | Caché con Redis (v1.2) |
| `StoragePort` | `CloudinaryStorageAdapter` | `S3StorageAdapter` (v2, AWS) |
| `EmailSenderPort` | Proveedor de correo simple | Amazon SES (v2) |
| `EventPublisherPort` | Eventos de Spring en memoria | `RabbitMqEventPublisherAdapter` (v1.3) |
| `RateLimiterPort` | En memoria | Redis (v1.2) |

Ejemplo del puerto de almacenamiento:

```java
// application/port/out
public interface StoragePort {
    UploadUrl createUploadUrl(String key, String contentType);
    String publicUrl(String key);
    void delete(String key);
}

// infrastructure/out/storage
@Component
@ConditionalOnProperty(name = "app.storage.provider", havingValue = "cloudinary")
class CloudinaryStorageAdapter implements StoragePort { ... }
```

Cambiar de proveedor es solo cambiar `app.storage.provider` en la configuración.

---

## 5. Reglas prácticas

- **Entidades JPA separadas del dominio.** `ProjectJpaEntity` vive en infraestructura; `Project` en dominio. Se convierten con mappers (MapStruct).
- **Los DTO de la API no salen del adaptador web.** Los controladores llaman a los casos de uso, nunca a repositorios.
- **Las transacciones van en la capa de aplicación** (`@Transactional` en los servicios).
- **Validación en dos niveles:**
  - Formato en los DTO, con Bean Validation.
  - Reglas de negocio en el dominio.
- **No sobrediseñar:**
  - Un puerto por necesidad real, no uno por cada clase.
  - Los módulos simples (por ejemplo `auth`) pueden tener menos piezas.

---

## 6. Tests por capa

| Capa | Tipo de test | Herramientas |
|---|---|---|
| `domain` | Unitarios, sin Spring | JUnit 5, AssertJ |
| `application` | Unitarios con puertos falsos o mocks | JUnit 5, Mockito |
| `infrastructure` | Integración contra servicios reales | Spring Boot Test, **Testcontainers** (PostgreSQL, Redis, RabbitMQ) |
| Arquitectura | Verifica la regla de dependencias | **ArchUnit** |

Ejemplo de regla con ArchUnit, que hace fallar el build si alguien rompe la arquitectura:

```java
@ArchTest
static final ArchRule domainIsPure = noClasses()
    .that().resideInAPackage("..domain..")
    .should().dependOnClassesThat()
    .resideInAnyPackage("org.springframework..", "jakarta.persistence..", "..infrastructure..");
```

---

## 7. Frontend (Angular)

La arquitectura hexagonal no hace falta en el frontend. Se usa una estructura por *features*:

```
frontend/src/app/
├── core/          # servicios globales, interceptores HTTP, guards, layout
├── shared/        # componentes reutilizables (tarjetas, botones, markdown)
├── features/
│   ├── home/
│   ├── projects/  # lista, filtro, detalle
│   ├── blog/      # lista, artículo, categorías
│   └── contact/
└── admin/         # carga diferida (lazy loading) y protegido por guard
    ├── projects/
    ├── posts/
    └── media/
```

Cada *feature* llama a la API mediante su propio servicio (`ProjectsApi`, `PostsApi`). Los componentes nunca usan `HttpClient` directamente.
