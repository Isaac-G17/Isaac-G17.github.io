# 03 - Blog tech

El blog es un espacio de **contenido tech en general**, no solo tutoriales de código.

---

## 1. Categorías y etiquetas

Pocas **categorías** fijas y muchas **etiquetas** libres.

| Categoría | Ejemplos de temas |
|---|---|
| **Desarrollo web** | WordPress, rendimiento, SEO técnico, frontend |
| **Backend** | Spring Boot, APIs, bases de datos |
| **Tutoriales** | Paso a paso con código |
| **Herramientas** | Git, Docker, VS Code, IA para desarrolladores |
| **Opinión y noticias** | Tendencias, lanzamientos, reflexiones |
| **Mi camino** | Lo que estoy aprendiendo y errores que cometí |

Las etiquetas (`#java`, `#angular`, `#wordpress`, `#ia`) sirven para filtrar con más detalle.

---

## 2. Modelo de datos

```
Post
  id, title, slug, excerpt, content (markdown),
  coverImageUrl, category, tags[],
  readingTime (se calcula al guardar),
  status (DRAFT | PUBLISHED), featured,
  seoTitle, seoDescription,   // opcionales; si están vacíos se usan title y excerpt
  publishedAt, updatedAt

Category (id, name, slug, description)
Tag      (id, name, slug)     // muchos a muchos con Post
```

---

## 3. Funciones

### Sitio público (MVP)

- Lista de artículos con **filtro por categoría y etiqueta**, y buscador por título.
- **Página del artículo:**
  - Tiempo de lectura.
  - Índice generado a partir de los títulos `##`.
  - Resaltado de código con botón de copiar.
  - Imágenes, vídeos de YouTube incrustados y bloques de cita o aviso.
  - "Artículos relacionados" según categoría y etiquetas.
  - Botones para compartir en LinkedIn, X y WhatsApp.
- **Etiquetas Open Graph por artículo** (requiere el SSR del [plan del MVP](02-plan-mvp.md)).
- **Sitemap y RSS.**

### Panel de administración (MVP)

- Editor Markdown con vista previa en vivo y subida de imágenes arrastrando.
- Borradores y **publicación programada**: la API pública filtra por `publishedAt <= now()`.
- Vista previa del borrador antes de publicar.

### Para la v2

- Series de artículos (por ejemplo "Construyendo mi portafolio, parte 1, 2, 3…").
- Contador de vistas.
- Comentarios.
- Newsletter.

---

## 4. Ideas de contenido para arrancar

1. **"Cómo conecté un formulario de contacto sin backend usando EmailJS"**. Basado en el trabajo de la rama `develop`.
2. **"Por qué estoy rehaciendo mi portafolio con Spring Boot y Angular"**. Abre una serie que documenta el proyecto.
3. **"Checklist para que tu sitio en WordPress cargue rápido"**. Dirigido a clientes; posiciona como experto.
4. **"5 errores que cometí en mi primer portafolio"**. Sale de la [revisión del sitio actual](01-revision-sitio-actual.md).
5. **Un tema de opinión**, por ejemplo cómo uso la IA en mi flujo como desarrollador.

**Estrategia:** alternar artículos **para clientes** (WordPress, SEO, rendimiento) con artículos **para desarrolladores** (Spring, Angular), para que el blog trabaje para los dos públicos.
