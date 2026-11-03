package dev.isaacguzman.portfolio.post.infrastructure.out.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataCategoryRepository extends JpaRepository<CategoryJpaEntity, Long> {

	List<CategoryJpaEntity> findAllByOrderByNameAsc();

	boolean existsByNameIgnoreCase(String name);

	boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

	boolean existsBySlug(String slug);

	boolean existsBySlugAndIdNot(String slug, Long id);
}
