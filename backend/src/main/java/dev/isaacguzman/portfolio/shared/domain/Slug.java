package dev.isaacguzman.portfolio.shared.domain;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Identificador legible para las URLs, por ejemplo "galeria-paredes-expression".
 * Solo minúsculas sin tildes, números y guiones simples entre palabras.
 */
public record Slug(String value) {

	public static final int MAX_LENGTH = 120;

	private static final Pattern FORMAT = Pattern.compile("^[a-z0-9]+(?:-[a-z0-9]+)*$");
	private static final Pattern NOT_ALPHANUMERIC = Pattern.compile("[^a-z0-9]+");

	public Slug {
		Objects.requireNonNull(value, "El slug es obligatorio");
		if (!isValid(value)) {
			throw new IllegalArgumentException("Slug inválido: " + value);
		}
	}

	public static boolean isValid(String value) {
		return value != null && value.length() <= MAX_LENGTH && FORMAT.matcher(value).matches();
	}

	/**
	 * Genera un slug a partir de un texto: "Opinión y Noticias" → "opinion-y-noticias".
	 * Quita tildes, pasa a minúsculas y cambia todo lo que no sea letra o número por guiones.
	 *
	 * @throws InvalidDataException si el texto no tiene ninguna letra ni número
	 */
	public static Slug fromText(String text) {
		String withoutAccents = Normalizer.normalize(text == null ? "" : text, Normalizer.Form.NFD)
				.replaceAll("\\p{M}", "");
		String slug = NOT_ALPHANUMERIC.matcher(withoutAccents.toLowerCase(Locale.ROOT)).replaceAll("-")
				.replaceAll("^-+|-+$", "");
		if (slug.length() > MAX_LENGTH) {
			slug = slug.substring(0, MAX_LENGTH).replaceAll("-+$", "");
		}
		if (slug.isEmpty()) {
			throw new InvalidDataException("No se puede generar un slug a partir de \"" + text + "\"");
		}
		return new Slug(slug);
	}

	@Override
	public String toString() {
		return value;
	}
}
