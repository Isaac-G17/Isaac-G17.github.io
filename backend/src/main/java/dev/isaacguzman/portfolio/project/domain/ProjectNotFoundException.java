package dev.isaacguzman.portfolio.project.domain;

import dev.isaacguzman.portfolio.shared.domain.NotFoundException;

public class ProjectNotFoundException extends NotFoundException {

	private ProjectNotFoundException(String message) {
		super(message);
	}

	/** Para el sitio público, que busca por slug y solo ve proyectos publicados. */
	public static ProjectNotFoundException publishedWithSlug(String slug) {
		return new ProjectNotFoundException("No existe un proyecto publicado con el slug \"" + slug + "\"");
	}

	/** Para el panel de administración, que busca por id y ve todos los estados. */
	public static ProjectNotFoundException withId(Long id) {
		return new ProjectNotFoundException("No existe un proyecto con el id " + id);
	}
}
