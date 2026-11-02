package dev.isaacguzman.portfolio.project.domain;

import dev.isaacguzman.portfolio.shared.domain.NotFoundException;

public class ProjectNotFoundException extends NotFoundException {

	public ProjectNotFoundException(String slug) {
		super("No existe un proyecto publicado con el slug \"" + slug + "\"");
	}
}
