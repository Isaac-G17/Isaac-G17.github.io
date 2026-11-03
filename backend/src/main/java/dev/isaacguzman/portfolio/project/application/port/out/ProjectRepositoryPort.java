package dev.isaacguzman.portfolio.project.application.port.out;

import dev.isaacguzman.portfolio.project.domain.Project;
import dev.isaacguzman.portfolio.project.domain.ProjectType;
import dev.isaacguzman.portfolio.project.domain.Slug;
import java.util.List;
import java.util.Optional;

/** Lo que la aplicación necesita de la base de datos de proyectos. */
public interface ProjectRepositoryPort {

	/** Proyectos publicados, del más reciente al más antiguo. Si {@code type} es null, devuelve todos los tipos. */
	List<Project> findPublished(ProjectType type);

	Optional<Project> findPublishedBySlug(Slug slug);

	/** Todos los proyectos, en cualquier estado, del editado más recientemente al más antiguo. */
	List<Project> findAll();

	Optional<Project> findById(Long id);

	/** {@code exceptId} permite ignorar el propio proyecto al editarlo; puede ser null. */
	boolean existsBySlug(Slug slug, Long exceptId);

	/** Crea el proyecto si no tiene id, o lo actualiza si ya existe. Devuelve la versión guardada. */
	Project save(Project project);

	void deleteById(Long id);
}
