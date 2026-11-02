package dev.isaacguzman.portfolio.project.infrastructure.in.web.dto;

import dev.isaacguzman.portfolio.project.domain.ProjectType;
import java.time.Instant;
import java.util.List;

/** Detalle de un proyecto, con el contenido en Markdown y los enlaces. */
public record ProjectDetailResponse(
		String slug,
		String title,
		ProjectType type,
		String summary,
		String content,
		String coverImageUrl,
		List<String> technologies,
		String liveUrl,
		String repoUrl,
		boolean featured,
		Instant publishedAt) {
}
