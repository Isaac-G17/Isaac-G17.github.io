package dev.isaacguzman.portfolio.project.application.port.in;

import dev.isaacguzman.portfolio.project.domain.Project;
import dev.isaacguzman.portfolio.project.domain.ProjectNotFoundException;
import dev.isaacguzman.portfolio.project.domain.ProjectType;
import java.util.List;

/** Consultas del sitio público: solo ven proyectos publicados. */
public interface GetPublishedProjectsQuery {

	/** Si {@code type} es null, devuelve todos los tipos. */
	List<Project> getPublishedProjects(ProjectType type);

	/** @throws ProjectNotFoundException si el slug no existe, no es válido o el proyecto no está publicado */
	Project getPublishedProject(String slug);
}
