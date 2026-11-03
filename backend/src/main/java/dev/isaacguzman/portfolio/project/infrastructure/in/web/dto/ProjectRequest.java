package dev.isaacguzman.portfolio.project.infrastructure.in.web.dto;

import dev.isaacguzman.portfolio.project.domain.ProjectType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.hibernate.validator.constraints.URL;

/**
 * Datos para crear o editar un proyecto. Bean Validation revisa el formato antes de llegar
 * al caso de uso; si algo falla, la API responde 400 con el detalle de cada campo.
 */
public record ProjectRequest(
		@NotBlank(message = "El título es obligatorio")
		@Size(max = 150, message = "El título admite máximo 150 caracteres")
		String title,

		@NotBlank(message = "El slug es obligatorio")
		@Size(max = 120, message = "El slug admite máximo 120 caracteres")
		@Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
				message = "El slug solo admite minúsculas sin tildes, números y guiones simples entre palabras")
		String slug,

		@NotNull(message = "El tipo es obligatorio (WORDPRESS o CODE)")
		ProjectType type,

		@NotBlank(message = "El resumen es obligatorio")
		@Size(max = 500, message = "El resumen admite máximo 500 caracteres")
		String summary,

		String content,

		@Size(max = 500, message = "La URL de la portada admite máximo 500 caracteres")
		@URL(message = "La URL de la portada no es válida")
		String coverImageUrl,

		@Size(max = 30, message = "Máximo 30 tecnologías")
		List<@NotBlank(message = "El nombre de la tecnología no puede estar vacío")
				@Size(max = 60, message = "El nombre de la tecnología admite máximo 60 caracteres") String> technologies,

		@Size(max = 500, message = "La URL del sitio admite máximo 500 caracteres")
		@URL(message = "La URL del sitio no es válida")
		String liveUrl,

		@Size(max = 500, message = "La URL del repositorio admite máximo 500 caracteres")
		@URL(message = "La URL del repositorio no es válida")
		String repoUrl,

		// Boolean y no boolean: así el campo es opcional (si no viene, queda en false).
		Boolean featured) {
}
