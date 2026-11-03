package dev.isaacguzman.portfolio.post.infrastructure.out.persistence;

import dev.isaacguzman.portfolio.post.application.port.out.CategoryRepositoryPort;
import dev.isaacguzman.portfolio.post.domain.Category;
import dev.isaacguzman.portfolio.shared.domain.Slug;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
class CategoryPersistenceAdapter implements CategoryRepositoryPort {

	private final SpringDataCategoryRepository repository;
	private final SpringDataPostRepository posts;
	private final PostPersistenceMapper mapper;

	CategoryPersistenceAdapter(SpringDataCategoryRepository repository, SpringDataPostRepository posts,
			PostPersistenceMapper mapper) {
		this.repository = repository;
		this.posts = posts;
		this.mapper = mapper;
	}

	@Override
	public List<Category> findAll() {
		return repository.findAllByOrderByNameAsc().stream().map(mapper::toDomain).toList();
	}

	@Override
	public Optional<Category> findById(Long id) {
		return repository.findById(id).map(mapper::toDomain);
	}

	@Override
	public boolean existsByName(String name, Long exceptId) {
		return exceptId == null
				? repository.existsByNameIgnoreCase(name)
				: repository.existsByNameIgnoreCaseAndIdNot(name, exceptId);
	}

	@Override
	public boolean existsBySlug(Slug slug, Long exceptId) {
		return exceptId == null
				? repository.existsBySlug(slug.value())
				: repository.existsBySlugAndIdNot(slug.value(), exceptId);
	}

	@Override
	public boolean hasPosts(Long id) {
		return posts.existsByCategoryId(id);
	}

	@Override
	public Category save(Category category) {
		CategoryJpaEntity entity = category.id() == null
				? new CategoryJpaEntity()
				: repository.findById(category.id()).orElseThrow();
		mapper.copyToEntity(category, entity);
		return mapper.toDomain(repository.save(entity));
	}

	@Override
	public void deleteById(Long id) {
		repository.deleteById(id);
	}
}
