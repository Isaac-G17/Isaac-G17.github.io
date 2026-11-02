-- Proyectos del portafolio y las tecnologías que usan (relación muchos a muchos).

CREATE TABLE technologies (
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(60) NOT NULL UNIQUE
);

CREATE TABLE projects (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title           VARCHAR(150) NOT NULL,
    slug            VARCHAR(120) NOT NULL UNIQUE,
    type            VARCHAR(20)  NOT NULL CHECK (type IN ('WORDPRESS', 'CODE')),
    summary         VARCHAR(500) NOT NULL,
    content         TEXT         NOT NULL DEFAULT '',
    cover_image_url VARCHAR(500),
    live_url        VARCHAR(500),
    repo_url        VARCHAR(500),
    featured        BOOLEAN      NOT NULL DEFAULT FALSE,
    status          VARCHAR(20)  NOT NULL DEFAULT 'DRAFT' CHECK (status IN ('DRAFT', 'PUBLISHED')),
    published_at    TIMESTAMPTZ,
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ  NOT NULL DEFAULT now(),
    -- Un proyecto publicado siempre tiene fecha de publicación.
    CONSTRAINT projects_published_has_date CHECK (status <> 'PUBLISHED' OR published_at IS NOT NULL)
);

-- La API pública siempre filtra por estado y, a veces, por tipo.
CREATE INDEX projects_status_type_idx ON projects (status, type);

CREATE TABLE project_technologies (
    project_id    BIGINT NOT NULL REFERENCES projects (id) ON DELETE CASCADE,
    technology_id BIGINT NOT NULL REFERENCES technologies (id),
    PRIMARY KEY (project_id, technology_id)
);
