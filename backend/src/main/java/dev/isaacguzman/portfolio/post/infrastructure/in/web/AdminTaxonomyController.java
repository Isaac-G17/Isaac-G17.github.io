package dev.isaacguzman.portfolio.post.infrastructure.in.web;

import dev.isaacguzman.portfolio.post.application.port.in.ManageTaxonomyUseCase;
import dev.isaacguzman.portfolio.post.domain.Category;
import dev.isaacguzman.portfolio.post.domain.Tag;
import dev.isaacguzman.portfolio.post.infrastructure.in.web.dto.AdminResponses.CategoryAdminResponse;
import dev.isaacguzman.portfolio.post.infrastructure.in.web.dto.AdminResponses.TagAdminResponse;
import dev.isaacguzman.portfolio.post.infrastructure.in.web.dto.TaxonomyRequests.CategoryRequest;
import dev.isaacguzman.portfolio.post.infrastructure.in.web.dto.TaxonomyRequests.TagRequest;
import dev.isaacguzman.portfolio.shared.infrastructure.web.openapi.ApiTags;
import dev.isaacguzman.portfolio.shared.infrastructure.web.openapi.BadRequestResponse;
import dev.isaacguzman.portfolio.shared.infrastructure.web.openapi.ConflictResponse;
import dev.isaacguzman.portfolio.shared.infrastructure.web.openapi.NotFoundResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
 * API de administración de categorías y etiquetas.
 * Pendiente: protegerla con autenticación (HU 4.2) antes de llevarla a producción.
 */
@RestController
@RequestMapping("/api/admin")
// Nombre completo porque "Tag" ya es la etiqueta del blog (dominio) en este archivo.
@io.swagger.v3.oas.annotations.tags.Tag(name = ApiTags.BLOG_ADMIN)
class AdminTaxonomyController {

	private final ManageTaxonomyUseCase useCase;
	private final PostAdminWebMapper mapper;

	AdminTaxonomyController(ManageTaxonomyUseCase useCase, PostAdminWebMapper mapper) {
		this.useCase = useCase;
		this.mapper = mapper;
	}

	// --- Categorías ---

	@GetMapping("/categories")
	@Operation(summary = "Lista las categorías", description = "En orden alfabético, con su id.")
	List<CategoryAdminResponse> categories() {
		return useCase.getAllCategories().stream().map(mapper::toAdmin).toList();
	}

	@PostMapping("/categories")
	@Operation(summary = "Crea una categoría", description = "Si no se envía el slug, se genera del nombre.")
	@ApiResponse(responseCode = "201", description = "Creada; la cabecera Location apunta a la nueva categoría")
	@BadRequestResponse
	@ConflictResponse
	ResponseEntity<CategoryAdminResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
		Category created = useCase.createCategory(request.name(), mapper.toSlug(request.slug()), request.description());
		return ResponseEntity.created(URI.create("/api/admin/categories/" + created.id())).body(mapper.toAdmin(created));
	}

	@PutMapping("/categories/{id}")
	@Operation(summary = "Edita una categoría")
	@ApiResponse(responseCode = "200", description = "La categoría editada")
	@BadRequestResponse
	@NotFoundResponse
	@ConflictResponse
	CategoryAdminResponse updateCategory(@Parameter(description = "Id de la categoría") @PathVariable Long id,
			@Valid @RequestBody CategoryRequest request) {
		return mapper.toAdmin(
				useCase.updateCategory(id, request.name(), mapper.toSlug(request.slug()), request.description()));
	}

	/** 409 si algún artículo usa la categoría. */
	@DeleteMapping("/categories/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Borra una categoría", description = "No se puede borrar si algún artículo la usa (409).")
	@ApiResponse(responseCode = "204", description = "Borrada")
	@NotFoundResponse
	@ConflictResponse
	void deleteCategory(@Parameter(description = "Id de la categoría") @PathVariable Long id) {
		useCase.deleteCategory(id);
	}

	// --- Etiquetas ---

	@GetMapping("/tags")
	@Operation(summary = "Lista las etiquetas", description = "En orden alfabético, con su id.")
	List<TagAdminResponse> tags() {
		return useCase.getAllTags().stream().map(mapper::toAdmin).toList();
	}

	@PostMapping("/tags")
	@Operation(summary = "Crea una etiqueta", description = "El slug se genera del nombre.")
	@ApiResponse(responseCode = "201", description = "Creada; la cabecera Location apunta a la nueva etiqueta")
	@BadRequestResponse
	@ConflictResponse
	ResponseEntity<TagAdminResponse> createTag(@Valid @RequestBody TagRequest request) {
		Tag created = useCase.createTag(request.name());
		return ResponseEntity.created(URI.create("/api/admin/tags/" + created.id())).body(mapper.toAdmin(created));
	}

	@PutMapping("/tags/{id}")
	@Operation(summary = "Renombra una etiqueta", description = "El slug se vuelve a generar del nombre nuevo.")
	@ApiResponse(responseCode = "200", description = "La etiqueta renombrada")
	@BadRequestResponse
	@NotFoundResponse
	@ConflictResponse
	TagAdminResponse renameTag(@Parameter(description = "Id de la etiqueta") @PathVariable Long id,
			@Valid @RequestBody TagRequest request) {
		return mapper.toAdmin(useCase.renameTag(id, request.name()));
	}

	/** 409 si algún artículo usa la etiqueta. */
	@DeleteMapping("/tags/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Borra una etiqueta", description = "No se puede borrar si algún artículo la usa (409).")
	@ApiResponse(responseCode = "204", description = "Borrada")
	@NotFoundResponse
	@ConflictResponse
	void deleteTag(@Parameter(description = "Id de la etiqueta") @PathVariable Long id) {
		useCase.deleteTag(id);
	}
}
