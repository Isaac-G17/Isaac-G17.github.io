package dev.isaacguzman.portfolio.shared.infrastructure.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import dev.isaacguzman.portfolio.TestcontainersConfiguration;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/** Comprueba que Swagger documenta todos los endpoints de la API, con sus esquemas y errores. */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class OpenApiDocumentationTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	@Qualifier("requestMappingHandlerMapping")
	private RequestMappingHandlerMapping mappings;

	private DocumentContext apiDocs;

	@BeforeEach
	void loadApiDocs() throws Exception {
		String json = mvc.perform(get("/v3/api-docs")).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		apiDocs = JsonPath.parse(json);
	}

	@Test
	void documentsEveryEndpointOfTheApi() {
		Set<String> registered = new HashSet<>();
		mappings.getHandlerMethods().keySet().forEach(info -> info.getPatternValues().stream()
				.filter(path -> path.startsWith("/api/"))
				.forEach(path -> info.getMethodsCondition().getMethods()
						.forEach(method -> registered.add(method + " " + path))));

		Set<String> documented = new HashSet<>();
		Map<String, Map<String, Object>> paths = apiDocs.read("$.paths");
		paths.forEach((path, operations) -> operations.keySet()
				.forEach(method -> documented.add(method.toUpperCase(Locale.ROOT) + " " + path)));

		assertThat(registered).isNotEmpty();
		assertThat(documented).containsExactlyInAnyOrderElementsOf(registered);
	}

	@Test
	@SuppressWarnings("unchecked")
	void everyOperationHasASectionASummaryAndASuccessResponse() {
		Map<String, Map<String, Map<String, Object>>> paths = apiDocs.read("$.paths");
		paths.forEach((path, operations) -> operations.forEach((method, operation) -> {
			String where = method.toUpperCase(Locale.ROOT) + " " + path;
			assertThat(operation.get("tags")).as("sección de %s", where).isNotNull();
			assertThat(operation.get("summary")).as("resumen de %s", where).isNotNull();
			// Si un método declara @ApiResponse de error, springdoc ya no agrega el 200 automático.
			Map<String, Object> responses = (Map<String, Object>) operation.get("responses");
			assertThat(responses.keySet()).as("respuesta 2xx de %s", where).anyMatch(code -> code.startsWith("2"));
		}));
	}

	@Test
	void documentsTheRequestSchemasAndTheErrorFormat() {
		Map<String, Object> schemas = apiDocs.read("$.components.schemas");
		assertThat(schemas).containsKeys("ProjectRequest", "PostRequest", "ProblemDetail");

		// Las meta-anotaciones de errores (@NotFoundResponse, etc.) llegan a la documentación.
		Map<String, Object> responses = apiDocs.read("$.paths['/api/public/projects/{slug}'].get.responses");
		assertThat(responses).containsKeys("200", "404");
		Map<String, Object> createResponses = apiDocs.read("$.paths['/api/admin/posts'].post.responses");
		assertThat(createResponses).containsKeys("201", "400", "409");
	}

	@Test
	void swaggerUiIsAvailable() throws Exception {
		mvc.perform(get("/swagger-ui/index.html")).andExpect(status().isOk());
	}
}
