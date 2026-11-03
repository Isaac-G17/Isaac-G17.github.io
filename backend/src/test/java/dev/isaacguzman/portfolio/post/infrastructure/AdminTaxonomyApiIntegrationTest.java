package dev.isaacguzman.portfolio.post.infrastructure;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

/** Prueba la API de administración de categorías y etiquetas con PostgreSQL real. */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@Sql("/sql/posts.sql")
class AdminTaxonomyApiIntegrationTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private JdbcTemplate jdbc;

	// --- Categorías ---

	@Test
	void createsACategoryWithTheSlugFromItsName() throws Exception {
		mvc.perform(post("/api/admin/categories").contentType(MediaType.APPLICATION_JSON)
						.content("{ \"name\": \"Bases de datos\", \"description\": \"PostgreSQL y más\" }"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.slug").value("bases-de-datos"));

		mvc.perform(get("/api/public/categories")).andExpect(jsonPath("$[*].slug", hasItem("bases-de-datos")));
	}

	@Test
	void duplicateCategoryNameReturns409() throws Exception {
		mvc.perform(post("/api/admin/categories").contentType(MediaType.APPLICATION_JSON)
						.content("{ \"name\": \"BACKEND\" }"))
				.andExpect(status().isConflict());
	}

	@Test
	void renamesACategory() throws Exception {
		Long id = createCategory("Temporal");

		mvc.perform(put("/api/admin/categories/{id}", id).contentType(MediaType.APPLICATION_JSON)
						.content("{ \"name\": \"Temporal renombrada\", \"slug\": \"temporal-2\" }"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.slug").value("temporal-2"));
	}

	@Test
	void categoryWithPostsCannotBeDeleted() throws Exception {
		Long backend = jdbc.queryForObject("SELECT id FROM categories WHERE slug = 'backend'", Long.class);

		mvc.perform(delete("/api/admin/categories/{id}", backend))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.title").value("Conflicto con datos existentes"));
	}

	@Test
	void deletesAnUnusedCategory() throws Exception {
		Long id = createCategory("Para borrar");

		mvc.perform(delete("/api/admin/categories/{id}", id)).andExpect(status().isNoContent());
	}

	// --- Etiquetas ---

	@Test
	void createsAndRenamesATag() throws Exception {
		String json = mvc.perform(post("/api/admin/tags").contentType(MediaType.APPLICATION_JSON)
						.content("{ \"name\": \"Spring Boot\" }"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.slug").value("spring-boot"))
				.andReturn().getResponse().getContentAsString();
		Number id = JsonPath.read(json, "$.id");

		mvc.perform(put("/api/admin/tags/{id}", id.longValue()).contentType(MediaType.APPLICATION_JSON)
						.content("{ \"name\": \"Spring Framework\" }"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.slug").value("spring-framework"));
	}

	@Test
	void duplicateTagReturns409IgnoringCase() throws Exception {
		mvc.perform(post("/api/admin/tags").contentType(MediaType.APPLICATION_JSON).content("{ \"name\": \"JAVA\" }"))
				.andExpect(status().isConflict());
	}

	@Test
	void tagInUseCannotBeDeleted() throws Exception {
		Long java = jdbc.queryForObject("SELECT id FROM tags WHERE slug = 'java'", Long.class);

		mvc.perform(delete("/api/admin/tags/{id}", java)).andExpect(status().isConflict());
	}

	@Test
	void unknownIdsAreNotFound() throws Exception {
		mvc.perform(delete("/api/admin/categories/{id}", 999_999)).andExpect(status().isNotFound());
		mvc.perform(delete("/api/admin/tags/{id}", 999_999)).andExpect(status().isNotFound());
	}

	private Long createCategory(String name) throws Exception {
		String json = mvc.perform(post("/api/admin/categories").contentType(MediaType.APPLICATION_JSON)
						.content("{ \"name\": \"" + name + "\" }"))
				.andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString();
		Number id = JsonPath.read(json, "$.id");
		return id.longValue();
	}
}
