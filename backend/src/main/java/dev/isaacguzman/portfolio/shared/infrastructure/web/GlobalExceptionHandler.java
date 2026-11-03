package dev.isaacguzman.portfolio.shared.infrastructure.web;

import dev.isaacguzman.portfolio.shared.domain.ConflictException;
import dev.isaacguzman.portfolio.shared.domain.InvalidDataException;
import dev.isaacguzman.portfolio.shared.domain.NotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Convierte los errores en respuestas con formato ProblemDetail (RFC 9457).
 * Los errores propios de Spring MVC (validación, JSON mal formado, método no
 * permitido...) los resuelve la clase padre con el mismo formato.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(NotFoundException.class)
	public ProblemDetail handleNotFound(NotFoundException ex) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
		problem.setTitle("Recurso no encontrado");
		return problem;
	}

	@ExceptionHandler(InvalidDataException.class)
	public ProblemDetail handleInvalidData(InvalidDataException ex) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
		problem.setTitle("Datos inválidos");
		return problem;
	}

	@ExceptionHandler(ConflictException.class)
	public ProblemDetail handleConflict(ConflictException ex) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
		problem.setTitle("Conflicto con datos existentes");
		return problem;
	}

	/**
	 * Respaldo para conflictos que solo detecta la base de datos, por ejemplo dos peticiones
	 * que crean el mismo slug al mismo tiempo y las dos pasan la validación previa.
	 */
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ProblemDetail handleDataIntegrity(DataIntegrityViolationException ex) {
		log.warn("Restricción de la base de datos violada", ex);
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(
				HttpStatus.CONFLICT, "Los datos entran en conflicto con información existente.");
		problem.setTitle("Conflicto con datos existentes");
		return problem;
	}

	/** Errores de Bean Validation (@Valid): un 400 con el mensaje de cada campo en "errors". */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors().stream()
				.sorted(Comparator.comparing(error -> error.getField()))
				.map(error -> Map.of(
						"field", error.getField(),
						"message", String.valueOf(error.getDefaultMessage())))
				.toList();
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(
				HttpStatus.BAD_REQUEST, "Hay campos con datos inválidos.");
		problem.setTitle("Datos inválidos");
		problem.setProperty("errors", errors);
		return handleExceptionInternal(ex, problem, headers, HttpStatus.BAD_REQUEST, request);
	}

	/** Cualquier error no previsto: se registra completo, pero al cliente no se le dan detalles internos. */
	@ExceptionHandler(Exception.class)
	public ProblemDetail handleUnexpected(Exception ex) {
		log.error("Error no controlado", ex);
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(
				HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado. Intenta de nuevo más tarde.");
		problem.setTitle("Error interno");
		return problem;
	}
}
