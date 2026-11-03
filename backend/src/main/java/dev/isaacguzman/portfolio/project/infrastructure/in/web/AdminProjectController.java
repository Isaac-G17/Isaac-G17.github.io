package dev.isaacguzman.portfolio.project.infrastructure.in.web;

import dev.isaacguzman.portfolio.project.application.port.in.ManageProjectsUseCase;
import dev.isaacguzman.portfolio.project.domain.Project;
import dev.isaacguzman.portfolio.project.infrastructure.in.web.dto.ProjectAdminResponse;
import dev.isaacguzman.portfolio.project.infrastructure.in.web.dto.ProjectRequest;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * API de administración de proyectos: ve y modifica proyectos en cualquier estado.
 * Pendiente: protegerla con autenticación (HU 4.2) antes de llevarla a producción.
 */
@RestController
@RequestMapping("/api/admin/projects")
class AdminProjectController {

	private final ManageProjectsUseCase useCase;
	private final ProjectWebMapper mapper;

	AdminProjectController(ManageProjectsUseCase useCase, ProjectWebMapper mapper) {
		this.useCase = useCase;
		this.mapper = mapper;
	}

	@GetMapping
	List<ProjectAdminResponse> list() {
		return useCase.getAll().stream().map(mapper::toAdmin).toList();
	}

	@GetMapping("/{id}")
	ProjectAdminResponse get(@PathVariable Long id) {
		return mapper.toAdmin(useCase.getById(id));
	}

	/** Responde 201 con la cabecera Location apuntando al proyecto creado. */
	@PostMapping
	ResponseEntity<ProjectAdminResponse> create(@Valid @RequestBody ProjectRequest request) {
		Project created = useCase.create(mapper.toDetails(request));
		return ResponseEntity.created(URI.create("/api/admin/projects/" + created.id()))
				.body(mapper.toAdmin(created));
	}

	@PutMapping("/{id}")
	ProjectAdminResponse update(@PathVariable Long id, @Valid @RequestBody ProjectRequest request) {
		return mapper.toAdmin(useCase.update(id, mapper.toDetails(request)));
	}

	@PostMapping("/{id}/publish")
	ProjectAdminResponse publish(@PathVariable Long id) {
		return mapper.toAdmin(useCase.publish(id));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void delete(@PathVariable Long id) {
		useCase.delete(id);
	}
}
