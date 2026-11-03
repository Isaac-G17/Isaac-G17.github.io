package dev.isaacguzman.portfolio.post.domain;

import dev.isaacguzman.portfolio.shared.domain.NotFoundException;

/** Artículo, categoría o etiqueta que no existe. */
public class PostNotFoundException extends NotFoundException {

	private PostNotFoundException(String message) {
		super(message);
	}

	/** Para el sitio público: no existe, es borrador o está programado para el futuro. */
	public static PostNotFoundException publishedWithSlug(String slug) {
		return new PostNotFoundException("No existe un artículo publicado con el slug \"" + slug + "\"");
	}

	public static PostNotFoundException withId(Long id) {
		return new PostNotFoundException("No existe un artículo con el id " + id);
	}

	public static PostNotFoundException categoryWithId(Long id) {
		return new PostNotFoundException("No existe una categoría con el id " + id);
	}

	public static PostNotFoundException tagWithId(Long id) {
		return new PostNotFoundException("No existe una etiqueta con el id " + id);
	}
}
