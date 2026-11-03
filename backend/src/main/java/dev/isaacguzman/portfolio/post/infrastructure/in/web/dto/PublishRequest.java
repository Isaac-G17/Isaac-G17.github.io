package dev.isaacguzman.portfolio.post.infrastructure.in.web.dto;

import java.time.Instant;

/**
 * Cuerpo opcional de "publicar". Sin {@code publishAt}, se publica ya; con una fecha futura,
 * queda programado. Ejemplo: {@code { "publishAt": "2026-12-01T13:00:00Z" }}.
 */
public record PublishRequest(Instant publishAt) {
}
