package dev.isaacguzman.portfolio.project.domain;

import dev.isaacguzman.portfolio.shared.domain.Slug;
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

	/** Proyecto nuevo: siempre empieza como borrador. */
	public static Project draft(ProjectDetails details, Instant now) {
		return new Project(null, details.title(), details.slug(), details.type(), details.summary(),
				details.content(), details.coverImageUrl(), details.technologies(), details.liveUrl(),
				details.repoUrl(), details.featured(), ProjectStatus.DRAFT, null, now, now);
	}

	/** Cambia los datos editables; el estado y la fecha de publicación no cambian al editar. */
	public Project update(ProjectDetails details, Instant now) {
		return new Project(id, details.title(), details.slug(), details.type(), details.summary(),
				details.content(), details.coverImageUrl(), details.technologies(), details.liveUrl(),
				details.repoUrl(), details.featured(), status, publishedAt, createdAt, now);
	}

	/** Publicar dos veces no cambia la fecha de la primera publicación. */
	public Project publish(Instant now) {
		if (isPublished()) {
			return this;
		}
		return new Project(id, title, slug, type, summary, content, coverImageUrl, technologies, liveUrl,
				repoUrl, featured, ProjectStatus.PUBLISHED, now, createdAt, now);
	}

	/**
	 * Vuelve a borrador y quita la fecha de publicación: si se publica de nuevo, toma la fecha nueva.
	 * Despublicar un borrador no cambia nada.
	 */
	public Project unpublish(Instant now) {
		if (!isPublished()) {
			return this;
		}
		return new Project(id, title, slug, type, summary, content, coverImageUrl, technologies, liveUrl,
				repoUrl, featured, ProjectStatus.DRAFT, null, createdAt, now);
	}
}
