package dev.isaacguzman.portfolio.project.infrastructure.in.web;

import dev.isaacguzman.portfolio.project.application.port.in.GetPublishedProjectsQuery;
import dev.isaacguzman.portfolio.project.domain.ProjectType;
import dev.isaacguzman.portfolio.project.infrastructure.in.web.dto.ProjectDetailResponse;
import dev.isaacguzman.portfolio.project.infrastructure.in.web.dto.ProjectSummaryResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** API pública de proyectos: abierta y solo con contenido publicado. */
@RestController
@RequestMapping("/api/public/projects")
class PublicProjectController {

	private final GetPublishedProjectsQuery query;
	private final ProjectWebMapper mapper;

	PublicProjectController(GetPublishedProjectsQuery query, ProjectWebMapper mapper) {
		this.query = query;
		this.mapper = mapper;
	}

	/** {@code ?type=WORDPRESS} o {@code ?type=CODE} filtran; sin el parámetro devuelve todos. */
	@GetMapping
	List<ProjectSummaryResponse> list(@RequestParam(required = false) ProjectType type) {
		return query.getPublishedProjects(type).stream().map(mapper::toSummary).toList();
	}

	@GetMapping("/{slug}")
	ProjectDetailResponse detail(@PathVariable String slug) {
		return mapper.toDetail(query.getPublishedProject(slug));
	}
}
