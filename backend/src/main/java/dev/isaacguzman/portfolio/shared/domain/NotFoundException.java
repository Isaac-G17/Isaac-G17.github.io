package dev.isaacguzman.portfolio.shared.domain;

/**
 * Base para los errores de "no existe" del dominio, por ejemplo un proyecto
 * con un slug que no está en la base de datos. La API la convierte en un 404.
 */
public abstract class NotFoundException extends RuntimeException {

	protected NotFoundException(String message) {
		super(message);
	}
}
