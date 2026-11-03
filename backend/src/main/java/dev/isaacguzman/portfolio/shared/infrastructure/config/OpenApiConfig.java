package dev.isaacguzman.portfolio.shared.infrastructure.config;

import dev.isaacguzman.portfolio.shared.infrastructure.web.openapi.ApiTags;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Datos generales de la documentación de la API. springdoc lee los controladores y genera
 * la especificación en /v3/api-docs y la página interactiva en /swagger-ui.html.
 */
@Configuration(proxyBeanMethods = false)
class OpenApiConfig {

	@Bean
	OpenAPI portfolioOpenApi() {
		return new OpenAPI()
				.info(new Info()
						.title("API del portafolio de Isaac Guzmán")
						.version("v1")
						.description("""
								API de la plataforma del portafolio: proyectos y blog.

								- `/api/public/**`: abierta; solo devuelve contenido publicado.
								- `/api/admin/**`: gestión completa (borradores, publicación, borrado).

								Los errores siguen el formato **ProblemDetail** (RFC 9457).""")
						.contact(new Contact().name("Isaac Guzmán").url("https://github.com/Isaac-G17"))
						.license(new License().name("MIT").url("https://opensource.org/licenses/MIT")))
				// El orden de las etiquetas es el orden de las secciones en Swagger UI.
				.addTagsItem(new Tag().name(ApiTags.PROJECTS_PUBLIC).description("Proyectos publicados del portafolio"))
				.addTagsItem(new Tag().name(ApiTags.BLOG_PUBLIC).description("Artículos publicados y categorías del blog"))
				.addTagsItem(new Tag().name(ApiTags.PROJECTS_ADMIN).description("Gestión de proyectos en cualquier estado"))
				.addTagsItem(new Tag().name(ApiTags.BLOG_ADMIN).description("Gestión de artículos, categorías y etiquetas"));
	}
}
