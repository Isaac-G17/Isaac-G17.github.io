package dev.isaacguzman.portfolio.post.infrastructure.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.hibernate.validator.constraints.URL;

/** Datos para crear o editar un artículo. La categoría va por id y las etiquetas por nombre. */
public record PostRequest(
		@NotBlank(message = "El título es obligatorio")
		@Size(max = 150, message = "El título admite máximo 150 caracteres")
		String title,

		@NotBlank(message = "El slug es obligatorio")
		@Size(max = 120, message = "El slug admite máximo 120 caracteres")
		@Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
				message = "El slug solo admite minúsculas sin tildes, números y guiones simples entre palabras")
		String slug,

		@NotBlank(message = "El extracto es obligatorio")
		@Size(max = 500, message = "El extracto admite máximo 500 caracteres")
		String excerpt,

		String content,

		@Size(max = 500, message = "La URL de la portada admite máximo 500 caracteres")
		@URL(message = "La URL de la portada no es válida")
		String coverImageUrl,

		@NotNull(message = "La categoría es obligatoria")
		Long categoryId,

		@Size(max = 20, message = "Máximo 20 etiquetas")
		List<@NotBlank(message = "El nombre de la etiqueta no puede estar vacío")
				@Size(max = 60, message = "El nombre de la etiqueta admite máximo 60 caracteres") String> tags,

		// Boolean y no boolean: así el campo es opcional (si no viene, queda en false).
		Boolean featured,

		@Size(max = 70, message = "El título SEO admite máximo 70 caracteres")
		String seoTitle,

		@Size(max = 160, message = "La descripción SEO admite máximo 160 caracteres")
		String seoDescription) {
}
