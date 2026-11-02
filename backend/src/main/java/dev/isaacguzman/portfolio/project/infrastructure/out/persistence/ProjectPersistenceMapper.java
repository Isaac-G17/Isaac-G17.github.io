package dev.isaacguzman.portfolio.project.infrastructure.out.persistence;

import dev.isaacguzman.portfolio.project.domain.Project;
import dev.isaacguzman.portfolio.project.domain.Slug;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

/** MapStruct genera la implementación al compilar (ProjectPersistenceMapperImpl). */
@Mapper
interface ProjectPersistenceMapper {

	Project toDomain(ProjectJpaEntity entity);

	default Slug toSlug(String value) {
		return new Slug(value);
	}

	/** Solo los nombres, en orden alfabético para que la respuesta sea estable. */
	default List<String> toTechnologyNames(Set<TechnologyJpaEntity> technologies) {
		return technologies.stream()
				.map(TechnologyJpaEntity::getName)
				.sorted(String.CASE_INSENSITIVE_ORDER)
				.toList();
	}
}
