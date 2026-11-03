package dev.isaacguzman.portfolio.post.domain;

import dev.isaacguzman.portfolio.shared.domain.Slug;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Artículo del blog. Modelo de dominio: sin anotaciones de JPA ni de Spring.
 *
 * @param readingTimeMinutes se calcula con {@link ReadingTime} al guardar
 * @param seoTitle           opcional; si está vacío se usa el título
 * @param seoDescription     opcional; si está vacío se usa el extracto
 */
public record Post(
		Long id,
		String title,
		Slug slug,
		String excerpt,
		String content,
		String coverImageUrl,
		Category category,
		List<Tag> tags,
		int readingTimeMinutes,
		PostStatus status,
		boolean featured,
		String seoTitle,
		String seoDescription,
		Instant publishedAt,
		Instant createdAt,
		Instant updatedAt) {

	public Post {
		Objects.requireNonNull(title, "El título es obligatorio");
		Objects.requireNonNull(slug, "El slug es obligatorio");
		Objects.requireNonNull(category, "La categoría es obligatoria");
		Objects.requireNonNull(status, "El estado es obligatorio");
		if (status == PostStatus.PUBLISHED && publishedAt == null) {
			throw new IllegalArgumentException("Un artículo publicado necesita fecha de publicación");
		}
		tags = tags == null ? List.of() : List.copyOf(tags);
	}

	/** Visible en el sitio público: publicado y con la fecha ya cumplida (no programado a futuro). */
	public boolean isVisibleAt(Instant now) {
		return status == PostStatus.PUBLISHED && !publishedAt.isAfter(now);
	}

	public String effectiveSeoTitle() {
		return seoTitle == null || seoTitle.isBlank() ? title : seoTitle;
	}

	public String effectiveSeoDescription() {
		return seoDescription == null || seoDescription.isBlank() ? excerpt : seoDescription;
	}
}
