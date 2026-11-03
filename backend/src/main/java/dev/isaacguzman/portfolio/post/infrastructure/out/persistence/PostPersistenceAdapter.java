package dev.isaacguzman.portfolio.post.infrastructure.out.persistence;

import dev.isaacguzman.portfolio.post.application.port.out.PostRepositoryPort;
import dev.isaacguzman.portfolio.post.domain.Post;
import dev.isaacguzman.portfolio.post.domain.PostSearch;
import dev.isaacguzman.portfolio.shared.application.PageQuery;
import dev.isaacguzman.portfolio.shared.application.PageResult;
import dev.isaacguzman.portfolio.shared.domain.Slug;
import java.time.Instant;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/** Implementa el puerto de salida con Spring Data JPA y PostgreSQL. */
@Component
class PostPersistenceAdapter implements PostRepositoryPort {

	// El id desempata artículos publicados en el mismo instante, para que el orden no cambie entre páginas.
	private static final Sort NEWEST_FIRST = Sort.by(Sort.Order.desc("publishedAt"), Sort.Order.desc("id"));

	private final SpringDataPostRepository repository;
	private final PostPersistenceMapper mapper;

	PostPersistenceAdapter(SpringDataPostRepository repository, PostPersistenceMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	@Override
	public PageResult<Post> findPublished(PostSearch search, Instant now, PageQuery page) {
		Page<PostJpaEntity> result = repository.findAll(
				PostSpecifications.publishedMatching(search, now),
				PageRequest.of(page.page(), page.size(), NEWEST_FIRST));
		return new PageResult<>(result.map(mapper::toDomain).getContent(), page.page(), page.size(),
				result.getTotalElements());
	}

	@Override
	public Optional<Post> findPublishedBySlug(Slug slug, Instant now) {
		Specification<PostJpaEntity> bySlug = (root, query, cb) -> cb.equal(root.get("slug"), slug.value());
		return repository.findOne(PostSpecifications.visibleAt(now).and(bySlug)).map(mapper::toDomain);
	}
}
