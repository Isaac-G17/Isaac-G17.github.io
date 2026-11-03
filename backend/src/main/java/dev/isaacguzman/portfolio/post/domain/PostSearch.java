package dev.isaacguzman.portfolio.post.domain;

/**
 * Filtros de la lista pública de artículos. Cualquiera puede ser null (sin filtro).
 *
 * @param categorySlug por ejemplo "backend"
 * @param tagSlug      por ejemplo "java"
 * @param titleText    texto a buscar dentro del título, sin importar mayúsculas
 */
public record PostSearch(String categorySlug, String tagSlug, String titleText) {

	public PostSearch {
		categorySlug = blankToNull(categorySlug);
		tagSlug = blankToNull(tagSlug);
		titleText = blankToNull(titleText);
	}

	private static String blankToNull(String value) {
		return value == null || value.isBlank() ? null : value.strip();
	}
}
