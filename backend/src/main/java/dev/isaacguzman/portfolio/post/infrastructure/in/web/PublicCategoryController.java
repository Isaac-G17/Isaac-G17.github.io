package dev.isaacguzman.portfolio.post.infrastructure.in.web;

import dev.isaacguzman.portfolio.post.application.port.in.GetCategoriesQuery;
import dev.isaacguzman.portfolio.post.infrastructure.in.web.dto.PostResponses.BlogCategoryResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Categorías del blog. Sus slugs sirven para el filtro {@code /api/public/posts?category=}. */
@RestController
@RequestMapping("/api/public/categories")
class PublicCategoryController {

	private final GetCategoriesQuery query;
	private final PostWebMapper mapper;

	PublicCategoryController(GetCategoriesQuery query, PostWebMapper mapper) {
		this.query = query;
		this.mapper = mapper;
	}

	@GetMapping
	List<BlogCategoryResponse> list() {
		return query.getCategories().stream().map(mapper::toBlogCategory).toList();
	}
}
