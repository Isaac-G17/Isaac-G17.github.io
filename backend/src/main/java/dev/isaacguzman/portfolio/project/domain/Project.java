package dev.isaacguzman.portfolio.project.domain;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Proyecto del portafolio. Modelo de dominio: sin anotaciones de JPA ni de Spring.
 *
 * @param technologies nombres de las tecnologías usadas, por ejemplo "Angular"
 * @param publishedAt  fecha de publicación; obligatoria si el proyecto está publicado
 */
public record Project(
		Long id,
		String title,
		Slug slug,
		ProjectType type,
		String summary,
		String content,
		String coverImageUrl,
		List<String> technologies,
		String liveUrl,
		String repoUrl,
		boolean featured,
		ProjectStatus status,
		Instant publishedAt,
		Instant createdAt,
		Instant updatedAt) {

	public Project {
		Objects.requireNonNull(title, "El título es obligatorio");
		Objects.requireNonNull(slug, "El slug es obligatorio");
		Objects.requireNonNull(type, "El tipo es obligatorio");
		Objects.requireNonNull(status, "El estado es obligatorio");
		if (status == ProjectStatus.PUBLISHED && publishedAt == null) {
			throw new IllegalArgumentException("Un proyecto publicado necesita fecha de publicación");
		}
		technologies = technologies == null ? List.of() : List.copyOf(technologies);
	}

	public boolean isPublished() {
		return status == ProjectStatus.PUBLISHED;
	}
}
