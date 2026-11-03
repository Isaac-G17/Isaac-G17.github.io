package dev.isaacguzman.portfolio.shared.application;

/**
 * Qué página se pide. La primera página es la 0.
 * Es propia (y no la de Spring) para que los casos de uso no dependan del framework.
 */
public record PageQuery(int page, int size) {

	public static final int MAX_SIZE = 50;

	public PageQuery {
		if (page < 0) {
			throw new IllegalArgumentException("La página no puede ser negativa");
		}
		if (size < 1 || size > MAX_SIZE) {
			throw new IllegalArgumentException("El tamaño de página debe estar entre 1 y " + MAX_SIZE);
		}
	}
}
