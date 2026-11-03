package dev.isaacguzman.portfolio.shared.domain;

/** No se puede borrar algo que otros registros todavía usan, por ejemplo una categoría con artículos. */
public class InUseException extends ConflictException {

	public InUseException(String message) {
		super(message);
	}
}
