package dev.isaacguzman.portfolio.post.infrastructure.in.web;

import dev.isaacguzman.portfolio.post.application.port.in.GetCategoriesQuery;
import dev.isaacguzman.portfolio.post.infrastructure.in.web.dto.PostResponses.BlogCategoryResponse;
import dev.isaacguzman.portfolio.shared.infrastructure.web.openapi.ApiTags;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Categorías del blog. Sus slugs sirven para el filtro {@code /api/public/posts?category=}. */
@RestController
@RequestMapping("/api/public/categories")
@Tag(name = ApiTags.BLOG_PUBLIC)
class PublicCategoryController {

	private final GetCategoriesQuery query;
	private final PostWebMapper mapper;

	PublicCategoryController(GetCategoriesQuery query, PostWebMapper mapper) {
		this.query = query;
		this.mapper = mapper;
	}

	@GetMapping
	@Operation(summary = "Lista las categorías del blog", description = "En orden alfabético. Sus slugs sirven para filtrar artículos.")
	List<BlogCategoryResponse> list() {
		return query.getCategories().stream().map(mapper::toBlogCategory).toList();
	}
}
