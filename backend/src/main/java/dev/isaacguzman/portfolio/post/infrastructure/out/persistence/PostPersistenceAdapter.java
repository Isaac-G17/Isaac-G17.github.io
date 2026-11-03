package dev.isaacguzman.portfolio.post.infrastructure.out.persistence;

import dev.isaacguzman.portfolio.post.application.port.out.PostRepositoryPort;
import dev.isaacguzman.portfolio.post.domain.Post;
import dev.isaacguzman.portfolio.post.domain.PostSearch;
import dev.isaacguzman.portfolio.post.domain.Tag;
import dev.isaacguzman.portfolio.shared.application.PageQuery;
import dev.isaacguzman.portfolio.shared.application.PageResult;
import dev.isaacguzman.portfolio.shared.domain.Slug;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/** Implementa el puerto de salida con Spring Data JPA y PostgreSQL. */
@Component
class PostPersistenceAdapter implements PostRepositoryPort {

	// El id desempata artículos con la misma fecha, para que el orden no cambie entre páginas.
	private static final Sort NEWEST_FIRST = Sort.by(Sort.Order.desc("publishedAt"), Sort.Order.desc("id"));
	private static final Sort LAST_EDITED_FIRST = Sort.by(Sort.Order.desc("updatedAt"), Sort.Order.desc("id"));

	private final SpringDataPostRepository repository;
	private final SpringDataCategoryRepository categories;
	private final SpringDataTagRepository tags;
	private final PostPersistenceMapper mapper;

	PostPersistenceAdapter(SpringDataPostRepository repository, SpringDataCategoryRepository categories,
			SpringDataTagRepository tags, PostPersistenceMapper mapper) {
		this.repository = repository;
		this.categories = categories;
		this.tags = tags;
		this.mapper = mapper;
	}

	@Override
	public PageResult<Post> findPublished(PostSearch search, Instant now, PageQuery page) {
		Page<PostJpaEntity> result = repository.findAll(
				PostSpecifications.publishedMatching(search, now),
				PageRequest.of(page.page(), page.size(), NEWEST_FIRST));
		return toPageResult(result, page);
	}

	@Override
	public Optional<Post> findPublishedBySlug(Slug slug, Instant now) {
		Specification<PostJpaEntity> bySlug = (root, query, cb) -> cb.equal(root.get("slug"), slug.value());
		return repository.findOne(PostSpecifications.visibleAt(now).and(bySlug)).map(mapper::toDomain);
	}

	@Override
	public PageResult<Post> findAll(PageQuery page) {
		return toPageResult(repository.findAllBy(PageRequest.of(page.page(), page.size(), LAST_EDITED_FIRST)), page);
	}

	@Override
	public Optional<Post> findById(Long id) {
		return repository.findById(id).map(mapper::toDomain);
	}

	@Override
	public boolean existsBySlug(Slug slug, Long exceptId) {
		return exceptId == null
				? repository.existsBySlug(slug.value())
				: repository.existsBySlugAndIdNot(slug.value(), exceptId);
	}

	@Override
	public Post save(Post post) {
		PostJpaEntity entity = post.id() == null ? new PostJpaEntity() : repository.findById(post.id()).orElseThrow();
		mapper.copyToEntity(post, entity);
		// getReferenceById no consulta la base de datos: solo usa el id para la clave foránea.
		entity.setCategory(categories.getReferenceById(post.category().id()));
		entity.setTags(post.tags().stream()
				.map(Tag::id)
				.map(tags::getReferenceById)
				.collect(Collectors.toCollection(HashSet::new)));
		return mapper.toDomain(repository.save(entity));
	}

	@Override
	public void deleteById(Long id) {
		repository.deleteById(id);
	}

	private PageResult<Post> toPageResult(Page<PostJpaEntity> result, PageQuery page) {
		return new PageResult<>(result.map(mapper::toDomain).getContent(), page.page(), page.size(),
				result.getTotalElements());
	}
}
