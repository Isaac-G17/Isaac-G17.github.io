package dev.isaacguzman.portfolio.project.infrastructure.out.persistence;

import dev.isaacguzman.portfolio.project.application.port.out.ProjectRepositoryPort;
import dev.isaacguzman.portfolio.project.domain.Project;
import dev.isaacguzman.portfolio.project.domain.ProjectStatus;
import dev.isaacguzman.portfolio.project.domain.ProjectType;
import dev.isaacguzman.portfolio.shared.domain.Slug;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/** Implementa el puerto de salida con Spring Data JPA y PostgreSQL. */
@Component
class ProjectPersistenceAdapter implements ProjectRepositoryPort {

	private final SpringDataProjectRepository repository;
	private final SpringDataTechnologyRepository technologies;
	private final ProjectPersistenceMapper mapper;

	ProjectPersistenceAdapter(SpringDataProjectRepository repository, SpringDataTechnologyRepository technologies,
			ProjectPersistenceMapper mapper) {
		this.repository = repository;
		this.technologies = technologies;
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

	@Override
	public List<Project> findAll() {
		return repository.findAllByOrderByUpdatedAtDesc().stream().map(mapper::toDomain).toList();
	}

	@Override
	public Optional<Project> findById(Long id) {
		return repository.findWithTechnologiesById(id).map(mapper::toDomain);
	}

	@Override
	public boolean existsBySlug(Slug slug, Long exceptId) {
		return exceptId == null
				? repository.existsBySlug(slug.value())
				: repository.existsBySlugAndIdNot(slug.value(), exceptId);
	}

	@Override
	public Project save(Project project) {
		ProjectJpaEntity entity = project.id() == null
				? new ProjectJpaEntity()
				: repository.findById(project.id()).orElseThrow();
		mapper.copyToEntity(project, entity);
		entity.setTechnologies(resolveTechnologies(project.technologies()));
		return mapper.toDomain(repository.save(entity));
	}

	@Override
	public void deleteById(Long id) {
		repository.deleteById(id);
	}

	/** Reutiliza las tecnologías que ya existen (sin importar mayúsculas) y crea las nuevas. */
	private Set<TechnologyJpaEntity> resolveTechnologies(List<String> names) {
		if (names.isEmpty()) {
			return new HashSet<>();
		}
		Map<String, TechnologyJpaEntity> existing = technologies
				.findByLowerCaseNameIn(names.stream().map(this::lowerCase).toList())
				.stream()
				.collect(Collectors.toMap(t -> lowerCase(t.getName()), Function.identity()));
		return names.stream()
				.map(name -> existing.computeIfAbsent(lowerCase(name),
						key -> technologies.save(new TechnologyJpaEntity(name))))
				.collect(Collectors.toCollection(HashSet::new));
	}

	private String lowerCase(String name) {
		return name.toLowerCase(Locale.ROOT);
	}
}
