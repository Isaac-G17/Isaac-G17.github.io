package dev.isaacguzman.portfolio.post.infrastructure.in.web.dto;

import java.time.Instant;
import java.util.List;

/** Respuestas JSON del blog público. */
public final class PostResponses {

	private PostResponses() {
	}

	/** Categoría dentro de un artículo: solo lo necesario para mostrarla y enlazarla. */
	public record CategoryResponse(String name, String slug) {
	}

	/** Categoría en la lista de categorías del blog, con su descripción. */
	public record BlogCategoryResponse(String name, String slug, String description) {
	}

	public record TagResponse(String name, String slug) {
	}

	/** Lo que muestra la lista de artículos: sin el contenido completo. */
	public record PostSummaryResponse(
			String slug,
			String title,
			String excerpt,
			String coverImageUrl,
			CategoryResponse category,
			List<TagResponse> tags,
			int readingTimeMinutes,
			boolean featured,
			Instant publishedAt) {
	}

	/** Artículo completo, con el contenido en Markdown y los datos para SEO ya resueltos. */
	public record PostDetailResponse(
			String slug,
			String title,
			String excerpt,
			String content,
			String coverImageUrl,
			CategoryResponse category,
			List<TagResponse> tags,
			int readingTimeMinutes,
			boolean featured,
			String seoTitle,
			String seoDescription,
			Instant publishedAt,
			Instant updatedAt) {
	}
}
