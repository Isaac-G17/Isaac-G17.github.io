# 01 - Revisión del sitio actual

Revisión del portafolio estático (rama `main`) y de los cambios pendientes en `develop`.

> Fecha de revisión: 2026-10-16

---

## 1. Problemas críticos

| Estado | Problema | Ubicación | Solución |
|---|---|---|---|
| ✅ Resuelto en `develop` | El formulario de contacto no enviaba nada (`action=""`) | `index.html` | Integrado con EmailJS en `src/js/contact.js` |
| ⏳ Pendiente | Botón "Solicitar mi página web" de la sección Sobre mí con `href=""` | `index.html` | Usar `https://wa.link/d57q3q` |
| ⏳ Pendiente | Botón "Escríbeme por WhatsApp" con `href=""` | `index.html` | Usar el mismo enlace de WhatsApp |
| ⏳ Pendiente | Correo y teléfono no se pueden pulsar | `index.html` | Enlaces `mailto:` y `https://wa.me/573122733087` |
| ⏳ Pendiente | Error de JavaScript en la página de proyectos: no existe `#mover-btn` | `src/js/script.js` | Envolver el código en `if (btn) { ... }` |

## 2. Errores de texto

| Dice | Debe decir |
|---|---|
| opbtener | obtener |
| Actualamente | Actualmente |
| fronted | frontend |
| incríble | increíble |
| estrategico | estratégico |
| pagina (en los botones) | página |
| wordpress | WordPress |
| Ecommerce que venden | Tiendas online que venden |
| sitios rápidos… | Sitios rápidos… (mayúscula) |

En el código ya se corrigieron en `develop`: `landig-pages` → `landing-pages` y `mensjae` → `mensaje`.

## 3. Contenido y posicionamiento

- **Los proyectos no respaldan la oferta.** El hero vende WordPress para negocios, pero dos de los cuatro proyectos son programas de consola en Python (cajero e inventario).
  - Moverlos a una sección de "Aprendizaje" o quitarlos.
  - Quitar el portafolio enlazado a sí mismo como proyecto.
  - Crear 2 o 3 sitios de ejemplo en WordPress (landing, tienda, restaurante), aunque sean ficticios.
- **Convertir la galería de arte en un caso de estudio:** problema, solución y resultado medible.
- **Agregar prueba social:** testimonios o logos de clientes.
- **El mensaje "Yo me encargo de eso" está escondido** detrás del botón de flechas. Mostrarlo siempre o animarlo al hacer scroll.
- **Footer:** agregar LinkedIn, WhatsApp y correo, no solo GitHub.

## 4. Rendimiento

El sitio vende "sitios rápidos", así que debe serlo.

