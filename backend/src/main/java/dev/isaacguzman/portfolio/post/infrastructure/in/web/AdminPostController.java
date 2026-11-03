package dev.isaacguzman.portfolio.post.infrastructure.in.web;

import dev.isaacguzman.portfolio.post.application.port.in.ManagePostsUseCase;
import dev.isaacguzman.portfolio.post.domain.Post;
import dev.isaacguzman.portfolio.post.infrastructure.in.web.dto.AdminResponses.PostAdminResponse;
import dev.isaacguzman.portfolio.post.infrastructure.in.web.dto.PostRequest;
import dev.isaacguzman.portfolio.post.infrastructure.in.web.dto.PublishRequest;
import dev.isaacguzman.portfolio.shared.application.PageQuery;
import dev.isaacguzman.portfolio.shared.infrastructure.web.PageResponse;
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
	PageResponse<PostAdminResponse> list(
			@RequestParam(defaultValue = "0") @Min(value = 0, message = "La página no puede ser negativa") int page,
			@RequestParam(defaultValue = "20")
			@Min(value = 1, message = "El tamaño de página mínimo es 1")
			@Max(value = PageQuery.MAX_SIZE, message = "El tamaño de página máximo es 50") int size) {
		return PageResponse.from(useCase.getAll(new PageQuery(page, size)).map(this::toResponse));
	}

	@GetMapping("/{id}")
	PostAdminResponse get(@PathVariable Long id) {
		return toResponse(useCase.getById(id));
	}

	/** Responde 201 con la cabecera Location apuntando al artículo creado. Siempre nace como borrador. */
	@PostMapping
	ResponseEntity<PostAdminResponse> create(@Valid @RequestBody PostRequest request) {
		Post created = useCase.create(mapper.toCommand(request));
		return ResponseEntity.created(URI.create("/api/admin/posts/" + created.id())).body(toResponse(created));
	}

	@PutMapping("/{id}")
	PostAdminResponse update(@PathVariable Long id, @Valid @RequestBody PostRequest request) {
		return toResponse(useCase.update(id, mapper.toCommand(request)));
	}

	/** Sin cuerpo publica ya; con {@code {"publishAt": "..."}} programa la publicación. */
	@PostMapping("/{id}/publish")
	PostAdminResponse publish(@PathVariable Long id, @RequestBody(required = false) PublishRequest request) {
		return toResponse(useCase.publish(id, request == null ? null : request.publishAt()));
	}

	@PostMapping("/{id}/unpublish")
	PostAdminResponse unpublish(@PathVariable Long id) {
		return toResponse(useCase.unpublish(id));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void delete(@PathVariable Long id) {
		useCase.delete(id);
	}

	private PostAdminResponse toResponse(Post post) {
		return mapper.toAdmin(post, post.isScheduledAt(clock.instant()));
	}
}
