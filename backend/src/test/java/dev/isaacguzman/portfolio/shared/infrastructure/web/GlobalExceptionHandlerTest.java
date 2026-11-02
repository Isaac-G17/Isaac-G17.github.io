package dev.isaacguzman.portfolio.shared.infrastructure.web;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.isaacguzman.portfolio.shared.domain.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

class GlobalExceptionHandlerTest {

	// Sin Spring completo: solo el controlador de prueba y el manejador de errores.
	private final MockMvc mvc = MockMvcBuilders.standaloneSetup(new ThrowingController())
			.setControllerAdvice(new GlobalExceptionHandler())
			.build();

	@Test
	void notFoundExceptionReturns404ProblemDetail() throws Exception {
		mvc.perform(get("/not-found"))
				.andExpect(status().isNotFound())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.title").value("Recurso no encontrado"))
				.andExpect(jsonPath("$.detail").value("No existe el proyecto demo"));
	}

	@Test
	void unexpectedErrorReturns500WithoutInternalDetails() throws Exception {
		mvc.perform(get("/boom"))
				.andExpect(status().isInternalServerError())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
				.andExpect(jsonPath("$.title").value("Error interno"))
				.andExpect(content().string(not(containsString("secreto interno"))));
	}

	@Test
	void springErrorsAlsoUseProblemDetail() throws Exception {
		mvc.perform(post("/boom"))
				.andExpect(status().isMethodNotAllowed())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
				.andExpect(jsonPath("$.status").value(405));
	}

	/** Al ser clase interna de un test, Spring Boot no la registra al escanear en otros tests. */
	@RestController
	static class ThrowingController {

		@GetMapping("/not-found")
		String notFound() {
			throw new DemoNotFoundException("No existe el proyecto demo");
		}

		@GetMapping("/boom")
		String boom() {
			throw new IllegalStateException("secreto interno");
		}
	}

	static class DemoNotFoundException extends NotFoundException {

		DemoNotFoundException(String message) {
			super(message);
		}
	}
}
