package dev.isaacguzman.portfolio.shared.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class SlugTest {

	@ParameterizedTest
	@ValueSource(strings = {"portafolio", "galeria-paredes-expression", "app-2026", "a"})
	void acceptsLowercaseWordsSeparatedBySingleHyphens(String value) {
		assertThat(new Slug(value).value()).isEqualTo(value);
	}

	@ParameterizedTest
	@ValueSource(strings = {"", "Mayusculas", "con espacio", "tilde-é", "-inicio", "fin-", "doble--guion", "guion_bajo"})
	void rejectsInvalidFormats(String value) {
		assertThat(Slug.isValid(value)).isFalse();
		assertThatThrownBy(() -> new Slug(value)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void respectsTheMaximumLength() {
		assertThat(Slug.isValid("a".repeat(Slug.MAX_LENGTH))).isTrue();
		assertThat(Slug.isValid("a".repeat(Slug.MAX_LENGTH + 1))).isFalse();
	}

	@ParameterizedTest
	@CsvSource({
			"Opinión y Noticias, opinion-y-noticias",
			"Spring Boot, spring-boot",
			"  C# & .NET  , c-net",
			"Año 2026, ano-2026",
			"Ñandú--veloz, nandu-veloz"
	})
	void generatesSlugsFromText(String text, String expected) {
		assertThat(Slug.fromText(text).value()).isEqualTo(expected);
	}

	@Test
	void generatedSlugIsCutAtTheMaximumLength() {
		String longText = "palabra ".repeat(30);

		Slug slug = Slug.fromText(longText);

		assertThat(slug.value()).hasSizeLessThanOrEqualTo(Slug.MAX_LENGTH).doesNotEndWith("-");
	}

	@Test
	void textWithoutLettersOrNumbersCannotBecomeASlug() {
		assertThatThrownBy(() -> Slug.fromText("¡¡¡!!!")).isInstanceOf(InvalidDataException.class);
	}
}
