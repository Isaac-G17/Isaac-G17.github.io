package dev.isaacguzman.portfolio.post.infrastructure.in.web.dto;

import java.time.Instant;
import java.util.List;

/** Respuestas JSON del blog público. */
public final class PostResponses {

	private PostResponses() {
	}

	public record CategoryResponse(String name, String slug) {
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
