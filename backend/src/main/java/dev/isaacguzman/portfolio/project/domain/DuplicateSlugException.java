package dev.isaacguzman.portfolio.project.domain;

import dev.isaacguzman.portfolio.shared.domain.ConflictException;

public class DuplicateSlugException extends ConflictException {

	public DuplicateSlugException(Slug slug) {
		super("Ya existe un proyecto con el slug \"" + slug + "\"");
	}
}
