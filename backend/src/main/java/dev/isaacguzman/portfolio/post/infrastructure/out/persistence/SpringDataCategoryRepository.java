package dev.isaacguzman.portfolio.post.infrastructure.out.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataCategoryRepository extends JpaRepository<CategoryJpaEntity, Long> {

	List<CategoryJpaEntity> findAllByOrderByNameAsc();
}
