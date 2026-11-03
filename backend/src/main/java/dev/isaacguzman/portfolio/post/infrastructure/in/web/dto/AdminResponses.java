package dev.isaacguzman.portfolio.post.infrastructure.in.web.dto;

import dev.isaacguzman.portfolio.post.domain.PostStatus;
import java.time.Instant;
import java.util.List;

/** Respuestas del panel del blog: incluyen ids, estado y fechas de control. */
public final class AdminResponses {

	private AdminResponses() {
	}

	public record CategoryAdminResponse(Long id, String name, String slug, String description) {
	}

	public record TagAdminResponse(Long id, String name, String slug) {
	}

	/**
	 * @param scheduled true si está publicado con una fecha futura: todavía no se ve en el sitio
	 */
	public record PostAdminResponse(
			Long id,
			String slug,
			String title,
			String excerpt,
			String content,
			String coverImageUrl,
			CategoryAdminResponse category,
			List<TagAdminResponse> tags,
			int readingTimeMinutes,
			PostStatus status,
			boolean scheduled,
			boolean featured,
			String seoTitle,
			String seoDescription,
			Instant publishedAt,
			Instant createdAt,
			Instant updatedAt) {
	}
}
