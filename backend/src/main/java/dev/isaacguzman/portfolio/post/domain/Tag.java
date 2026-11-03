package dev.isaacguzman.portfolio.post.domain;

import dev.isaacguzman.portfolio.shared.domain.Slug;
import java.util.Objects;

/** Etiqueta libre para filtrar con más detalle, por ejemplo "java" o "angular". */
public record Tag(Long id, String name, Slug slug) {

	public Tag {
		Objects.requireNonNull(name, "El nombre de la etiqueta es obligatorio");
		Objects.requireNonNull(slug, "El slug de la etiqueta es obligatorio");
	}
}
