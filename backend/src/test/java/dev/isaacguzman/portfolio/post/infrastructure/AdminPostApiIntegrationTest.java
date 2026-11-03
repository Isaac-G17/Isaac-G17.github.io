package dev.isaacguzman.portfolio.post.infrastructure;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import dev.isaacguzman.portfolio.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

/** Prueba la API de administración de artículos de punta a punta con PostgreSQL real. */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@Sql("/sql/posts.sql")
class AdminPostApiIntegrationTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private JdbcTemplate jdbc;

	@Test
	void createsADraftWithReadingTimeAndReusedTags() throws Exception {
		String body = postJson("nuevo-articulo", backendId(), "palabra ".repeat(450), "[\"JAVA\", \"Docker\"]");

		mvc.perform(post("/api/admin/posts").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", startsWith("/api/admin/posts/")))
				.andExpect(jsonPath("$.status").value("DRAFT"))
				.andExpect(jsonPath("$.scheduled").value(false))
				.andExpect(jsonPath("$.publishedAt", nullValue()))
				// 450 palabras a 200 por minuto → 3 minutos.
				.andExpect(jsonPath("$.readingTimeMinutes").value(3))
				.andExpect(jsonPath("$.category.slug").value("backend"))
				// "JAVA" reutiliza la etiqueta "Java" existente; "Docker" se crea.
				.andExpect(jsonPath("$.tags[*].slug", contains("docker", "java")));

		mvc.perform(get("/api/public/posts/nuevo-articulo")).andExpect(status().isNotFound());
	}

	@Test
	void schedulesThePublicationForAFutureDate() throws Exception {
		Long id = createDraft("programado-nuevo");

		mvc.perform(post("/api/admin/posts/{id}/publish", id)
						.contentType(MediaType.APPLICATION_JSON)
						.content("{ \"publishAt\": \"2099-06-01T10:00:00Z\" }"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("PUBLISHED"))
				.andExpect(jsonPath("$.scheduled").value(true))
				.andExpect(jsonPath("$.publishedAt").value("2099-06-01T10:00:00Z"));

		// Programado: todavía no se ve en el sitio público.
		mvc.perform(get("/api/public/posts/programado-nuevo")).andExpect(status().isNotFound());
		mvc.perform(get("/api/public/posts")).andExpect(jsonPath("$.items[*].slug", not(hasItem("programado-nuevo"))));
	}

	@Test
	void publishingWithoutBodyPublishesNow() throws Exception {
		Long id = createDraft("ya-publicado");

		mvc.perform(post("/api/admin/posts/{id}/publish", id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.scheduled").value(false))
				.andExpect(jsonPath("$.publishedAt", notNullValue()));

		mvc.perform(get("/api/public/posts/ya-publicado")).andExpect(status().isOk());
	}

	@Test
	void unpublishingHidesThePost() throws Exception {
		Long id = idOf("intro-spring");

		mvc.perform(post("/api/admin/posts/{id}/unpublish", id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("DRAFT"));

		mvc.perform(get("/api/public/posts/intro-spring")).andExpect(status().isNotFound());
	}

	@Test
	void updatingRecalculatesTheReadingTime() throws Exception {
		Long id = idOf("intro-spring");
		String body = postJson("intro-spring", backendId(), "palabra ".repeat(1000), "[]");

		mvc.perform(put("/api/admin/posts/{id}", id).contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.readingTimeMinutes").value(5))
				.andExpect(jsonPath("$.status").value("PUBLISHED"))
				.andExpect(jsonPath("$.tags").isEmpty());
	}

	@Test
	void invalidDataReturns400WithTheFields() throws Exception {
		mvc.perform(post("/api/admin/posts").contentType(MediaType.APPLICATION_JSON)
						.content("{ \"title\": \"\", \"slug\": \"Mal Slug\", \"seoTitle\": \"" + "x".repeat(80) + "\" }"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[*].field",
						contains("categoryId", "excerpt", "seoTitle", "slug", "title")));
	}

	@Test
	void unknownCategoryReturns400() throws Exception {
		mvc.perform(post("/api/admin/posts").contentType(MediaType.APPLICATION_JSON)
						.content(postJson("sin-categoria", 999_999L, "texto", "[]")))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.detail").value("No existe la categoría con id 999999"));
	}

	@Test
	void duplicateSlugReturns409() throws Exception {
		mvc.perform(post("/api/admin/posts").contentType(MediaType.APPLICATION_JSON)
						.content(postJson("intro-spring", backendId(), "texto", "[]")))
				.andExpect(status().isConflict());
	}

	@Test
	void listsAllPostsIncludingDraftsAndScheduled() throws Exception {
		mvc.perform(get("/api/admin/posts").param("size", "50"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.totalItems").value(6))
				.andExpect(jsonPath("$.items[*].slug", hasItem("borrador")))
				.andExpect(jsonPath("$.items[?(@.slug == 'programado')].scheduled", contains(true)));
	}

	@Test
	void deletesThePost() throws Exception {
		Long id = idOf("intro-spring");

		mvc.perform(delete("/api/admin/posts/{id}", id)).andExpect(status().isNoContent());
		mvc.perform(get("/api/admin/posts/{id}", id)).andExpect(status().isNotFound());
	}

	private Long createDraft(String slug) throws Exception {
		String json = mvc.perform(post("/api/admin/posts").contentType(MediaType.APPLICATION_JSON)
						.content(postJson(slug, backendId(), "texto", "[]")))
				.andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString();
		return ((Number) JsonPath.read(json, "$.id")).longValue();
	}

	private Long idOf(String slug) {
		return jdbc.queryForObject("SELECT id FROM posts WHERE slug = ?", Long.class, slug);
	}

	private Long backendId() {
		return jdbc.queryForObject("SELECT id FROM categories WHERE slug = 'backend'", Long.class);
	}

	private static String postJson(String slug, Long categoryId, String content, String tagsJson) {
		return """
				{
				  "title": "Artículo de prueba",
				  "slug": "%s",
				  "excerpt": "Extracto",
				  "content": "%s",
				  "categoryId": %d,
				  "tags": %s
				}
				""".formatted(slug, content, categoryId, tagsJson);
	}
}
