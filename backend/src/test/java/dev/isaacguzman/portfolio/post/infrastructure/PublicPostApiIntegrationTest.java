package dev.isaacguzman.portfolio.post.infrastructure;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.isaacguzman.portfolio.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

/** Prueba el blog público de punta a punta con PostgreSQL real. */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@Sql("/sql/posts.sql")
class PublicPostApiIntegrationTest {

	@Autowired
	private MockMvc mvc;

	@Test
	void listsOnlyVisiblePostsNewestFirst() throws Exception {
		// No aparecen el borrador ni el programado para 2099.
		mvc.perform(get("/api/public/posts"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.items[*].slug",
						contains("testcontainers", "angular-signals", "intro-spring", "wordpress-rapido")))
				.andExpect(jsonPath("$.totalItems").value(4))
				.andExpect(jsonPath("$.items[0].content").doesNotExist())
				.andExpect(jsonPath("$.items[0].category.slug").value("backend"))
				.andExpect(jsonPath("$.items[0].readingTimeMinutes").value(7));
	}

	@Test
	void filtersByCategory() throws Exception {
		mvc.perform(get("/api/public/posts").param("category", "backend"))
				.andExpect(jsonPath("$.items[*].slug", contains("testcontainers", "intro-spring")));
	}

	@Test
	void filtersByTag() throws Exception {
		mvc.perform(get("/api/public/posts").param("tag", "java"))
				.andExpect(jsonPath("$.items[*].slug", contains("testcontainers", "intro-spring")))
				.andExpect(jsonPath("$.items[1].tags[*].slug", contains("java", "spring")));
	}

	@Test
	void combinesFilters() throws Exception {
		mvc.perform(get("/api/public/posts").param("category", "backend").param("tag", "spring"))
				.andExpect(jsonPath("$.items[*].slug", contains("intro-spring")));
	}

	@Test
	void searchesInTheTitleIgnoringCase() throws Exception {
		mvc.perform(get("/api/public/posts").param("q", "SPRING"))
				.andExpect(jsonPath("$.items[*].slug", contains("intro-spring")));
	}

	@Test
	void searchTreatsPercentAsText() throws Exception {
		mvc.perform(get("/api/public/posts").param("q", "%"))
				.andExpect(jsonPath("$.items[*].slug", contains("wordpress-rapido")));
	}

	@Test
	void paginates() throws Exception {
		mvc.perform(get("/api/public/posts").param("page", "1").param("size", "3"))
				.andExpect(jsonPath("$.items", hasSize(1)))
				.andExpect(jsonPath("$.items[0].slug").value("wordpress-rapido"))
				.andExpect(jsonPath("$.page").value(1))
				.andExpect(jsonPath("$.totalItems").value(4))
				.andExpect(jsonPath("$.totalPages").value(2));
	}

	@Test
	void invalidPageSizeIsABadRequest() throws Exception {
		mvc.perform(get("/api/public/posts").param("size", "500"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void returnsTheDetailWithSeoFallback() throws Exception {
		mvc.perform(get("/api/public/posts/intro-spring"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content").value("# Spring"))
				.andExpect(jsonPath("$.seoTitle").value("Introducción a Spring Boot"))
				.andExpect(jsonPath("$.seoDescription").value("Primeros pasos"));

		mvc.perform(get("/api/public/posts/testcontainers"))
				.andExpect(jsonPath("$.seoTitle").value("Testcontainers con Spring"));
	}

	@Test
	void scheduledAndDraftPostsAreNotFound() throws Exception {
		mvc.perform(get("/api/public/posts/programado")).andExpect(status().isNotFound());
		mvc.perform(get("/api/public/posts/borrador")).andExpect(status().isNotFound());
		mvc.perform(get("/api/public/posts/no-existe")).andExpect(status().isNotFound());
	}
}
