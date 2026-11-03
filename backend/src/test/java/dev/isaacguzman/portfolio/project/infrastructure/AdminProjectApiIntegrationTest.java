package dev.isaacguzman.portfolio.project.infrastructure;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
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
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

/** Prueba la API de administración de proyectos de punta a punta con PostgreSQL real. */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@Sql("/sql/projects.sql")
class AdminProjectApiIntegrationTest {

	private static final String VALID = """
			{
			  "title": "Nuevo proyecto",
			  "slug": "nuevo-proyecto",
			  "type": "CODE",
			  "summary": "Un proyecto de prueba",
			  "content": "# Nuevo",
			  "technologies": ["Java", "angular"],
			  "repoUrl": "https://github.com/Isaac-G17/nuevo",
			  "featured": false
			}
			""";

	@Autowired
	private MockMvc mvc;

	@Test
	void listsProjectsInAnyStatus() throws Exception {
		mvc.perform(get("/api/admin/projects"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[*].status", hasItem("DRAFT")));
	}

	@Test
	void createsADraftAndReusesExistingTechnologies() throws Exception {
		mvc.perform(post("/api/admin/projects").contentType(MediaType.APPLICATION_JSON).content(VALID))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", startsWith("/api/admin/projects/")))
				.andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.status").value("DRAFT"))
				.andExpect(jsonPath("$.publishedAt", nullValue()))
				// "angular" en minúsculas reutiliza la tecnología "Angular" que ya existe.
				.andExpect(jsonPath("$.technologies", contains("Angular", "Java")));

		// Un borrador no aparece en la API pública.
		mvc.perform(get("/api/public/projects/nuevo-proyecto")).andExpect(status().isNotFound());
	}

	@Test
	void invalidDataReturns400WithTheDetailOfEachField() throws Exception {
		String invalid = """
				{ "title": "", "slug": "Con Espacios", "summary": "Resumen", "repoUrl": "no-es-url" }
				""";
		mvc.perform(post("/api/admin/projects").contentType(MediaType.APPLICATION_JSON).content(invalid))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.title").value("Datos inválidos"))
				.andExpect(jsonPath("$.errors[*].field", contains("repoUrl", "slug", "title", "type")));
	}

	@Test
	void duplicateSlugReturns409() throws Exception {
		String duplicate = VALID.replace("nuevo-proyecto", "portafolio");
		mvc.perform(post("/api/admin/projects").contentType(MediaType.APPLICATION_JSON).content(duplicate))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.status").value(409));
	}

	@Test
	void updatingToAnotherProjectsSlugReturns409() throws Exception {
		Long id = idOf("tienda-ejemplo");
		String toExisting = VALID.replace("nuevo-proyecto", "portafolio");
		mvc.perform(put("/api/admin/projects/{id}", id).contentType(MediaType.APPLICATION_JSON).content(toExisting))
				.andExpect(status().isConflict());
	}

	@Test
	void updatesTheProjectAndKeepsItsStatus() throws Exception {
		Long id = idOf("portafolio");
		String changes = VALID.replace("nuevo-proyecto", "portafolio").replace("Nuevo proyecto", "Portafolio v2");
		mvc.perform(put("/api/admin/projects/{id}", id).contentType(MediaType.APPLICATION_JSON).content(changes))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Portafolio v2"))
				.andExpect(jsonPath("$.status").value("PUBLISHED"));
	}

	@Test
	void publishingMakesTheProjectPublic() throws Exception {
		Long id = idOf("borrador");
		mvc.perform(post("/api/admin/projects/{id}/publish", id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("PUBLISHED"))
				.andExpect(jsonPath("$.publishedAt", notNullValue()));

		mvc.perform(get("/api/public/projects/borrador")).andExpect(status().isOk());
	}

	@Test
	void unpublishingHidesTheProjectButKeepsItInTheAdmin() throws Exception {
		Long id = idOf("portafolio");
		mvc.perform(post("/api/admin/projects/{id}/unpublish", id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("DRAFT"))
				.andExpect(jsonPath("$.publishedAt", nullValue()));

		mvc.perform(get("/api/public/projects/portafolio")).andExpect(status().isNotFound());
		mvc.perform(get("/api/public/projects")).andExpect(jsonPath("$[*].slug", contains("tienda-ejemplo")));
		mvc.perform(get("/api/admin/projects/{id}", id)).andExpect(status().isOk());
	}

	@Test
	void deletesTheProject() throws Exception {
		Long id = idOf("tienda-ejemplo");
		mvc.perform(delete("/api/admin/projects/{id}", id)).andExpect(status().isNoContent());
		mvc.perform(get("/api/admin/projects/{id}", id)).andExpect(status().isNotFound());
	}

	@Test
	void unknownIdIsNotFound() throws Exception {
		mvc.perform(get("/api/admin/projects/{id}", 999_999)).andExpect(status().isNotFound());
		mvc.perform(delete("/api/admin/projects/{id}", 999_999)).andExpect(status().isNotFound());
	}

	/** Busca el id de un proyecto de los datos de ejemplo a partir de su slug. */
	private Long idOf(String slug) throws Exception {
		String json = mvc.perform(get("/api/admin/projects")).andReturn().getResponse().getContentAsString();
		// Un filtro [?(...)] de JsonPath siempre devuelve una lista, aunque haya un solo resultado.
		List<Number> ids = JsonPath.read(json, "$[?(@.slug == '" + slug + "')].id");
		return ids.getFirst().longValue();
	}
}
