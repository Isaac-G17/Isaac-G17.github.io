-- Datos para PublicProjectApiIntegrationTest: dos publicados (uno de cada tipo) y un borrador.
DELETE FROM project_technologies;
DELETE FROM projects;
DELETE FROM technologies;

INSERT INTO technologies (name) VALUES ('Spring Boot'), ('Angular'), ('WordPress');

INSERT INTO projects (title, slug, type, summary, content, live_url, repo_url, featured, status, published_at)
VALUES
    ('Portafolio', 'portafolio', 'CODE', 'Esta misma plataforma', '# Portafolio',
     'https://isaac-g17.github.io', 'https://github.com/Isaac-G17/Isaac-G17.github.io', TRUE, 'PUBLISHED', '2026-10-01T10:00:00Z'),
    ('Tienda de ejemplo', 'tienda-ejemplo', 'WORDPRESS', 'Tienda en WordPress', '# Tienda',
     'https://example.com', NULL, FALSE, 'PUBLISHED', '2026-09-01T10:00:00Z'),
    ('Proyecto en borrador', 'borrador', 'CODE', 'Todavía no se publica', '', NULL, NULL, FALSE, 'DRAFT', NULL);

INSERT INTO project_technologies (project_id, technology_id)
SELECT p.id, t.id FROM projects p, technologies t
WHERE (p.slug = 'portafolio' AND t.name IN ('Spring Boot', 'Angular'))
   OR (p.slug = 'tienda-ejemplo' AND t.name = 'WordPress');
