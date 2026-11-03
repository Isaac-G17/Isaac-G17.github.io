package dev.isaacguzman.portfolio.post.infrastructure.in.web;

import dev.isaacguzman.portfolio.post.application.port.in.ManageTaxonomyUseCase;
import dev.isaacguzman.portfolio.post.domain.Category;
import dev.isaacguzman.portfolio.post.domain.Tag;
import dev.isaacguzman.portfolio.post.infrastructure.in.web.dto.AdminResponses.CategoryAdminResponse;
import dev.isaacguzman.portfolio.post.infrastructure.in.web.dto.AdminResponses.TagAdminResponse;
import dev.isaacguzman.portfolio.post.infrastructure.in.web.dto.TaxonomyRequests.CategoryRequest;
import dev.isaacguzman.portfolio.post.infrastructure.in.web.dto.TaxonomyRequests.TagRequest;
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
class AdminTaxonomyController {

	private final ManageTaxonomyUseCase useCase;
	private final PostAdminWebMapper mapper;

	AdminTaxonomyController(ManageTaxonomyUseCase useCase, PostAdminWebMapper mapper) {
		this.useCase = useCase;
		this.mapper = mapper;
	}

	// --- Categorías ---

	@GetMapping("/categories")
	List<CategoryAdminResponse> categories() {
		return useCase.getAllCategories().stream().map(mapper::toAdmin).toList();
	}

	@PostMapping("/categories")
	ResponseEntity<CategoryAdminResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
		Category created = useCase.createCategory(request.name(), mapper.toSlug(request.slug()), request.description());
		return ResponseEntity.created(URI.create("/api/admin/categories/" + created.id())).body(mapper.toAdmin(created));
	}

	@PutMapping("/categories/{id}")
	CategoryAdminResponse updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
		return mapper.toAdmin(
				useCase.updateCategory(id, request.name(), mapper.toSlug(request.slug()), request.description()));
	}

	/** 409 si algún artículo usa la categoría. */
	@DeleteMapping("/categories/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void deleteCategory(@PathVariable Long id) {
		useCase.deleteCategory(id);
	}

	// --- Etiquetas ---

	@GetMapping("/tags")
	List<TagAdminResponse> tags() {
		return useCase.getAllTags().stream().map(mapper::toAdmin).toList();
	}

	@PostMapping("/tags")
	ResponseEntity<TagAdminResponse> createTag(@Valid @RequestBody TagRequest request) {
		Tag created = useCase.createTag(request.name());
		return ResponseEntity.created(URI.create("/api/admin/tags/" + created.id())).body(mapper.toAdmin(created));
	}

	@PutMapping("/tags/{id}")
	TagAdminResponse renameTag(@PathVariable Long id, @Valid @RequestBody TagRequest request) {
		return mapper.toAdmin(useCase.renameTag(id, request.name()));
	}

	/** 409 si algún artículo usa la etiqueta. */
	@DeleteMapping("/tags/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void deleteTag(@PathVariable Long id) {
		useCase.deleteTag(id);
	}
}
