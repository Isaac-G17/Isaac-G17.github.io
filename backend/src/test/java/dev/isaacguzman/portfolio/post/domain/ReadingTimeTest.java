package dev.isaacguzman.portfolio.post.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ReadingTimeTest {

	@Test
	void emptyContentIsOneMinute() {
		assertThat(ReadingTime.minutesFor(null)).isEqualTo(1);
		assertThat(ReadingTime.minutesFor("   ")).isEqualTo(1);
	}

	@Test
	void roundsUpToTheNextMinute() {
		assertThat(ReadingTime.minutesFor(words(ReadingTime.WORDS_PER_MINUTE))).isEqualTo(1);
		assertThat(ReadingTime.minutesFor(words(ReadingTime.WORDS_PER_MINUTE + 1))).isEqualTo(2);
	}

	@Test
	void ignoresCodeBlocksAndLinkUrls() {
		String code = "```java\n" + words(1000) + "\n```";
		String link = "[mi sitio](https://example.com/una/ruta/muy/larga/con/muchas/palabras)";

		// Solo cuentan "mi sitio" y las 199 palabras: 201 palabras → 2 minutos.
		assertThat(ReadingTime.minutesFor(code + " " + link + " " + words(199))).isEqualTo(2);
	}

	@Test
	void countsWordsWithAccents() {
		assertThat(ReadingTime.minutesFor("Introducción a la programación orientada a dominio")).isEqualTo(1);
	}

	private static String words(int count) {
		return "palabra ".repeat(count);
	}
}
