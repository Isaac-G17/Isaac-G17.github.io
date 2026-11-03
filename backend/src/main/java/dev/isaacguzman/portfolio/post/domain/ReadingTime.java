package dev.isaacguzman.portfolio.post.domain;

import java.util.regex.Pattern;

/**
 * Calcula los minutos de lectura de un artículo en Markdown.
 * Se usa al guardar el artículo (el resultado se guarda en la base de datos).
 */
public final class ReadingTime {

	/** Velocidad de lectura media de un texto técnico en español. */
	static final int WORDS_PER_MINUTE = 200;

	// Bloques de código: se leen más rápido que el texto, así que no cuentan como palabras.
	private static final Pattern CODE_BLOCKS = Pattern.compile("```.*?```", Pattern.DOTALL);
	// Imágenes y enlaces en Markdown: solo cuenta el texto visible, no la URL.
	private static final Pattern LINKS = Pattern.compile("!?\\[([^\\]]*)]\\([^)]*\\)");
	private static final Pattern WORD = Pattern.compile("[\\p{L}\\p{N}]+");

	private ReadingTime() {
	}

	/** Mínimo 1 minuto, aunque el artículo esté vacío. */
	public static int minutesFor(String markdown) {
		if (markdown == null || markdown.isBlank()) {
			return 1;
		}
		String text = CODE_BLOCKS.matcher(markdown).replaceAll(" ");
		text = LINKS.matcher(text).replaceAll("$1");
		long words = WORD.matcher(text).results().count();
		return (int) Math.max(1, Math.ceil((double) words / WORDS_PER_MINUTE));
	}
}
