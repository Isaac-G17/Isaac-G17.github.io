# 02 - Plan del MVP

Nueva versión del portafolio como plataforma full-stack con **Spring Boot** y **Angular**, con panel de administración para publicar proyectos y artículos sin tocar código.

---

## 1. Ideas clave

### Dos públicos

| Público | Qué quiere ver | Qué se le muestra |
|---|---|---|
| **Clientes** | Resultados | Sitios en WordPress, casos de éxito, contacto rápido |
| **Reclutadores y empresas** | Que sabes programar | Código, arquitectura, buenas prácticas |

La propia plataforma se convierte en el proyecto de código más fuerte del portafolio: un sistema full-stack real, en producción y con panel de administración.

### Riesgo principal: no terminarlo

Un portafolio con CMS propio crece sin fin. La estrategia es:

- Definir un MVP pequeño, lanzarlo e iterar.
- Mantener el sitio actual de GitHub Pages en línea hasta que la v1 esté lista.

---

## 2. Alcance del MVP

### Sitio público

- **Inicio:** migrar el diseño actual a componentes de Angular.
- **Proyectos:** lista con filtro **WordPress / Código**.
- **Detalle de proyecto:** galería, tecnologías, enlace al sitio en vivo y al repositorio, y sección "problema → solución → resultado".
- **Blog tech:** ver [03 - Blog tech](03-blog.md).
- **Contacto:** reutilizar EmailJS o enviar desde el backend.

### Panel de administración (`/admin`)

- Inicio de sesión con un solo usuario administrador.
- CRUD de proyectos y artículos, con estado **borrador o publicado**.
- Editor de Markdown con vista previa.
- Subida de imágenes.
- Marcar proyectos como **destacados** para el inicio.

### Fuera del MVP (v2)

- Comentarios.
- Varios usuarios.
- Analítica propia.
- Varios idiomas.
- Sincronización automática con la API de GitHub.
- "Me gusta".

---

## 3. Modelo de datos

```
Project
  id, title, slug, type (WORDPRESS | CODE), summary,
  content (markdown), coverImageUrl, gallery[],
  technologies[], liveUrl, repoUrl, featured,
  status (DRAFT | PUBLISHED), publishedAt, createdAt, updatedAt

Post                (detalle en 03-blog.md)
Category
Tag
Technology          (muchos a muchos con Project)
AdminUser           (email, passwordHash)
```

Usar el `slug` en las URLs (`/proyectos/galeria-paredes-expression`) en vez del `id`, porque es mejor para el SEO.

---

## 4. Stack

### Backend: Spring Boot

- **Arquitectura hexagonal** (puertos y adaptadores). Detalle en [04 - Arquitectura](04-arquitectura.md).
- **Java 21 y Spring Boot 3**, con Spring Web, Spring Data JPA y Validation.
- **PostgreSQL** como base de datos y **Flyway** para las migraciones.
- **Spring Security** con un solo administrador:
  - Contraseñas cifradas con BCrypt.
  - JWT en una cookie `httpOnly` (mejor que `localStorage`).
