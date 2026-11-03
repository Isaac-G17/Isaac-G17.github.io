package dev.isaacguzman.portfolio.shared.domain;

/**
 * Datos que tienen el formato correcto pero no tienen sentido para el negocio,
 * por ejemplo una categoría que no existe. La API la convierte en un 400.
 */
public class InvalidDataException extends RuntimeException {

	public InvalidDataException(String message) {
		super(message);
	}
}