**Imágenes demasiado pesadas** (convertir a WebP con [Squoosh](https://squoosh.app), objetivo < 150 KB):

| Archivo | Tamaño |
|---|---|
| `public/img/Orange_light.png` | 1,4 MB |
| `public/img/ligh_servicios.png` | 1 MB |
| `public/img/light-about-me.png` | 795 KB |
| `public/img/proyectos/Simulador de Cajero Automático en Python.png` | 1,8 MB |
| `public/img/proyectos/Sistema de inventario.png` | 619 KB |
| `public/img/proyectos/portafolio.jpg` | 614 KB |

**Otras mejoras:**

- Borrar las dos imágenes `ChatGPT Image 24 abr 2026…` (1,7 MB cada una, duplicadas y sin uso).
- Revisar si `PUNTO 1/2/3/4.png` se usan; parece que no.
- Agregar `loading="lazy"`, `width` y `height` a las imágenes de proyectos.
- **Fuentes:** se cargan los 18 pesos de Poppins y solo se usan 400 a 800. Unir las tres fuentes en un solo enlace de Google Fonts con esos pesos.
- **CSS:** los `@import` cargan los archivos en serie, y los `.min.css` se mantienen a mano (`proyectos.html` ni siquiera usa el `.min`). Usar los archivos normales o automatizar la minificación.
- Medir con **Lighthouse** y usar la nota como argumento de venta.

## 5. SEO y metadatos

- Cambiar `lang="en"` por `lang="es"` en las dos páginas.
- Agregar `<meta name="description">` y favicon.
- Agregar etiquetas **Open Graph** (`og:title`, `og:description`, `og:image`) para la vista previa en WhatsApp y LinkedIn.
- Títulos distintos por página, por ejemplo:
  - "Isaac Guzmán | Desarrollador WordPress"
  - "Proyectos | Isaac Guzmán"

## 6. Calidad del código y responsive

- **Alturas fijas en móvil** (`1216px`, `1110px`, `1092px`, `1252px`, `968px` en `src/css/responsive.css`): se descuadran si cambia el texto. Usar `padding` y `min-height`.
- **Anchos fijos:** `.problem h2 { width: 605px }` y `.card-step { width: 400px }`. Cambiar por `max-width`.
- Las fotos se acomodan con `translateX(-17%)`, `(-30%)`, etc. y `overflow: hidden`. Es más limpio con `object-position` o grid.
- `body { max-height: 100vh }` en `src/css/proyectos.css` sobra.
- El CSS del header está duplicado en los dos media queries.

## 7. Accesibilidad

- Los campos del formulario solo tienen placeholder y `outline: none`. Agregar `<label>` (pueden ser ocultos) y un estilo `:focus` visible.
- **Menú hamburguesa:**
  - Agregar `aria-expanded`.
  - Cerrarlo al pulsar un enlace.
- Agregar `aria-label` al botón de flechas.
- Agregar `rel="noopener"` a los enlaces con `target="_blank"`.

## 8. Detalles menores

- El `README.md` de la raíz todavía menciona `mascotas.html` y `mascotas.css`, que ya se borraron.

---

## 9. Revisión de la rama `develop`

`develop` tiene 5 commits por delante de `main`, centrados en el formulario de contacto.

### Lo que quedó bien

- El formulario envía con EmailJS (`src/js/contact.js`), valida los datos y muestra notificaciones de éxito o error.
- Las notificaciones están separadas en `src/utils/notificaciones.js`, son responsive y el botón de cerrar tiene `aria-label`.

### Corregir antes de fusionar con `main`

1. **La validación propia casi nunca se ve.** Los campos tienen `required`, así que el navegador muestra su propio globo antes. Agregar `novalidate` al `<form>` para usar solo las notificaciones.
2. **El celular rechaza números reales.** La regex `/^[0-9]{7,15}$/` no acepta `+57 312 273 3087` ni `312-273-3087`:
   ```js
   const clean = phone.replace(/[\s\-()]/g, "");
   return /^\+?[0-9]{7,15}$/.test(clean);
   ```
3. **Doble envío.** Desactivar el botón y mostrar "Enviando…" mientras se espera a EmailJS; reactivarlo en un `finally`.
4. **Proteger la cuenta de EmailJS** desde su panel:
   - Restringir los dominios permitidos a `isaac-g17.github.io`.
   - Activar un límite de envíos o reCAPTCHA para evitar spam.
5. **Fijar la versión del script:** usar una versión exacta como `@emailjs/browser@4.4.1` en vez de `@4`.
6. **Detalles menores:**
   - Quitar el `console.log("Mensaje enviado…")`.
   - La regex del email rechaza dominios que empiezan con número (`@163.com`). Alternativa simple: `/^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/`.
   - El aviso tiene `role="alert"` y `aria-live="polite"` a la vez. Usar solo `role="status"` para los de éxito.
   - `notificaciones.css` se carga con `./src/...` y el resto con `/src/...`. Unificar.
   - Confirmar que la plantilla de EmailJS use `{{name}}`, `{{phone}}`, `{{email}}`, `{{service}}` y `{{mensaje}}`.

### Historial de Git

Hay tres commits con el mismo mensaje y un revert en medio. Al hacer el PR a `main`, usar **"Squash and merge"** para dejar un solo commit limpio.

---

## Prioridad sugerida

1. Formulario (puntos 1 a 3 de la sección 9) y enlaces vacíos.
2. Errores de texto.
3. Optimización de imágenes.
4. SEO y metadatos.
5. Contenido de proyectos.
6. Responsive, accesibilidad y limpieza de código.
