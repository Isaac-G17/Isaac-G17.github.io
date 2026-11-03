package dev.isaacguzman.portfolio.post.infrastructure.in.web;

import dev.isaacguzman.portfolio.post.application.port.in.GetPublishedPostsQuery;
import dev.isaacguzman.portfolio.post.domain.PostSearch;
import dev.isaacguzman.portfolio.post.infrastructure.in.web.dto.PostResponses.PostDetailResponse;
import dev.isaacguzman.portfolio.post.infrastructure.in.web.dto.PostResponses.PostSummaryResponse;
import dev.isaacguzman.portfolio.shared.application.PageQuery;
import dev.isaacguzman.portfolio.shared.infrastructure.web.PageResponse;
import dev.isaacguzman.portfolio.shared.infrastructure.web.openapi.ApiTags;
import dev.isaacguzman.portfolio.shared.infrastructure.web.openapi.BadRequestResponse;
import dev.isaacguzman.portfolio.shared.infrastructure.web.openapi.NotFoundResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** API pública del blog: solo artículos publicados cuya fecha ya llegó. */
@RestController
@RequestMapping("/api/public/posts")
@Tag(name = ApiTags.BLOG_PUBLIC)
class PublicPostController {

	private final GetPublishedPostsQuery query;
	private final PostWebMapper mapper;

	PublicPostController(GetPublishedPostsQuery query, PostWebMapper mapper) {
		this.query = query;
		this.mapper = mapper;
	}

	/**
	 * Ejemplo: {@code ?category=backend&tag=java&q=spring&page=0&size=10}.
	 * Todos los parámetros son opcionales.
	 */
	@GetMapping
	@Operation(summary = "Lista los artículos publicados",
			description = "Paginado y del más reciente al más antiguo. Los artículos programados no aparecen hasta su fecha.")
	@ApiResponse(responseCode = "200", description = "Una página de artículos")
	@BadRequestResponse
	PageResponse<PostSummaryResponse> list(
			@Parameter(description = "Slug de la categoría", example = "backend")
			@RequestParam(required = false) String category,
			@Parameter(description = "Slug de la etiqueta", example = "java")
			@RequestParam(required = false) String tag,
			@Parameter(description = "Texto a buscar en el título, sin importar mayúsculas")
			@RequestParam(required = false) String q,
			@Parameter(description = "Número de página, empieza en 0")
			@RequestParam(defaultValue = "0") @Min(value = 0, message = "La página no puede ser negativa") int page,
			@Parameter(description = "Artículos por página, entre 1 y 50")
			@RequestParam(defaultValue = "10")
			@Min(value = 1, message = "El tamaño de página mínimo es 1")
			@Max(value = PageQuery.MAX_SIZE, message = "El tamaño de página máximo es 50") int size) {
		var result = query.getPublishedPosts(new PostSearch(category, tag, q), new PageQuery(page, size));
		return PageResponse.from(result.map(mapper::toSummary));
	}

	@GetMapping("/{slug}")
	@Operation(summary = "Obtiene un artículo publicado",
			description = "Incluye el contenido en Markdown y el SEO resuelto (si no hay uno propio, se usan el título y el extracto).")
	@ApiResponse(responseCode = "200", description = "El artículo")
	@NotFoundResponse
	PostDetailResponse detail(@Parameter(description = "Slug del artículo") @PathVariable String slug) {
		return mapper.toDetail(query.getPublishedPost(slug));
	}
}
