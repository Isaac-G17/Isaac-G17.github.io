package dev.isaacguzman.portfolio.post.infrastructure.in.web;

import dev.isaacguzman.portfolio.post.application.port.in.GetPublishedPostsQuery;
import dev.isaacguzman.portfolio.post.domain.PostSearch;
import dev.isaacguzman.portfolio.post.infrastructure.in.web.dto.PostResponses.PostDetailResponse;
import dev.isaacguzman.portfolio.post.infrastructure.in.web.dto.PostResponses.PostSummaryResponse;
import dev.isaacguzman.portfolio.shared.application.PageQuery;
import dev.isaacguzman.portfolio.shared.infrastructure.web.PageResponse;
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
	PageResponse<PostSummaryResponse> list(
			@RequestParam(required = false) String category,
			@RequestParam(required = false) String tag,
			@RequestParam(required = false) String q,
			@RequestParam(defaultValue = "0") @Min(value = 0, message = "La página no puede ser negativa") int page,
			@RequestParam(defaultValue = "10")
			@Min(value = 1, message = "El tamaño de página mínimo es 1")
			@Max(value = PageQuery.MAX_SIZE, message = "El tamaño de página máximo es 50") int size) {
		var result = query.getPublishedPosts(new PostSearch(category, tag, q), new PageQuery(page, size));
		return PageResponse.from(result.map(mapper::toSummary));
	}

	@GetMapping("/{slug}")
	PostDetailResponse detail(@PathVariable String slug) {
		return mapper.toDetail(query.getPublishedPost(slug));
	}
}
