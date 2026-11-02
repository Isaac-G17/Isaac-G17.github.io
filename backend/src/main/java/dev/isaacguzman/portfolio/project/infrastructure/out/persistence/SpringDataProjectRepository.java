package dev.isaacguzman.portfolio.project.infrastructure.out.persistence;

import dev.isaacguzman.portfolio.project.domain.ProjectStatus;
import dev.isaacguzman.portfolio.project.domain.ProjectType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataProjectRepository extends JpaRepository<ProjectJpaEntity, Long> {

	// @EntityGraph trae las tecnologías en la misma consulta y evita el problema N+1.
	@EntityGraph(attributePaths = "technologies")
	List<ProjectJpaEntity> findByStatusOrderByPublishedAtDesc(ProjectStatus status);

	@EntityGraph(attributePaths = "technologies")
	List<ProjectJpaEntity> findByStatusAndTypeOrderByPublishedAtDesc(ProjectStatus status, ProjectType type);

	@EntityGraph(attributePaths = "technologies")
	Optional<ProjectJpaEntity> findBySlugAndStatus(String slug, ProjectStatus status);
}
