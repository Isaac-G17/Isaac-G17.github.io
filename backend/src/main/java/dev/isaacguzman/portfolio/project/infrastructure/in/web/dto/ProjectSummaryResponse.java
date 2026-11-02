package dev.isaacguzman.portfolio.project.infrastructure.in.web.dto;

import dev.isaacguzman.portfolio.project.domain.ProjectType;
import java.time.Instant;
import java.util.List;

/** Lo que muestra la lista de proyectos: sin el contenido completo para que la respuesta sea liviana. */
public record ProjectSummaryResponse(
		String slug,
		String title,
		ProjectType type,
		String summary,
		String coverImageUrl,
		List<String> technologies,
		boolean featured,
		Instant publishedAt) {
}
