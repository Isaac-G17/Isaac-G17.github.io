package dev.isaacguzman.portfolio.post.infrastructure.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

interface SpringDataPostRepository extends JpaRepository<PostJpaEntity, Long>, JpaSpecificationExecutor<PostJpaEntity> {

	// La categoría (una por artículo) sí se trae en la misma consulta; las etiquetas, con @BatchSize.
	@Override
	@EntityGraph(attributePaths = "category")
	Page<PostJpaEntity> findAll(Specification<PostJpaEntity> spec, Pageable pageable);
}
