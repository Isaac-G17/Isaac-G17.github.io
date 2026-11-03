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

	/** Publicado con una fecha que todavía no llega. */
	public boolean isScheduledAt(Instant now) {
		return status == PostStatus.PUBLISHED && publishedAt.isAfter(now);
	}

	public String effectiveSeoTitle() {
		return seoTitle == null || seoTitle.isBlank() ? title : seoTitle;
	}

	public String effectiveSeoDescription() {
		return seoDescription == null || seoDescription.isBlank() ? excerpt : seoDescription;
	}

	/** Artículo nuevo: siempre empieza como borrador. El tiempo de lectura se calcula del contenido. */
	public static Post draft(PostDetails details, Instant now) {
		return new Post(null, details.title(), details.slug(), details.excerpt(), details.content(),
				details.coverImageUrl(), details.category(), details.tags(), ReadingTime.minutesFor(details.content()),
				PostStatus.DRAFT, details.featured(), details.seoTitle(), details.seoDescription(), null, now, now);
	}

	/** Cambia los datos editables y recalcula el tiempo de lectura. El estado y la fecha no cambian. */
	public Post update(PostDetails details, Instant now) {
		return new Post(id, details.title(), details.slug(), details.excerpt(), details.content(),
				details.coverImageUrl(), details.category(), details.tags(), ReadingTime.minutesFor(details.content()),
				status, details.featured(), details.seoTitle(), details.seoDescription(), publishedAt, createdAt, now);
	}

	/**
	 * Publica en la fecha indicada. Si es futura, el artículo queda programado: no se ve hasta ese momento.
	 * Se puede volver a llamar para cambiar la fecha de un artículo programado.
	 */
	public Post publishAt(Instant publicationDate, Instant now) {
		Objects.requireNonNull(publicationDate, "La fecha de publicación es obligatoria");
		return new Post(id, title, slug, excerpt, content, coverImageUrl, category, tags, readingTimeMinutes,
				PostStatus.PUBLISHED, featured, seoTitle, seoDescription, publicationDate, createdAt, now);
	}

	/** Vuelve a borrador y quita la fecha (también cancela una publicación programada). */
	public Post unpublish(Instant now) {
		if (status == PostStatus.DRAFT) {
			return this;
		}
		return new Post(id, title, slug, excerpt, content, coverImageUrl, category, tags, readingTimeMinutes,
				PostStatus.DRAFT, featured, seoTitle, seoDescription, null, createdAt, now);
	}
}
