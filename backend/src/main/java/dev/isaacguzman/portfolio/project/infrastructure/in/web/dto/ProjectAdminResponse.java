package dev.isaacguzman.portfolio.project.infrastructure.in.web.dto;

import dev.isaacguzman.portfolio.project.domain.ProjectStatus;
import dev.isaacguzman.portfolio.project.domain.ProjectType;
import java.time.Instant;
import java.util.List;

/** Proyecto completo para el panel: incluye el id, el estado y las fechas de control. */
public record ProjectAdminResponse(
		Long id,
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
		ProjectStatus status,
		Instant publishedAt,
		Instant createdAt,
		Instant updatedAt) {
}
