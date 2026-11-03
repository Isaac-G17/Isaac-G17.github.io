package dev.isaacguzman.portfolio.post.infrastructure.out.persistence;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataTagRepository extends JpaRepository<TagJpaEntity, Long> {

	List<TagJpaEntity> findAllByOrderByNameAsc();

	List<TagJpaEntity> findBySlugIn(Collection<String> slugs);

	boolean existsBySlug(String slug);

	boolean existsBySlugAndIdNot(String slug, Long id);
}
