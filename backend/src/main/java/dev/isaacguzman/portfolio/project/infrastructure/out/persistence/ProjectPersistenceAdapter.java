package dev.isaacguzman.portfolio.project.infrastructure.out.persistence;

import dev.isaacguzman.portfolio.project.application.port.out.ProjectRepositoryPort;
import dev.isaacguzman.portfolio.project.domain.Project;
import dev.isaacguzman.portfolio.project.domain.ProjectStatus;
import dev.isaacguzman.portfolio.project.domain.ProjectType;
import dev.isaacguzman.portfolio.project.domain.Slug;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/** Implementa el puerto de salida con Spring Data JPA y PostgreSQL. */
@Component
class ProjectPersistenceAdapter implements ProjectRepositoryPort {

	private final SpringDataProjectRepository repository;
	private final ProjectPersistenceMapper mapper;

	ProjectPersistenceAdapter(SpringDataProjectRepository repository, ProjectPersistenceMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	@Override
	public List<Project> findPublished(ProjectType type) {
		List<ProjectJpaEntity> entities = type == null
				? repository.findByStatusOrderByPublishedAtDesc(ProjectStatus.PUBLISHED)
				: repository.findByStatusAndTypeOrderByPublishedAtDesc(ProjectStatus.PUBLISHED, type);
		return entities.stream().map(mapper::toDomain).toList();
	}

	@Override
	public Optional<Project> findPublishedBySlug(Slug slug) {
		return repository.findBySlugAndStatus(slug.value(), ProjectStatus.PUBLISHED).map(mapper::toDomain);
	}
}
