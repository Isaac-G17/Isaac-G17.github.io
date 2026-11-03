package dev.isaacguzman.portfolio.shared.infrastructure.web.openapi;

/** Secciones de Swagger UI. Cada controlador usa una con {@code @Tag(name = ApiTags...)}. */
public final class ApiTags {

	public static final String PROJECTS_PUBLIC = "Proyectos (público)";
	public static final String BLOG_PUBLIC = "Blog (público)";
	public static final String PROJECTS_ADMIN = "Proyectos (admin)";
	public static final String BLOG_ADMIN = "Blog (admin)";

	private ApiTags() {
	}
}
