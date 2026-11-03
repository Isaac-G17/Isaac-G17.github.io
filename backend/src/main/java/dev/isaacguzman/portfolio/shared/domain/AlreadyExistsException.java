package dev.isaacguzman.portfolio.shared.domain;

/** Un valor que debe ser único ya está en uso, por ejemplo un slug o un nombre. */
public class AlreadyExistsException extends ConflictException {

	public AlreadyExistsException(String what, String value) {
		super("Ya existe " + what + " \"" + value + "\"");
	}
}
