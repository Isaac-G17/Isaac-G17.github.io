package dev.isaacguzman.portfolio.post.application.port.in;

import dev.isaacguzman.portfolio.shared.domain.Slug;
import java.util.List;

/**
 * Datos para crear o editar un artículo, tal como llegan del panel: la categoría por id
 * y las etiquetas por nombre. El caso de uso los resuelve (y crea las etiquetas nuevas).
 */
public record PostCommand(
		String title,
		Slug slug,
		String excerpt,
		String content,
		String coverImageUrl,
		Long categoryId,
		List<String> tagNames,
		boolean featured,
		String seoTitle,
		String seoDescription) {

	public PostCommand {
		tagNames = tagNames == null ? List.of() : List.copyOf(tagNames);
	}
}
