package dev.isaacguzman.portfolio.post.domain;

import dev.isaacguzman.portfolio.shared.domain.Slug;
import java.util.List;
import java.util.Objects;

/**
 * Datos de un artículo que escribe el administrador (todo menos el id, el estado,
 * el tiempo de lectura y las fechas). La categoría y las etiquetas ya vienen resueltas.
 */
public record PostDetails(
		String title,
		Slug slug,
		String excerpt,
		String content,
		String coverImageUrl,
		Category category,
		List<Tag> tags,
		boolean featured,
		String seoTitle,
		String seoDescription) {

	public PostDetails {
		Objects.requireNonNull(title, "El título es obligatorio");
		Objects.requireNonNull(slug, "El slug es obligatorio");
		Objects.requireNonNull(excerpt, "El extracto es obligatorio");
		Objects.requireNonNull(category, "La categoría es obligatoria");
		content = content == null ? "" : content;
		tags = tags == null ? List.of() : List.copyOf(tags);
	}
}
