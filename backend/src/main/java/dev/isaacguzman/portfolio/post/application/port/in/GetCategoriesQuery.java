package dev.isaacguzman.portfolio.post.application.port.in;

import dev.isaacguzman.portfolio.post.domain.Category;
import java.util.List;

/** Categorías del blog, para el menú y los filtros del sitio público. */
public interface GetCategoriesQuery {

	/** En orden alfabético por nombre. */
	List<Category> getCategories();
}
