package dev.isaacguzman.portfolio.post.domain;

import dev.isaacguzman.portfolio.shared.domain.Slug;
import java.util.Objects;

/** Categoría fija del blog, por ejemplo "Backend" o "Tutoriales". */
public record Category(Long id, String name, Slug slug, String description) {

	public Category {
		Objects.requireNonNull(name, "El nombre de la categoría es obligatorio");
		Objects.requireNonNull(slug, "El slug de la categoría es obligatorio");
	}

	/** Si no se indica un slug, sale del nombre ("Mi camino" → "mi-camino"). */
	public static Category create(String name, Slug slug, String description) {
		String cleanName = name.strip();
		return new Category(null, cleanName, slug != null ? slug : Slug.fromText(cleanName), description);
	}

	public Category update(String newName, Slug newSlug, String newDescription) {
		String cleanName = newName.strip();
		return new Category(id, cleanName, newSlug != null ? newSlug : Slug.fromText(cleanName), newDescription);
	}
}
