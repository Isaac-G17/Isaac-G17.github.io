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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

/** Las categorías vienen de la migración V2; posts.sql borra las que hayan creado otros tests. */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@Sql("/sql/posts.sql")
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
