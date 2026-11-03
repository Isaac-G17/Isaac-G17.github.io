package dev.isaacguzman.portfolio.post.infrastructure.in.web;

import dev.isaacguzman.portfolio.post.application.port.in.ManagePostsUseCase;
import dev.isaacguzman.portfolio.post.domain.Post;
import dev.isaacguzman.portfolio.post.infrastructure.in.web.dto.AdminResponses.PostAdminResponse;
import dev.isaacguzman.portfolio.post.infrastructure.in.web.dto.PostRequest;
import dev.isaacguzman.portfolio.post.infrastructure.in.web.dto.PublishRequest;
import dev.isaacguzman.portfolio.shared.application.PageQuery;
import dev.isaacguzman.portfolio.shared.infrastructure.web.PageResponse;
import dev.isaacguzman.portfolio.shared.infrastructure.web.openapi.ApiTags;
import dev.isaacguzman.portfolio.shared.infrastructure.web.openapi.BadRequestResponse;
import dev.isaacguzman.portfolio.shared.infrastructure.web.openapi.ConflictResponse;
import dev.isaacguzman.portfolio.shared.infrastructure.web.openapi.NotFoundResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.net.URI;
import java.time.Clock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * API de administración de artículos: ve y modifica artículos en cualquier estado.
 * Pendiente: protegerla con autenticación (HU 4.2) antes de llevarla a producción.
 */
@RestController
@RequestMapping("/api/admin/posts")
@Tag(name = ApiTags.BLOG_ADMIN)
class AdminPostController {

	private final ManagePostsUseCase useCase;
	private final PostAdminWebMapper mapper;
	private final Clock clock;

	AdminPostController(ManagePostsUseCase useCase, PostAdminWebMapper mapper, Clock clock) {
		this.useCase = useCase;
		this.mapper = mapper;
		this.clock = clock;
	}

	@GetMapping
	@Operation(summary = "Lista todos los artículos",
			description = "Incluye borradores y programados (scheduled = true). Del editado más recientemente al más antiguo.")
	@ApiResponse(responseCode = "200", description = "Una página de artículos")
	@BadRequestResponse
	PageResponse<PostAdminResponse> list(
			@Parameter(description = "Número de página, empieza en 0")
			@RequestParam(defaultValue = "0") @Min(value = 0, message = "La página no puede ser negativa") int page,
			@Parameter(description = "Artículos por página, entre 1 y 50")
			@RequestParam(defaultValue = "20")
			@Min(value = 1, message = "El tamaño de página mínimo es 1")
			@Max(value = PageQuery.MAX_SIZE, message = "El tamaño de página máximo es 50") int size) {
		return PageResponse.from(useCase.getAll(new PageQuery(page, size)).map(this::toResponse));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Obtiene un artículo por id")
	@ApiResponse(responseCode = "200", description = "El artículo")
	@NotFoundResponse
	PostAdminResponse get(@Parameter(description = "Id del artículo") @PathVariable Long id) {
		return toResponse(useCase.getById(id));
	}

	/** Responde 201 con la cabecera Location apuntando al artículo creado. Siempre nace como borrador. */
	@PostMapping
	@Operation(summary = "Crea un artículo",
			description = "Siempre nace como borrador. La categoría va por id; las etiquetas por nombre (se crean las que no existen). "
					+ "El tiempo de lectura se calcula del contenido.")
	@ApiResponse(responseCode = "201", description = "Creado; la cabecera Location apunta al nuevo artículo")
	@BadRequestResponse
	@ConflictResponse
	ResponseEntity<PostAdminResponse> create(@Valid @RequestBody PostRequest request) {
		Post created = useCase.create(mapper.toCommand(request));
		return ResponseEntity.created(URI.create("/api/admin/posts/" + created.id())).body(toResponse(created));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Edita un artículo",
			description = "No cambia el estado ni la fecha de publicación. Recalcula el tiempo de lectura.")
	@ApiResponse(responseCode = "200", description = "El artículo editado")
	@BadRequestResponse
	@NotFoundResponse
	@ConflictResponse
	PostAdminResponse update(@Parameter(description = "Id del artículo") @PathVariable Long id,
			@Valid @RequestBody PostRequest request) {
		return toResponse(useCase.update(id, mapper.toCommand(request)));
	}

	/** Sin cuerpo publica ya; con {@code {"publishAt": "..."}} programa la publicación. */
	@PostMapping("/{id}/publish")
	@Operation(summary = "Publica o programa un artículo",
			description = "Sin cuerpo se publica ya. Con {\"publishAt\": \"2026-12-01T13:00:00Z\"} queda programado "
					+ "y no aparece en el sitio público hasta esa fecha.")
	@ApiResponse(responseCode = "200", description = "El artículo publicado o programado")
	@NotFoundResponse
	PostAdminResponse publish(@Parameter(description = "Id del artículo") @PathVariable Long id,
			@RequestBody(required = false) PublishRequest request) {
		return toResponse(useCase.publish(id, request == null ? null : request.publishAt()));
	}

	@PostMapping("/{id}/unpublish")
	@Operation(summary = "Despublica un artículo", description = "Vuelve a borrador; también cancela una publicación programada.")
	@ApiResponse(responseCode = "200", description = "El artículo como borrador")
	@NotFoundResponse
	PostAdminResponse unpublish(@Parameter(description = "Id del artículo") @PathVariable Long id) {
		return toResponse(useCase.unpublish(id));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Borra un artículo")
	@ApiResponse(responseCode = "204", description = "Borrado")
	@NotFoundResponse
	void delete(@Parameter(description = "Id del artículo") @PathVariable Long id) {
		useCase.delete(id);
	}

	private PostAdminResponse toResponse(Post post) {
		return mapper.toAdmin(post, post.isScheduledAt(clock.instant()));
	}
}
