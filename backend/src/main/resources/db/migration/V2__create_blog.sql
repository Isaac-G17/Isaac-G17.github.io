-- Blog: artículos, categorías fijas y etiquetas libres (muchos a muchos con artículos).

CREATE TABLE categories (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR(60)  NOT NULL UNIQUE,
    slug        VARCHAR(120) NOT NULL UNIQUE,
    description VARCHAR(300)
);

-- Categorías definidas en docs/03-blog.md.
INSERT INTO categories (name, slug, description) VALUES
    ('Desarrollo web',     'desarrollo-web',     'WordPress, rendimiento, SEO técnico, frontend'),
    ('Backend',            'backend',            'Spring Boot, APIs, bases de datos'),
    ('Tutoriales',         'tutoriales',         'Paso a paso con código'),
    ('Herramientas',       'herramientas',       'Git, Docker, VS Code, IA para desarrolladores'),
    ('Opinión y noticias', 'opinion-y-noticias', 'Tendencias, lanzamientos, reflexiones'),
    ('Mi camino',          'mi-camino',          'Lo que estoy aprendiendo y errores que cometí');

CREATE TABLE tags (
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(60)  NOT NULL UNIQUE,
    slug VARCHAR(120) NOT NULL UNIQUE
);

CREATE TABLE posts (
    id                   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title                VARCHAR(150) NOT NULL,
    slug                 VARCHAR(120) NOT NULL UNIQUE,
    excerpt              VARCHAR(500) NOT NULL,
    content              TEXT         NOT NULL DEFAULT '',
    cover_image_url      VARCHAR(500),
    category_id          BIGINT       NOT NULL REFERENCES categories (id),
    reading_time_minutes INTEGER      NOT NULL CHECK (reading_time_minutes > 0),
    status               VARCHAR(20)  NOT NULL DEFAULT 'DRAFT' CHECK (status IN ('DRAFT', 'PUBLISHED')),
    featured             BOOLEAN      NOT NULL DEFAULT FALSE,
    seo_title            VARCHAR(70),
    seo_description      VARCHAR(160),
    published_at         TIMESTAMPTZ,
    created_at           TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at           TIMESTAMPTZ  NOT NULL DEFAULT now(),
    CONSTRAINT posts_published_has_date CHECK (status <> 'PUBLISHED' OR published_at IS NOT NULL)
);

-- La lista pública filtra por estado y ordena por fecha de publicación.
CREATE INDEX posts_status_published_at_idx ON posts (status, published_at DESC);
CREATE INDEX posts_category_idx ON posts (category_id);

CREATE TABLE post_tags (
    post_id BIGINT NOT NULL REFERENCES posts (id) ON DELETE CASCADE,
    tag_id  BIGINT NOT NULL REFERENCES tags (id),
    PRIMARY KEY (post_id, tag_id)
);
