package dev.isaacguzman.portfolio;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 * PostgreSQL real en un contenedor de Docker para los tests de integración.
 * {@code @ServiceConnection} hace que Spring se conecte a él sin configurar URL ni contraseña.
 */
@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

	@Bean
	@ServiceConnection
	PostgreSQLContainer postgres() {
		// La misma versión que docker-compose.yml y Neon.
		return new PostgreSQLContainer("postgres:18.6-alpine");
	}
}
