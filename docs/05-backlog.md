# 05 - Backlog

Historias de usuario (HU) y tareas de cada epic. En Jira, cada HU es una **Historia** hija de su epic, y cada tarea es una **Subtask** de su HU.

> Las subtareas de cada HU están en Jira con el mismo nombre. Las epics están en [02 - Plan del MVP](02-plan-mvp.md#10-epics-en-jira).

## Sprints

| Sprint | Fechas | Objetivo | Historias |
|---|---|---|---|
| **Sprint 0** | 19 al 30 de octubre de 2026 | Dejar el sitio actual corregido y la base del nuevo proyecto lista: monorepo, entorno local con Docker, CI/CD y primer despliegue público | HU 1.1 a 1.5 y HU 2.1 a 2.4 (9 historias, 34 subtareas) |

---

## [SCRUM-1](https://guzmanisaac2005.atlassian.net/browse/SCRUM-1) · Mejoras del sitio actual

### HU 1.1 – Formulario de contacto confiable · [SCRUM-14](https://guzmanisaac2005.atlassian.net/browse/SCRUM-14)
**Como** visitante, **quiero** enviar el formulario de contacto sin errores **para** que mi mensaje llegue a Isaac.
- **Criterios:**
  - Se aceptan celulares con espacios, guiones y `+`.
  - No se envía dos veces.
  - Los errores se muestran con notificaciones.
- **Tareas:**
  - [ ] Agregar `novalidate` al formulario
  - [ ] Normalizar y validar el celular con espacios, guiones y `+`
  - [ ] Desactivar el botón y mostrar "Enviando…" durante el envío
  - [ ] Restringir dominios y límite de envíos en EmailJS
  - [ ] Fijar la versión de `@emailjs/browser` y quitar el `console.log`

### HU 1.2 – Enlaces y datos de contacto funcionando · [SCRUM-16](https://guzmanisaac2005.atlassian.net/browse/SCRUM-16)
**Como** visitante, **quiero** que todos los botones y datos de contacto funcionen **para** poder escribirle a Isaac por el medio que prefiera.
- **Criterios:**
  - No hay enlaces con `href=""`.
  - El correo y el teléfono se pueden pulsar.
  - No hay errores en la consola.
- **Tareas:**
  - [ ] Corregir los `href` vacíos de "Solicitar mi página web" y "WhatsApp"
  - [ ] Enlazar el correo con `mailto:` y el teléfono con `wa.me`
  - [ ] Agregar LinkedIn y WhatsApp al footer con `rel="noopener"`
  - [ ] Corregir el error de `script.js` en la página de proyectos

### HU 1.3 – Textos sin faltas · [SCRUM-17](https://guzmanisaac2005.atlassian.net/browse/SCRUM-17)
**Como** visitante, **quiero** leer textos sin faltas **para** confiar en el profesional.
- **Criterios:**
  - Todas las faltas de la revisión están corregidas.
- **Tareas:**
  - [ ] Corregir las faltas de ortografía de `index.html`
  - [ ] Revisar los textos de `proyectos.html`

### HU 1.4 – Sitio rápido · [SCRUM-18](https://guzmanisaac2005.atlassian.net/browse/SCRUM-18)
**Como** visitante, **quiero** que el sitio cargue rápido **para** no abandonarlo.
- **Criterios:**
  - Ninguna imagen supera los 300 KB.
  - Lighthouse en Rendimiento por encima de 90.
- **Tareas:**
  - [ ] Convertir las imágenes pesadas a WebP
  - [ ] Borrar las imágenes duplicadas y sin uso
  - [ ] Agregar `loading="lazy"`, `width` y `height` a las imágenes
  - [ ] Unificar Google Fonts en un enlace con solo los pesos usados

### HU 1.5 – SEO básico · [SCRUM-19](https://guzmanisaac2005.atlassian.net/browse/SCRUM-19)
**Como** Isaac, **quiero** que el sitio aparezca bien en Google y al compartirlo **para** atraer clientes.
- **Criterios:**
  - El enlace compartido en WhatsApp muestra la vista previa.
  - Lighthouse en SEO por encima de 90.
- **Tareas:**
  - [ ] Cambiar `lang` a `es` en las dos páginas
  - [ ] Agregar meta description y títulos únicos por página
  - [ ] Agregar etiquetas Open Graph y favicon
  - [ ] Medir con Lighthouse y registrar resultados

---

## [SCRUM-2](https://guzmanisaac2005.atlassian.net/browse/SCRUM-2) · Infraestructura y CI/CD

### HU 2.1 – Monorepo y entorno local · [SCRUM-20](https://guzmanisaac2005.atlassian.net/browse/SCRUM-20)
**Como** desarrollador, **quiero** un monorepo con entorno local en Docker **para** empezar a desarrollar rápido.
- **Criterios:**
  - `docker compose up` levanta PostgreSQL.
  - El backend y el frontend arrancan en local.
- **Tareas:**
  - [ ] Crear el repositorio con `/backend`, `/frontend` y `/docs`
  - [ ] Generar el proyecto Spring Boot 3 con Java 21
  - [ ] Generar el proyecto Angular
  - [ ] Crear `docker-compose.yml` con PostgreSQL
  - [ ] Escribir las instrucciones de ejecución local en el README

### HU 2.2 – Integración continua · [SCRUM-21](https://guzmanisaac2005.atlassian.net/browse/SCRUM-21)
**Como** desarrollador, **quiero** que cada PR ejecute build y tests **para** detectar errores antes de fusionar.
- **Criterios:**
  - Un PR con tests fallando no se puede fusionar.
- **Tareas:**
  - [ ] Workflow de GitHub Actions para el backend (build y tests)
  - [ ] Workflow de GitHub Actions para el frontend (lint, tests y build)
  - [ ] Proteger `main` y configurar squash merge

### HU 2.3 – Despliegue continuo · [SCRUM-22](https://guzmanisaac2005.atlassian.net/browse/SCRUM-22)
**Como** desarrollador, **quiero** desplegar automáticamente al fusionar con `main` **para** tener una URL pública desde el sprint 0.
- **Criterios:**
  - Hay una URL pública del frontend y otra del backend con health check.
  - No hay secretos en el repositorio.
- **Tareas:**
  - [ ] Crear la base de datos en Neon o Supabase
  - [ ] Desplegar el backend con health check
  - [ ] Desplegar el frontend en Vercel o Netlify
  - [ ] Configurar variables de entorno y secretos
  - [ ] Activar el despliegue automático al fusionar con `main`

### HU 2.4 – Dominio propio · [SCRUM-23](https://guzmanisaac2005.atlassian.net/browse/SCRUM-23)
**Como** Isaac, **quiero** un dominio propio **para** transmitir más confianza que `github.io`.
- **Criterios:**
  - El frontend y la API responden con HTTPS en el dominio propio.
- **Tareas:**
  - [ ] Elegir y comprar el dominio
  - [ ] Configurar DNS y HTTPS para el frontend y la API

---

## [SCRUM-3](https://guzmanisaac2005.atlassian.net/browse/SCRUM-3) · API de contenido con arquitectura hexagonal

### HU 3.1 – Base hexagonal · [SCRUM-24](https://guzmanisaac2005.atlassian.net/browse/SCRUM-24)
**Como** desarrollador, **quiero** la estructura hexagonal base **para** que el dominio no dependa de frameworks.
- **Criterios:**
  - ArchUnit falla si `domain` depende de Spring, JPA o `infrastructure`.
- **Tareas:**
  - [ ] Crear los paquetes por módulo con capas `domain`, `application` e `infrastructure`
  - [ ] Configurar MapStruct
  - [ ] Escribir las reglas de dependencias con ArchUnit
  - [ ] Manejo global de errores con `ProblemDetail`

### HU 3.2 – Consultar proyectos publicados · [SCRUM-25](https://guzmanisaac2005.atlassian.net/browse/SCRUM-25)
**Como** visitante, **quiero** consultar los proyectos publicados **para** conocer el trabajo de Isaac.
- **Criterios:**
  - `GET /api/public/projects` filtra por tipo.
  - `GET /api/public/projects/{slug}` devuelve 404 si no existe o no está publicado.
- **Tareas:**
  - [ ] Modelo de dominio `Project`, `ProjectType` y `Slug`
  - [ ] Migración Flyway de proyectos y tecnologías
  - [ ] Adaptador de persistencia JPA de proyectos
  - [ ] Casos de uso y endpoints públicos de proyectos
  - [ ] Tests de dominio, aplicación e integración con Testcontainers

### HU 3.3 – Gestionar proyectos por API · [SCRUM-26](https://guzmanisaac2005.atlassian.net/browse/SCRUM-26)
**Como** administrador, **quiero** crear, editar, publicar y borrar proyectos mediante la API **para** mantener el portafolio al día.
- **Criterios:**
  - Los datos inválidos devuelven 400 con detalle.
  - Un slug duplicado devuelve 409.
- **Tareas:**
  - [ ] Casos de uso de crear, editar, borrar y publicar proyectos
  - [ ] Endpoints `/api/admin/projects` con Bean Validation
  - [ ] Tests de los endpoints de admin de proyectos

### HU 3.4 – Consultar artículos publicados · [SCRUM-27](https://guzmanisaac2005.atlassian.net/browse/SCRUM-27)
**Como** lector, **quiero** consultar los artículos publicados por categoría y etiqueta **para** encontrar temas que me interesan.
- **Criterios:**
  - Solo se devuelven artículos con `publishedAt <= now()`.
  - Hay paginación y búsqueda por título.
- **Tareas:**
  - [ ] Modelo de dominio `Post`, `Category` y `Tag`
  - [ ] Migraciones Flyway del blog
  - [ ] Endpoints públicos de artículos con filtros, búsqueda y paginación
  - [ ] Calcular el tiempo de lectura al guardar
  - [ ] Tests del módulo de artículos

### HU 3.5 – Gestionar artículos por API · [SCRUM-28](https://guzmanisaac2005.atlassian.net/browse/SCRUM-28)
**Como** administrador, **quiero** gestionar artículos, categorías y etiquetas mediante la API **para** publicar en el blog.
- **Criterios:**
  - Se pueden crear borradores y programar la publicación.
- **Tareas:**
  - [ ] Endpoints de admin de artículos
  - [ ] Endpoints de admin de categorías y etiquetas
  - [ ] Tests de los endpoints de admin del blog

### HU 3.6 – Documentación de la API · [SCRUM-29](https://guzmanisaac2005.atlassian.net/browse/SCRUM-29)
**Como** desarrollador, **quiero** la API documentada en Swagger **para** probarla y mostrarla a reclutadores.
- **Criterios:**
  - Swagger UI muestra todos los endpoints con sus esquemas.
- **Tareas:**
  - [ ] Configurar springdoc-openapi
  - [ ] Documentar endpoints, esquemas y errores

---

## [SCRUM-4](https://guzmanisaac2005.atlassian.net/browse/SCRUM-4) · Autenticación y seguridad

### HU 4.1 – Login seguro · [SCRUM-30](https://guzmanisaac2005.atlassian.net/browse/SCRUM-30)
**Como** administrador, **quiero** iniciar sesión de forma segura **para** acceder al panel.
- **Criterios:**
  - La contraseña se guarda con BCrypt.
  - El JWT viaja en una cookie `httpOnly`, `Secure` y `SameSite`.
- **Tareas:**
  - [ ] Modelo `AdminUser` y usuario inicial desde variables de entorno
  - [ ] Endpoints de login y logout
  - [ ] Generar y validar el JWT en una cookie `httpOnly`
  - [ ] Tests de login

### HU 4.2 – Proteger la API de admin · [SCRUM-31](https://guzmanisaac2005.atlassian.net/browse/SCRUM-31)
**Como** Isaac, **quiero** que solo el administrador acceda a `/api/admin/**` **para** que nadie modifique el contenido.
- **Criterios:**
  - Sin sesión válida se recibe 401.
  - CORS solo permite el dominio del frontend.
- **Tareas:**
  - [ ] Configurar `SecurityFilterChain` para las rutas públicas y de admin
  - [ ] Restringir CORS al dominio del frontend
  - [ ] Tests de acceso 401 y 403

### HU 4.3 – Límite de intentos · [SCRUM-32](https://guzmanisaac2005.atlassian.net/browse/SCRUM-32)
**Como** Isaac, **quiero** limitar los intentos de login y los envíos de contacto **para** evitar ataques y spam.
- **Criterios:**
  - Al superar el límite se responde 429.
- **Tareas:**
  - [ ] Definir `RateLimiterPort`
  - [ ] Adaptador en memoria con Bucket4j
  - [ ] Aplicar el límite al login y al contacto

---

## [SCRUM-5](https://guzmanisaac2005.atlassian.net/browse/SCRUM-5) · Gestión de imágenes (Cloudinary, con puerto para S3)

### HU 5.1 – Puerto de almacenamiento · [SCRUM-33](https://guzmanisaac2005.atlassian.net/browse/SCRUM-33)
**Como** desarrollador, **quiero** un puerto de almacenamiento independiente del proveedor **para** poder cambiar a S3 sin tocar el negocio.
- **Criterios:**
  - El proveedor se elige con `app.storage.provider`.
- **Tareas:**
  - [ ] Definir `StoragePort` y el modelo `UploadUrl`
  - [ ] Seleccionar el adaptador con `app.storage.provider`

### HU 5.2 – Subir imágenes a Cloudinary · [SCRUM-34](https://guzmanisaac2005.atlassian.net/browse/SCRUM-34)
**Como** administrador, **quiero** subir imágenes a Cloudinary **para** usarlas en proyectos y artículos.
- **Criterios:**
  - La subida va directo del navegador a Cloudinary con firma generada por el backend.
- **Tareas:**
  - [ ] Crear la cuenta de Cloudinary y guardar las credenciales en variables de entorno
  - [ ] Implementar `CloudinaryStorageAdapter`
  - [ ] Endpoint para solicitar la firma de subida
  - [ ] Tests del adaptador de Cloudinary

### HU 5.3 – Asociar y borrar imágenes · [SCRUM-35](https://guzmanisaac2005.atlassian.net/browse/SCRUM-35)
**Como** administrador, **quiero** asociar y borrar imágenes de proyectos y artículos **para** mantener el contenido ordenado.
- **Criterios:**
  - En la base de datos se guarda la clave, no la URL.
  - Al borrar una imagen se elimina también del proveedor.
- **Tareas:**
  - [ ] Guardar la clave de la imagen en la portada y la galería
  - [ ] Borrar la imagen del proveedor al eliminarla
  - [ ] Generar URLs optimizadas con formato y tamaño automáticos

---

## [SCRUM-6](https://guzmanisaac2005.atlassian.net/browse/SCRUM-6) · Sitio público: portafolio

### HU 6.1 – Base del frontend · [SCRUM-36](https://guzmanisaac2005.atlassian.net/browse/SCRUM-36)
**Como** desarrollador, **quiero** la base del frontend con layout y estilos **para** construir las páginas sobre ella.
- **Criterios:**
  - El menú es accesible con teclado y se cierra al navegar.
- **Tareas:**
  - [ ] Estructura `core`, `shared` y `features`, y rutas
  - [ ] Header con menú responsive accesible y footer
  - [ ] Variables de diseño (colores y tipografías)
  - [ ] Servicios de API e interceptor de errores

### HU 6.2 – Página de inicio · [SCRUM-37](https://guzmanisaac2005.atlassian.net/browse/SCRUM-37)
**Como** visitante, **quiero** ver el inicio con los servicios y los proyectos destacados **para** entender qué ofrece Isaac.
- **Criterios:**
  - Los proyectos destacados vienen de la API.
  - Se ve bien a 375 px, 768 px y 1440 px.
- **Tareas:**
  - [ ] Migrar las secciones hero, problema/solución, servicios, sobre mí y proceso
  - [ ] Mostrar los proyectos destacados desde la API
  - [ ] Sección de testimonios

### HU 6.3 – Lista de proyectos · [SCRUM-38](https://guzmanisaac2005.atlassian.net/browse/SCRUM-38)
**Como** visitante, **quiero** ver los proyectos y filtrarlos por WordPress o código **para** encontrar lo que me interesa.
- **Criterios:**
  - El filtro se refleja en la URL.
  - Hay estados de carga, vacío y error.
- **Tareas:**
  - [ ] Componente de tarjeta de proyecto
  - [ ] Página de lista con filtro por tipo
  - [ ] Estados de carga, vacío y error

### HU 6.4 – Detalle de proyecto · [SCRUM-39](https://guzmanisaac2005.atlassian.net/browse/SCRUM-39)
**Como** visitante, **quiero** ver el detalle de un proyecto **para** conocer el problema, la solución y el resultado.
- **Criterios:**
  - La URL usa el slug.
  - Un slug inexistente muestra una página 404.
- **Tareas:**
  - [ ] Página de detalle por slug con galería y tecnologías
  - [ ] Sección problema, solución y resultado en Markdown
  - [ ] Enlaces al sitio y al repositorio, y página 404

### HU 6.5 – Contacto · [SCRUM-40](https://guzmanisaac2005.atlassian.net/browse/SCRUM-40)
**Como** visitante, **quiero** contactar a Isaac desde el sitio **para** pedir una cotización.
- **Criterios:**
  - El formulario valida antes de enviar y muestra el resultado.
- **Tareas:**
  - [ ] Componente de formulario con validaciones
  - [ ] Integrar el envío y las notificaciones
  - [ ] Botón de WhatsApp y datos de contacto

---

## [SCRUM-7](https://guzmanisaac2005.atlassian.net/browse/SCRUM-7) · Blog tech

### HU 7.1 – Lista de artículos · [SCRUM-41](https://guzmanisaac2005.atlassian.net/browse/SCRUM-41)
**Como** lector, **quiero** ver la lista de artículos y filtrarla **para** encontrar temas que me interesan.
- **Criterios:**
  - Hay filtro por categoría y etiqueta, búsqueda y paginación.
- **Tareas:**
  - [ ] Componente de tarjeta de artículo
  - [ ] Página de lista con paginación
  - [ ] Filtros por categoría y etiqueta, y buscador

### HU 7.2 – Lectura de artículos · [SCRUM-42](https://guzmanisaac2005.atlassian.net/browse/SCRUM-42)
**Como** lector, **quiero** leer un artículo de forma cómoda **para** aprender sin distracciones.
- **Criterios:**
  - El código tiene resaltado y botón de copiar.
  - Se muestran el índice y el tiempo de lectura.
- **Tareas:**
  - [ ] Renderizar Markdown con ngx-markdown y Prism
  - [ ] Botón para copiar el código
  - [ ] Índice generado a partir de los títulos y tiempo de lectura
  - [ ] Soporte para vídeos incrustados y bloques de aviso

### HU 7.3 – Relacionados y compartir · [SCRUM-43](https://guzmanisaac2005.atlassian.net/browse/SCRUM-43)
**Como** lector, **quiero** ver artículos relacionados y compartir el artículo **para** seguir leyendo y recomendarlo.
- **Criterios:**
  - Se muestran hasta 3 relacionados.
  - Hay botones para LinkedIn, X y WhatsApp.
- **Tareas:**
  - [ ] Consulta de artículos relacionados en la API
  - [ ] Componente de artículos relacionados
  - [ ] Botones para compartir en LinkedIn, X y WhatsApp

### HU 7.4 – RSS y sitemap del blog · [SCRUM-44](https://guzmanisaac2005.atlassian.net/browse/SCRUM-44)
**Como** Isaac, **quiero** RSS y sitemap del blog **para** que Google y los lectores me sigan.
- **Criterios:**
  - El RSS es válido y el sitemap incluye los artículos publicados.
- **Tareas:**
  - [ ] Endpoint de RSS
  - [ ] Incluir los artículos en el sitemap

---

## [SCRUM-8](https://guzmanisaac2005.atlassian.net/browse/SCRUM-8) · Panel de administración

### HU 8.1 – Acceso al panel · [SCRUM-45](https://guzmanisaac2005.atlassian.net/browse/SCRUM-45)
**Como** administrador, **quiero** iniciar sesión en el panel **para** gestionar el contenido.
- **Criterios:**
  - Sin sesión, cualquier ruta de `/admin` redirige al login.
- **Tareas:**
  - [ ] Módulo `/admin` con carga diferida y layout
  - [ ] Pantalla de login
  - [ ] Guard de autenticación y cierre de sesión

### HU 8.2 – Gestión de proyectos · [SCRUM-46](https://guzmanisaac2005.atlassian.net/browse/SCRUM-46)
**Como** administrador, **quiero** gestionar los proyectos desde el panel **para** publicarlos sin tocar código.
- **Criterios:**
  - Se puede crear, editar, publicar, despublicar, destacar y borrar.
- **Tareas:**
  - [ ] Tabla de proyectos con estado y acciones
  - [ ] Formulario de crear y editar proyecto con validaciones
  - [ ] Acciones de publicar, despublicar y destacar

### HU 8.3 – Editor de artículos · [SCRUM-47](https://guzmanisaac2005.atlassian.net/browse/SCRUM-47)
**Como** administrador, **quiero** escribir artículos con un editor Markdown **para** publicar en el blog.
- **Criterios:**
  - La vista previa se actualiza en vivo.
- **Tareas:**
  - [ ] Tabla de artículos
  - [ ] Formulario con editor Markdown y vista previa en vivo
  - [ ] Gestión de categorías y etiquetas

### HU 8.4 – Subida de imágenes · [SCRUM-48](https://guzmanisaac2005.atlassian.net/browse/SCRUM-48)
**Como** administrador, **quiero** subir imágenes arrastrándolas **para** ilustrar proyectos y artículos rápido.
- **Criterios:**
  - Se muestra el progreso de subida y los errores.
- **Tareas:**
  - [ ] Componente de arrastrar y soltar con progreso
  - [ ] Integrar la firma de subida del backend
  - [ ] Selector de portada y galería

### HU 8.5 – Programar y previsualizar · [SCRUM-49](https://guzmanisaac2005.atlassian.net/browse/SCRUM-49)
**Como** administrador, **quiero** programar y previsualizar publicaciones **para** revisar antes de publicar.
- **Criterios:**
  - Un contenido programado aparece en el sitio a partir de su fecha.
- **Tareas:**
  - [ ] Selector de fecha de publicación
  - [ ] Vista previa de borradores
  - [ ] Indicadores de estado: borrador, programado y publicado

---

## [SCRUM-9](https://guzmanisaac2005.atlassian.net/browse/SCRUM-9) · SEO, rendimiento y lanzamiento v1.0

### HU 9.1 – Contenido visible para buscadores y redes · [SCRUM-50](https://guzmanisaac2005.atlassian.net/browse/SCRUM-50)
**Como** Isaac, **quiero** que Google y las redes vean el contenido **para** atraer visitas.
- **Criterios:**
  - El HTML llega renderizado desde el servidor.
  - Cada página tiene su título, descripción y Open Graph.
- **Tareas:**
  - [ ] Configurar `@angular/ssr`
  - [ ] Servicio de SEO para título, meta y Open Graph por página
  - [ ] Generar `sitemap.xml` y `robots.txt`

### HU 9.2 – Rendimiento y accesibilidad · [SCRUM-51](https://guzmanisaac2005.atlassian.net/browse/SCRUM-51)
**Como** visitante, **quiero** que el sitio sea rápido y accesible **para** usarlo desde cualquier dispositivo.
- **Criterios:**
  - Lighthouse por encima de 90 en las cuatro categorías.
- **Tareas:**
  - [ ] Optimizar imágenes con `NgOptimizedImage`
  - [ ] Auditoría con Lighthouse y correcciones
  - [ ] Revisión de accesibilidad: teclado, contraste y etiquetas

### HU 9.3 – Proyecto presentable para reclutadores · [SCRUM-52](https://guzmanisaac2005.atlassian.net/browse/SCRUM-52)
**Como** reclutador, **quiero** ver cómo está construido el proyecto **para** evaluar las habilidades de Isaac.
- **Criterios:**
  - El README explica la arquitectura y cómo correr el proyecto.
- **Tareas:**
  - [ ] README con diagrama de arquitectura, capturas y enlace a Swagger
  - [ ] Enlace "Ver código de este sitio" en el footer

### HU 9.4 – Lanzamiento v1.0.0 · [SCRUM-53](https://guzmanisaac2005.atlassian.net/browse/SCRUM-53)
**Como** Isaac, **quiero** lanzar la versión 1.0.0 **para** empezar a mostrar el portafolio.
- **Criterios:**
  - Producción funciona en el dominio propio y GitHub Pages apunta al nuevo sitio.
- **Tareas:**
  - [ ] Pruebas de humo en producción
  - [ ] `CHANGELOG` y etiqueta `v1.0.0`
  - [ ] Redirigir GitHub Pages al nuevo dominio
  - [ ] Publicar el lanzamiento en LinkedIn

---

## [SCRUM-10](https://guzmanisaac2005.atlassian.net/browse/SCRUM-10) · Contenido para el lanzamiento

### HU 10.1 – Casos en WordPress · [SCRUM-54](https://guzmanisaac2005.atlassian.net/browse/SCRUM-54)
**Como** cliente potencial, **quiero** ver casos reales en WordPress **para** confiar en el trabajo de Isaac.
- **Criterios:**
  - Al menos 3 casos con capturas, objetivo y resultado.
- **Tareas:**
  - [ ] Caso de estudio de la galería Paredes Expression
  - [ ] Crear 2 sitios de ejemplo en WordPress
  - [ ] Capturas y textos de cada caso

### HU 10.2 – Proyectos de código · [SCRUM-55](https://guzmanisaac2005.atlassian.net/browse/SCRUM-55)
**Como** reclutador, **quiero** ver proyectos de código documentados **para** evaluar cómo programa Isaac.
- **Criterios:**
  - Cada proyecto tiene un README completo.
- **Tareas:**
  - [ ] Completar los README de los proyectos de código
  - [ ] Ficha de la plataforma del portafolio como proyecto

### HU 10.3 – Primeros artículos · [SCRUM-56](https://guzmanisaac2005.atlassian.net/browse/SCRUM-56)
**Como** lector, **quiero** encontrar artículos desde el lanzamiento **para** que el blog tenga valor desde el día 1.
- **Criterios:**
  - Al menos 2 artículos publicados.
- **Tareas:**
  - [ ] Escribir el artículo sobre EmailJS
  - [ ] Escribir el artículo "Por qué estoy rehaciendo mi portafolio"

### HU 10.4 – Testimonios · [SCRUM-57](https://guzmanisaac2005.atlassian.net/browse/SCRUM-57)
**Como** cliente potencial, **quiero** ver testimonios **para** tener prueba social.
- **Criterios:**
  - Al menos 1 testimonio real.
- **Tareas:**
  - [ ] Pedir un testimonio a la galería Paredes Expression
  - [ ] Cargar los testimonios en el sitio

---

## [SCRUM-11](https://guzmanisaac2005.atlassian.net/browse/SCRUM-11) · Observabilidad con Prometheus y Grafana (v1.1)

### HU 11.1 – Métricas de la API · [SCRUM-58](https://guzmanisaac2005.atlassian.net/browse/SCRUM-58)
**Como** desarrollador, **quiero** exponer métricas de la API **para** saber cómo se comporta.
- **Criterios:**
  - El endpoint de métricas no es público.
- **Tareas:**
  - [ ] Agregar Actuator y `micrometer-registry-prometheus`
  - [ ] Proteger el endpoint de métricas
  - [ ] Métricas personalizadas de publicaciones y contactos

### HU 11.2 – Dashboards en local · [SCRUM-59](https://guzmanisaac2005.atlassian.net/browse/SCRUM-59)
**Como** desarrollador, **quiero** ver dashboards en local **para** probar las métricas mientras desarrollo.
- **Criterios:**
  - `docker compose up` levanta Prometheus y Grafana con un dashboard listo.
- **Tareas:**
  - [ ] Agregar Prometheus y Grafana a `docker-compose`
  - [ ] Dashboard de JVM y HTTP

### HU 11.3 – Monitoreo en producción · [SCRUM-60](https://guzmanisaac2005.atlassian.net/browse/SCRUM-60)
**Como** desarrollador, **quiero** monitorear producción con alertas **para** enterarme de los errores antes que los usuarios.
- **Criterios:**
  - Llega una alerta si la tasa de errores sube.
- **Tareas:**
  - [ ] Configurar Grafana Cloud
  - [ ] Enviar las métricas de producción
  - [ ] Alerta de tasa de errores
  - [ ] Registrar la línea base de latencia

---

## [SCRUM-12](https://guzmanisaac2005.atlassian.net/browse/SCRUM-12) · Caché y límite de intentos con Redis (v1.2)

### HU 12.1 – Caché de contenido público · [SCRUM-61](https://guzmanisaac2005.atlassian.net/browse/SCRUM-61)
**Como** visitante, **quiero** respuestas más rápidas **para** navegar sin esperas.
- **Criterios:**
  - Al publicar se invalida la caché.
  - Si Redis cae, la API sigue respondiendo.
- **Tareas:**
  - [ ] Redis en `docker-compose` y Testcontainers
  - [ ] Spring Cache con Redis en los endpoints públicos
  - [ ] Invalidar la caché al publicar o editar
  - [ ] Tolerancia a fallos si Redis no está disponible

### HU 12.2 – Límite de intentos distribuido · [SCRUM-62](https://guzmanisaac2005.atlassian.net/browse/SCRUM-62)
**Como** Isaac, **quiero** el límite de intentos en Redis **para** que funcione con varias instancias.
- **Criterios:**
  - El límite se respeta aunque se reinicie la aplicación.
- **Tareas:**
  - [ ] Adaptador Redis de `RateLimiterPort`
  - [ ] Tests del límite de intentos con Redis

### HU 12.3 – Medir la mejora · [SCRUM-63](https://guzmanisaac2005.atlassian.net/browse/SCRUM-63)
**Como** desarrollador, **quiero** medir la mejora de la caché **para** demostrar su impacto.
- **Criterios:**
  - Hay una comparación de latencia antes y después en Grafana.
- **Tareas:**
  - [ ] Configurar Redis en producción (Upstash o Redis Cloud)
  - [ ] Comparar la latencia antes y después en Grafana
  - [ ] Escribir un artículo con los resultados

---

## [SCRUM-13](https://guzmanisaac2005.atlassian.net/browse/SCRUM-13) · Procesamiento asíncrono con RabbitMQ (v1.3)

### HU 13.1 – Eventos por RabbitMQ · [SCRUM-64](https://guzmanisaac2005.atlassian.net/browse/SCRUM-64)
**Como** desarrollador, **quiero** publicar los eventos de dominio por RabbitMQ **para** procesar tareas en segundo plano.
- **Criterios:**
  - El cambio de eventos en memoria a RabbitMQ solo toca el adaptador.
- **Tareas:**
  - [ ] RabbitMQ en `docker-compose` y Testcontainers
  - [ ] Implementar `RabbitMqEventPublisherAdapter`
  - [ ] Configurar CloudAMQP en producción

### HU 13.2 – Correo de contacto asíncrono · [SCRUM-65](https://guzmanisaac2005.atlassian.net/browse/SCRUM-65)
**Como** visitante, **quiero** que el formulario responda al instante **para** no esperar al proveedor de correo.
- **Criterios:**
  - Los fallos se reintentan y terminan en la dead-letter queue.
- **Tareas:**
  - [ ] Cola y consumidor del correo de contacto
  - [ ] Reintentos y dead-letter queue
  - [ ] Reprocesar los mensajes fallidos

### HU 13.3 – Optimización de imágenes asíncrona · [SCRUM-66](https://guzmanisaac2005.atlassian.net/browse/SCRUM-66)
**Como** administrador, **quiero** que las imágenes se optimicen solas **para** no hacerlo a mano.
- **Criterios:**
  - Cada imagen subida tiene versión WebP y miniatura.
- **Tareas:**
  - [ ] Cola de procesamiento de imágenes
  - [ ] Generar WebP y miniaturas

### HU 13.4 – Sitemap automático · [SCRUM-67](https://guzmanisaac2005.atlassian.net/browse/SCRUM-67)
**Como** Isaac, **quiero** que el sitemap se regenere al publicar **para** que Google vea el contenido nuevo.
- **Criterios:**
  - Al publicar, el sitemap se actualiza sin intervención.
- **Tareas:**
  - [ ] Evento `ContentPublished` y su consumidor
  - [ ] Documentar en el README por qué se usa RabbitMQ
