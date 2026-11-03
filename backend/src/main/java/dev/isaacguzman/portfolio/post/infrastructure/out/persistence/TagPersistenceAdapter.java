package dev.isaacguzman.portfolio.post.infrastructure.out.persistence;

import dev.isaacguzman.portfolio.post.application.port.out.TagRepositoryPort;
import dev.isaacguzman.portfolio.post.domain.Tag;
import dev.isaacguzman.portfolio.shared.domain.Slug;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
class TagPersistenceAdapter implements TagRepositoryPort {

	private final SpringDataTagRepository repository;
	private final SpringDataPostRepository posts;
	private final PostPersistenceMapper mapper;

	TagPersistenceAdapter(SpringDataTagRepository repository, SpringDataPostRepository posts,
			PostPersistenceMapper mapper) {
		this.repository = repository;
		this.posts = posts;
		this.mapper = mapper;
	}

	@Override
	public List<Tag> findAll() {
		return repository.findAllByOrderByNameAsc().stream().map(mapper::toDomain).toList();
	}

	@Override
	public Optional<Tag> findById(Long id) {
		return repository.findById(id).map(mapper::toDomain);
	}

	@Override
	public List<Tag> findBySlugs(Collection<Slug> slugs) {
		if (slugs.isEmpty()) {
			return List.of();
		}
		return repository.findBySlugIn(slugs.stream().map(Slug::value).toList()).stream()
				.map(mapper::toDomain)
				.toList();
	}

	@Override
	public boolean existsBySlug(Slug slug, Long exceptId) {
		return exceptId == null
				? repository.existsBySlug(slug.value())
				: repository.existsBySlugAndIdNot(slug.value(), exceptId);
	}

	@Override
	public boolean hasPosts(Long id) {
		return posts.existsByTagsId(id);
	}

	@Override
	public Tag save(Tag tag) {
		TagJpaEntity entity = tag.id() == null ? new TagJpaEntity() : repository.findById(tag.id()).orElseThrow();
		mapper.copyToEntity(tag, entity);
		return mapper.toDomain(repository.save(entity));
	}

	@Override
	public void deleteById(Long id) {
		repository.deleteById(id);
	}
}
