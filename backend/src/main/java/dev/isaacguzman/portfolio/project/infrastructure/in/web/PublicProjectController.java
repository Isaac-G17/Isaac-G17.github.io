package dev.isaacguzman.portfolio.project.infrastructure.in.web;

import dev.isaacguzman.portfolio.project.application.port.in.GetPublishedProjectsQuery;
import dev.isaacguzman.portfolio.project.domain.ProjectType;
import dev.isaacguzman.portfolio.project.infrastructure.in.web.dto.ProjectDetailResponse;
import dev.isaacguzman.portfolio.project.infrastructure.in.web.dto.ProjectSummaryResponse;
import dev.isaacguzman.portfolio.shared.infrastructure.web.openapi.ApiTags;
import dev.isaacguzman.portfolio.shared.infrastructure.web.openapi.BadRequestResponse;
import dev.isaacguzman.portfolio.shared.infrastructure.web.openapi.NotFoundResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** API pública de proyectos: abierta y solo con contenido publicado. */
@RestController
@RequestMapping("/api/public/projects")
@Tag(name = ApiTags.PROJECTS_PUBLIC)
class PublicProjectController {

	private final GetPublishedProjectsQuery query;
	private final ProjectWebMapper mapper;

	PublicProjectController(GetPublishedProjectsQuery query, ProjectWebMapper mapper) {
		this.query = query;
		this.mapper = mapper;
	}

	/** {@code ?type=WORDPRESS} o {@code ?type=CODE} filtran; sin el parámetro devuelve todos. */
	@GetMapping
	@Operation(summary = "Lista los proyectos publicados", description = "Del más reciente al más antiguo. Sin el contenido completo.")
	@ApiResponse(responseCode = "200", description = "Los proyectos publicados")
	@BadRequestResponse
	List<ProjectSummaryResponse> list(
			@Parameter(description = "Filtra por tipo; sin el parámetro devuelve todos")
			@RequestParam(required = false) ProjectType type) {
		return query.getPublishedProjects(type).stream().map(mapper::toSummary).toList();
	}

	@GetMapping("/{slug}")
	@Operation(summary = "Obtiene un proyecto publicado", description = "Incluye el contenido en Markdown y los enlaces.")
	@ApiResponse(responseCode = "200", description = "El proyecto")
	@NotFoundResponse
	ProjectDetailResponse detail(@Parameter(description = "Slug del proyecto", example = "portafolio") @PathVariable String slug) {
		return mapper.toDetail(query.getPublishedProject(slug));
	}
}
