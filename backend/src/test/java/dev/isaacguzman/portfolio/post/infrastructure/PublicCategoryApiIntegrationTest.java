package dev.isaacguzman.portfolio.post.infrastructure;

import static org.hamcrest.Matchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.isaacguzman.portfolio.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

/** Las categorías vienen de la migración V2, así que no hace falta cargar datos de prueba. */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class PublicCategoryApiIntegrationTest {

	@Autowired
	private MockMvc mvc;

	@Test
	void listsTheBlogCategoriesAlphabetically() throws Exception {
		mvc.perform(get("/api/public/categories"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[*].slug", contains("backend", "desarrollo-web", "herramientas", "mi-camino",
						"opinion-y-noticias", "tutoriales")))
				.andExpect(jsonPath("$[0].name").value("Backend"))
				.andExpect(jsonPath("$[0].description").value("Spring Boot, APIs, bases de datos"));
	}
}
