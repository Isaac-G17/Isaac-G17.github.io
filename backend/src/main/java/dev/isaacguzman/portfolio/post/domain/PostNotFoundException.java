package dev.isaacguzman.portfolio.post.domain;

import dev.isaacguzman.portfolio.shared.domain.NotFoundException;

public class PostNotFoundException extends NotFoundException {

	private PostNotFoundException(String message) {
		super(message);
	}

	/** Para el sitio público: no existe, es borrador o está programado para el futuro. */
	public static PostNotFoundException publishedWithSlug(String slug) {
		return new PostNotFoundException("No existe un artículo publicado con el slug \"" + slug + "\"");
	}
}
