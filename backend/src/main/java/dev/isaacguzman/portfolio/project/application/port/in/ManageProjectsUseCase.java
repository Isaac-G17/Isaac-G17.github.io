package dev.isaacguzman.portfolio.project.application.port.in;

import dev.isaacguzman.portfolio.project.domain.DuplicateSlugException;
import dev.isaacguzman.portfolio.project.domain.Project;
import dev.isaacguzman.portfolio.project.domain.ProjectDetails;
import dev.isaacguzman.portfolio.project.domain.ProjectNotFoundException;
import java.util.List;

/**
 * Casos de uso del panel de administración. Ve los proyectos en cualquier estado.
 * Los métodos con id lanzan {@link ProjectNotFoundException} si no existe.
 */
public interface ManageProjectsUseCase {

	List<Project> getAll();

	Project getById(Long id);

	/** Crea el proyecto como borrador. @throws DuplicateSlugException si el slug ya está en uso */
	Project create(ProjectDetails details);

	/** @throws DuplicateSlugException si el nuevo slug ya lo usa otro proyecto */
	Project update(Long id, ProjectDetails details);

	Project publish(Long id);

	/** Lo oculta del sitio público sin borrarlo: vuelve a borrador. */
	Project unpublish(Long id);

	void delete(Long id);
}
