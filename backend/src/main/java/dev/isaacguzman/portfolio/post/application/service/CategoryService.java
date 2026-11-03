package dev.isaacguzman.portfolio.post.application.service;

import dev.isaacguzman.portfolio.post.application.port.in.GetCategoriesQuery;
import dev.isaacguzman.portfolio.post.application.port.out.CategoryRepositoryPort;
import dev.isaacguzman.portfolio.post.domain.Category;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CategoryService implements GetCategoriesQuery {

	private final CategoryRepositoryPort categories;

	public CategoryService(CategoryRepositoryPort categories) {
		this.categories = categories;
	}

	@Override
	public List<Category> getCategories() {
		return categories.findAll();
	}
}
