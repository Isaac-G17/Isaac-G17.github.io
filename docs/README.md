# Documentación del proyecto

Documentación del portafolio de Isaac Guzmán: estado del sitio actual y plan para la nueva versión con Spring Boot y Angular.

## Índice

| Documento | Contenido |
|---|---|
| [01 - Revisión del sitio actual](01-revision-sitio-actual.md) | Problemas encontrados en `main` y `develop`, y recomendaciones de mejora ordenadas por prioridad |
| [02 - Plan del MVP](02-plan-mvp.md) | Alcance, modelo de datos, stack, infraestructura y sprints de la nueva plataforma |
| [03 - Blog tech](03-blog.md) | Categorías, modelo, funciones e ideas de contenido para el blog |
| [04 - Arquitectura](04-arquitectura.md) | Arquitectura hexagonal del backend, puertos y adaptadores, tests por capa y estructura del frontend |

## Contexto

- **Sitio actual:** HTML, CSS y JavaScript estático publicado en GitHub Pages ([isaac-g17.github.io](https://isaac-g17.github.io)).
- **Objetivo:** convertirlo en una plataforma con panel de administración para publicar proyectos (WordPress y código) y artículos sin tocar código.
- **Stack elegido:** Spring Boot con arquitectura hexagonal (backend), Angular (frontend), PostgreSQL y Cloudinary.
- **Después del MVP:** Prometheus y Grafana, Redis, RabbitMQ y AWS.
- **Gestión:** Scrum en Jira, espacio Portafolio. Las historias de usuario, las tareas y su estado viven solo en Jira.

> Última actualización: 2026-10-16
