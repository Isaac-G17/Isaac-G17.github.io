package dev.isaacguzman.portfolio.post.infrastructure.out.persistence;

import dev.isaacguzman.portfolio.post.application.port.out.CategoryRepositoryPort;
import dev.isaacguzman.portfolio.post.domain.Category;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
class CategoryPersistenceAdapter implements CategoryRepositoryPort {

	private final SpringDataCategoryRepository repository;
	private final PostPersistenceMapper mapper;

	CategoryPersistenceAdapter(SpringDataCategoryRepository repository, PostPersistenceMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	@Override
	public List<Category> findAll() {
		return repository.findAllByOrderByNameAsc().stream().map(mapper::toDomain).toList();
	}
}
