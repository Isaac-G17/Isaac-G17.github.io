package dev.isaacguzman.portfolio.shared.domain;

/**
 * Base para los errores de "ya existe" del dominio, por ejemplo un slug repetido.
 * La API la convierte en un 409.
 */
public abstract class ConflictException extends RuntimeException {

	protected ConflictException(String message) {
		super(message);
	}
}
