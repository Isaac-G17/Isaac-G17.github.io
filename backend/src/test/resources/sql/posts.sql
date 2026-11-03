-- Datos para PublicPostApiIntegrationTest. Las categorías vienen de la migración V2.
DELETE FROM post_tags;
DELETE FROM posts;
DELETE FROM tags;

INSERT INTO tags (name, slug) VALUES ('Java', 'java'), ('Spring', 'spring'), ('Angular', 'angular');

INSERT INTO posts (title, slug, excerpt, content, category_id, reading_time_minutes, status, seo_title, published_at)
VALUES
    ('Introducción a Spring Boot', 'intro-spring', 'Primeros pasos', '# Spring',
     (SELECT id FROM categories WHERE slug = 'backend'), 5, 'PUBLISHED', NULL, '2026-10-01T10:00:00Z'),
    ('Signals en Angular', 'angular-signals', 'Estado reactivo', '# Signals',
     (SELECT id FROM categories WHERE slug = 'desarrollo-web'), 4, 'PUBLISHED', NULL, '2026-10-10T10:00:00Z'),
    ('Tests con Testcontainers', 'testcontainers', 'Base de datos real en los tests', '# Testcontainers',
     (SELECT id FROM categories WHERE slug = 'backend'), 7, 'PUBLISHED', 'Testcontainers con Spring', '2026-10-20T10:00:00Z'),
    ('Velocidad al 100% en WordPress', 'wordpress-rapido', 'Checklist de rendimiento', '# Rápido',
     (SELECT id FROM categories WHERE slug = 'herramientas'), 3, 'PUBLISHED', NULL, '2026-09-01T10:00:00Z'),
    ('Artículo programado de Spring', 'programado', 'Sale en el futuro', '# Futuro',
     (SELECT id FROM categories WHERE slug = 'backend'), 2, 'PUBLISHED', NULL, '2099-01-01T10:00:00Z'),
    ('Borrador de Spring', 'borrador', 'Sin publicar', '',
     (SELECT id FROM categories WHERE slug = 'backend'), 1, 'DRAFT', NULL, NULL);

INSERT INTO post_tags (post_id, tag_id)
SELECT p.id, t.id FROM posts p, tags t
WHERE (p.slug = 'intro-spring' AND t.slug IN ('java', 'spring'))
   OR (p.slug = 'testcontainers' AND t.slug = 'java')
   OR (p.slug = 'angular-signals' AND t.slug = 'angular')
   OR (p.slug = 'programado' AND t.slug = 'java');