- **Imágenes en Cloudinary** detrás de un puerto `StoragePort`, nunca en el disco del servidor (en los hostings gratuitos se borra con cada despliegue). Ver la [decisión sobre almacenamiento](#5-decisión-almacenamiento-de-imágenes).
- **springdoc-openapi** para documentar la API con Swagger.
- **Endpoints separados:**
  - `GET /api/public/**`: abierto, solo devuelve contenido publicado.
  - `/api/admin/**`: protegido, CRUD completo.

### Frontend: Angular

- **Angular reciente** con standalone components y signals.
- **Una sola app:** el panel `/admin` en una ruta con *lazy loading* protegida por un `guard`.
- **⚠️ SSR o prerender obligatorio (`@angular/ssr`).** Una SPA normal carga vacía y Google, WhatsApp y LinkedIn no ven el contenido ni la vista previa.
- **ngx-markdown** con Prism para el resaltado de código.
- **Angular Material o PrimeNG** para formularios y tablas del panel.

### Infraestructura

- **Monorepo:**
  ```
  /backend
  /frontend
  /docs
  docker-compose.yml   (Postgres en local)
  ```
- **GitHub Actions:** tests y build en cada PR; despliegue automático al fusionar con `main`.
- **Hosting:**
  - Frontend: Vercel o Netlify.
  - Backend: Railway, Render o Fly.io.
  - Base de datos: Neon o Supabase (Postgres administrado).
  - ⚠️ En los planes gratis el backend "se duerme" y la primera visita puede tardar de 30 a 50 segundos. Para un sitio que se muestra a clientes conviene un plan pequeño (unos 5 USD al mes) o un VPS con Docker.
- **Dominio propio** (por ejemplo `isaacguzman.dev`): da más confianza que `github.io`.

---

## 5. Decisión: almacenamiento de imágenes

**Elegido para el MVP: Cloudinary.**

| Opción | Costo | Notas |
|---|---|---|
| **Cloudinary** ✅ | Plan gratuito permanente, 25 créditos al mes, sin tarjeta | 1 crédito = 1 GB de almacenamiento, 1 GB de ancho de banda o 1.000 transformaciones |
| AWS S3 + CloudFront | Plan gratuito de 6 meses o hasta 200 USD en créditos; luego centavos al mes con tarjeta | CloudFront tiene 1 TB al mes gratis permanente; S3 no |
| Cloudflare R2 | 10 GB gratis permanentes | Compatible con la API de S3 |

**Motivo:** el requisito es que sea gratis. AWS solo es gratis los primeros 6 meses (cuentas creadas desde el 15 de julio de 2025).

**Plan a futuro:** el almacenamiento queda detrás de `StoragePort`. Para aprender AWS en la v2 basta con escribir `S3StorageAdapter`, sin tocar el negocio.

**Si se usa AWS, configurar el primer día:**

- MFA en la cuenta root.
- Usuario IAM propio y un rol con permisos mínimos para la app.
- Alerta de presupuesto en AWS Budgets.
- Claves solo en variables de entorno.

---

## 6. Roadmap después del MVP

Cada tecnología entra cuando resuelve un problema real del proyecto, y se documenta en el README por qué se usó.

| Versión | Tecnología | Uso | Hosting gratis |
|---|---|---|---|
| **v1.1** | Spring Boot Actuator, Micrometer, **Prometheus y Grafana** | Métricas de la API: latencia, errores, JVM | Grafana Cloud (incluye Prometheus administrado) |
| **v1.2** | **Redis** | Caché de endpoints públicos y límite de intentos en login y contacto | Upstash o Redis Cloud |
| **v1.3** | **RabbitMQ** | Tareas asíncronas: correo de contacto con reintentos, conversión de imágenes a WebP, regenerar sitemap | CloudAMQP |
| **v2** | **AWS** (S3, CloudFront, SES, EC2) | Migrar almacenamiento y correo; opcionalmente el backend | Requiere tarjeta tras 6 meses |
| **v3** | Terraform o AWS CDK | Infraestructura como código | Gratis |

- En local, todos los servicios corren en `docker-compose`, y en los tests con Testcontainers.
- **Historia para contar:** medir con Grafana en la v1.1 y comparar antes y después de agregar la caché de Redis en la v1.2. Sirve como artículo de blog y como respuesta de entrevista.

---

## 7. Sprints (2 semanas cada uno)

| Sprint | Objetivo | Entregable |
|---|---|---|
| **0. Base** | Monorepo, Docker con Postgres, CI, "hola mundo" desplegado | URL en producción desde el día 1 |
| **1. API** | Entidades, migraciones, endpoints públicos, CRUD de admin, autenticación, subida de imágenes | API documentada en Swagger, con tests |
| **2. Sitio público** | Migrar el diseño, lista y detalle de proyectos, blog | Sitio navegable con datos de prueba |
| **3. Panel** | Login, formularios, editor Markdown, subida de imágenes, borradores | Publicar sin tocar código |
| **4. Lanzamiento** | SSR, etiquetas meta y Open Graph, sitemap, imágenes WebP, Lighthouse, **contenido real** | **v1.0.0** en el dominio propio |

**Desplegar desde el sprint 0.** Si el despliegue se deja para el final, ahí aparecen todos los problemas (CORS, variables de entorno, SSR).

---

## 8. Criterios de una versión estable

- **Tests:**
  - JUnit con Testcontainers para servicios y API. Cubrir lo crítico: autenticación y publicación.
  - Algunos tests de componentes en Angular.
- **README del proyecto** con:
  - Diagrama de arquitectura.
  - Capturas del panel.
  - Cómo correrlo en local.
  - Enlace a Swagger.
- **Flujo de Git:**
  - Ramas `develop` y `main`, con `main` protegida y PR fusionados con squash.
  - Etiquetas de versión (`v1.0.0`) y `CHANGELOG`.
- **Seguridad básica:**
  - Secretos en variables de entorno, nunca en el repositorio.
  - CORS restringido al dominio propio.
  - Límite de intentos en el login y en el formulario de contacto.
- **Enlace "Ver código de este sitio"** en el footer.

---

## 9. Contenido necesario antes del lanzamiento

La tecnología no consigue clientes; los proyectos sí.

- **2 o 3 casos en WordPress** con capturas, objetivo del cliente y resultado concreto. Si faltan clientes, sitios de ejemplo de nichos reales (restaurante, consultorio, tienda).
- **2 o 3 proyectos de código** con un README decente. Esta plataforma puede ser uno.
- **1 o 2 artículos** para el blog (ver ideas en [03 - Blog tech](03-blog.md)).

---

## 10. Epics en Jira

Espacio **Portafolio** en Jira, con metodología Scrum.

| Clave | Epic | Versión |
|---|---|---|
| [SCRUM-1](https://guzmanisaac2005.atlassian.net/browse/SCRUM-1) | Mejoras del sitio actual | Sprint 0 |
| [SCRUM-2](https://guzmanisaac2005.atlassian.net/browse/SCRUM-2) | Infraestructura y CI/CD | Sprint 0 |
| [SCRUM-3](https://guzmanisaac2005.atlassian.net/browse/SCRUM-3) | API de contenido con arquitectura hexagonal | Sprint 1 |
| [SCRUM-4](https://guzmanisaac2005.atlassian.net/browse/SCRUM-4) | Autenticación y seguridad | Sprint 1 |
| [SCRUM-5](https://guzmanisaac2005.atlassian.net/browse/SCRUM-5) | Gestión de imágenes (Cloudinary, con puerto para S3) | Sprint 1 |
| [SCRUM-6](https://guzmanisaac2005.atlassian.net/browse/SCRUM-6) | Sitio público: portafolio | Sprint 2 |
| [SCRUM-7](https://guzmanisaac2005.atlassian.net/browse/SCRUM-7) | Blog tech | Sprint 2 |
| [SCRUM-8](https://guzmanisaac2005.atlassian.net/browse/SCRUM-8) | Panel de administración | Sprint 3 |
| [SCRUM-9](https://guzmanisaac2005.atlassian.net/browse/SCRUM-9) | SEO, rendimiento y lanzamiento v1.0 | Sprint 4 |
| [SCRUM-10](https://guzmanisaac2005.atlassian.net/browse/SCRUM-10) | Contenido para el lanzamiento | Todo el MVP |
| [SCRUM-11](https://guzmanisaac2005.atlassian.net/browse/SCRUM-11) | Observabilidad con Prometheus y Grafana | v1.1 |
| [SCRUM-12](https://guzmanisaac2005.atlassian.net/browse/SCRUM-12) | Caché y límite de intentos con Redis | v1.2 |
| [SCRUM-13](https://guzmanisaac2005.atlassian.net/browse/SCRUM-13) | Procesamiento asíncrono con RabbitMQ | v1.3 |

---

## 11. Preguntas abiertas

- [x] Almacenamiento de imágenes: Cloudinary (ver sección 5).
- [x] Arquitectura del backend: hexagonal (ver [04 - Arquitectura](04-arquitectura.md)).
- [ ] Presupuesto de hosting: ¿100 % gratis o 5 a 10 USD al mes?
- [ ] Horas a la semana dedicadas al proyecto.
- [ ] Nivel actual en Spring Boot y Angular, para decidir por dónde empezar.
- [ ] Dominio propio: ¿cuál?
