package dev.isaacguzman.portfolio.post.infrastructure.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Datos para crear o editar categorías y etiquetas. */
public final class TaxonomyRequests {

	private TaxonomyRequests() {
	}

	/** Si no viene el slug, se genera del nombre. */
	public record CategoryRequest(
			@NotBlank(message = "El nombre es obligatorio")
			@Size(max = 60, message = "El nombre admite máximo 60 caracteres")
			String name,

			@Size(max = 120, message = "El slug admite máximo 120 caracteres")
			@Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
					message = "El slug solo admite minúsculas sin tildes, números y guiones simples entre palabras")
			String slug,

			@Size(max = 300, message = "La descripción admite máximo 300 caracteres")
			String description) {
	}

	/** El slug de la etiqueta siempre se genera del nombre. */
	public record TagRequest(
			@NotBlank(message = "El nombre es obligatorio")
			@Size(max = 60, message = "El nombre admite máximo 60 caracteres")
			String name) {
	}
}
