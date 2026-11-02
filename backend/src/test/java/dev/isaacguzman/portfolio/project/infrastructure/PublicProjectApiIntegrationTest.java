package dev.isaacguzman.portfolio.project.infrastructure;

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

/** Prueba la API de punta a punta: controlador, caso de uso, JPA, Flyway y PostgreSQL real. */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@Sql("/sql/projects.sql")
class PublicProjectApiIntegrationTest {

	@Autowired
	private MockMvc mvc;

	@Test
	void listsOnlyPublishedProjectsNewestFirst() throws Exception {
		mvc.perform(get("/api/public/projects"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[*].slug", contains("portafolio", "tienda-ejemplo")))
				.andExpect(jsonPath("$[0].technologies", contains("Angular", "Spring Boot")))
				.andExpect(jsonPath("$[0].content").doesNotExist());
	}

	@Test
	void filtersByType() throws Exception {
		mvc.perform(get("/api/public/projects").param("type", "WORDPRESS"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[*].slug", contains("tienda-ejemplo")));
	}

	@Test
	void unknownTypeIsABadRequest() throws Exception {
		mvc.perform(get("/api/public/projects").param("type", "OTRO"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400));
	}

	@Test
	void returnsThePublishedProjectBySlug() throws Exception {
		mvc.perform(get("/api/public/projects/portafolio"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Portafolio"))
				.andExpect(jsonPath("$.content").value("# Portafolio"))
				.andExpect(jsonPath("$.repoUrl").value("https://github.com/Isaac-G17/Isaac-G17.github.io"));
	}

	@Test
	void draftProjectIsNotFound() throws Exception {
		mvc.perform(get("/api/public/projects/borrador"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.title").value("Recurso no encontrado"));
	}

	@Test
	void unknownSlugIsNotFound() throws Exception {
		mvc.perform(get("/api/public/projects/no-existe"))
				.andExpect(status().isNotFound());
	}
}
