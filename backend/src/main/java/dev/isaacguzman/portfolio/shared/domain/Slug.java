package dev.isaacguzman.portfolio.shared.domain;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Identificador legible para las URLs, por ejemplo "galeria-paredes-expression".
 * Solo minúsculas sin tildes, números y guiones simples entre palabras.
 */
public record Slug(String value) {

	public static final int MAX_LENGTH = 120;

	private static final Pattern FORMAT = Pattern.compile("^[a-z0-9]+(?:-[a-z0-9]+)*$");

	public Slug {
		Objects.requireNonNull(value, "El slug es obligatorio");
		if (!isValid(value)) {
			throw new IllegalArgumentException("Slug inválido: " + value);
		}
	}

	public static boolean isValid(String value) {
		return value != null && value.length() <= MAX_LENGTH && FORMAT.matcher(value).matches();
	}

	@Override
	public String toString() {
		return value;
	}
}
