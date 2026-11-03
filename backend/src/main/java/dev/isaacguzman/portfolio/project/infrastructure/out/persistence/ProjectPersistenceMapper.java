package dev.isaacguzman.portfolio.project.infrastructure.out.persistence;

import dev.isaacguzman.portfolio.project.domain.Project;
import dev.isaacguzman.portfolio.project.domain.Slug;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/** MapStruct genera la implementación al compilar (ProjectPersistenceMapperImpl). */
@Mapper
interface ProjectPersistenceMapper {

	// MapStruct confunde publish(Instant) y unpublish(Instant) con "setters fluidos" (reciben un valor
	// y devuelven el mismo tipo). No son campos: se ignoran para que no avise.
	@Mapping(target = "publish", ignore = true)
	@Mapping(target = "unpublish", ignore = true)
	Project toDomain(ProjectJpaEntity entity);

	/**
	 * Copia los datos del dominio sobre una entidad nueva o ya existente.
	 * El id lo genera la base de datos y las tecnologías las resuelve el adaptador.
	 */
	@Mapping(target = "technologies", ignore = true)
	void copyToEntity(Project project, @MappingTarget ProjectJpaEntity entity);

	default Slug toSlug(String value) {
		return new Slug(value);
	}

	default String fromSlug(Slug slug) {
		return slug.value();
	}

	/** Solo los nombres, en orden alfabético para que la respuesta sea estable. */
	default List<String> toTechnologyNames(Set<TechnologyJpaEntity> technologies) {
		return technologies.stream()
				.map(TechnologyJpaEntity::getName)
				.sorted(String.CASE_INSENSITIVE_ORDER)
				.toList();
	}
}
