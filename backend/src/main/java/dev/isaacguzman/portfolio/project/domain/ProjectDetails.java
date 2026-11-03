package dev.isaacguzman.portfolio.project.domain;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * Datos de un proyecto que el administrador escribe al crearlo o editarlo
 * (todo menos el id, el estado y las fechas).
 */
public record ProjectDetails(
		String title,
		Slug slug,
		ProjectType type,
		String summary,
		String content,
		String coverImageUrl,
		List<String> technologies,
		String liveUrl,
		String repoUrl,
		boolean featured) {

	public ProjectDetails {
		Objects.requireNonNull(title, "El título es obligatorio");
		Objects.requireNonNull(slug, "El slug es obligatorio");
		Objects.requireNonNull(type, "El tipo es obligatorio");
		content = content == null ? "" : content;
		technologies = normalize(technologies);
	}

	/** Sin espacios sobrantes, sin vacíos y sin repetidos (sin importar mayúsculas). */
	private static List<String> normalize(Collection<String> technologies) {
		if (technologies == null) {
			return List.of();
		}
		Set<String> seen = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		return technologies.stream()
				.filter(Objects::nonNull)
				.map(String::strip)
				.filter(name -> !name.isEmpty())
				.filter(seen::add)
				.toList();
	}
}
