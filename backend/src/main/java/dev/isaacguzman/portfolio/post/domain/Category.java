package dev.isaacguzman.portfolio.post.domain;

import dev.isaacguzman.portfolio.shared.domain.Slug;
import java.util.Objects;

/** Categoría fija del blog, por ejemplo "Backend" o "Tutoriales". */
public record Category(Long id, String name, Slug slug, String description) {

	public Category {
		Objects.requireNonNull(name, "El nombre de la categoría es obligatorio");
		Objects.requireNonNull(slug, "El slug de la categoría es obligatorio");
	}
}
