package dev.isaacguzman.portfolio.project.infrastructure.in.web;

import dev.isaacguzman.portfolio.project.application.port.in.ManageProjectsUseCase;
import dev.isaacguzman.portfolio.project.domain.Project;
import dev.isaacguzman.portfolio.project.infrastructure.in.web.dto.ProjectAdminResponse;
import dev.isaacguzman.portfolio.project.infrastructure.in.web.dto.ProjectRequest;
import dev.isaacguzman.portfolio.shared.infrastructure.web.openapi.ApiTags;
import dev.isaacguzman.portfolio.shared.infrastructure.web.openapi.BadRequestResponse;
import dev.isaacguzman.portfolio.shared.infrastructure.web.openapi.ConflictResponse;
import dev.isaacguzman.portfolio.shared.infrastructure.web.openapi.NotFoundResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = ApiTags.PROJECTS_ADMIN)
class AdminProjectController {

	private final ManageProjectsUseCase useCase;
	private final ProjectWebMapper mapper;

	AdminProjectController(ManageProjectsUseCase useCase, ProjectWebMapper mapper) {
		this.useCase = useCase;
		this.mapper = mapper;
	}

	@GetMapping
	@Operation(summary = "Lista todos los proyectos", description = "Incluye borradores. Del editado más recientemente al más antiguo.")
	List<ProjectAdminResponse> list() {
		return useCase.getAll().stream().map(mapper::toAdmin).toList();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Obtiene un proyecto por id")
	@ApiResponse(responseCode = "200", description = "El proyecto")
	@NotFoundResponse
	ProjectAdminResponse get(@Parameter(description = "Id del proyecto") @PathVariable Long id) {
		return mapper.toAdmin(useCase.getById(id));
	}

	/** Responde 201 con la cabecera Location apuntando al proyecto creado. */
	@PostMapping
	@Operation(summary = "Crea un proyecto", description = "Siempre se crea como borrador. Las tecnologías se reutilizan sin importar mayúsculas.")
	@ApiResponse(responseCode = "201", description = "Creado; la cabecera Location apunta al nuevo proyecto")
	@BadRequestResponse
	@ConflictResponse
	ResponseEntity<ProjectAdminResponse> create(@Valid @RequestBody ProjectRequest request) {
		Project created = useCase.create(mapper.toDetails(request));
		return ResponseEntity.created(URI.create("/api/admin/projects/" + created.id()))
				.body(mapper.toAdmin(created));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Edita un proyecto", description = "No cambia el estado ni la fecha de publicación.")
	@ApiResponse(responseCode = "200", description = "El proyecto editado")
	@BadRequestResponse
	@NotFoundResponse
	@ConflictResponse
	ProjectAdminResponse update(@Parameter(description = "Id del proyecto") @PathVariable Long id,
			@Valid @RequestBody ProjectRequest request) {
		return mapper.toAdmin(useCase.update(id, mapper.toDetails(request)));
	}

	@PostMapping("/{id}/publish")
	@Operation(summary = "Publica un proyecto", description = "Si ya estaba publicado, conserva la fecha original.")
	@ApiResponse(responseCode = "200", description = "El proyecto publicado")
	@NotFoundResponse
	ProjectAdminResponse publish(@Parameter(description = "Id del proyecto") @PathVariable Long id) {
		return mapper.toAdmin(useCase.publish(id));
	}

	@PostMapping("/{id}/unpublish")
	@Operation(summary = "Despublica un proyecto", description = "Vuelve a borrador y quita la fecha de publicación.")
	@ApiResponse(responseCode = "200", description = "El proyecto como borrador")
	@NotFoundResponse
	ProjectAdminResponse unpublish(@Parameter(description = "Id del proyecto") @PathVariable Long id) {
		return mapper.toAdmin(useCase.unpublish(id));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Borra un proyecto")
	@ApiResponse(responseCode = "204", description = "Borrado")
	@NotFoundResponse
	void delete(@Parameter(description = "Id del proyecto") @PathVariable Long id) {
		useCase.delete(id);
	}
}
