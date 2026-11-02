package dev.isaacguzman.portfolio;

import static org.assertj.core.api.Assertions.assertThat;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

/**
 * Comprueba que las reglas de ArchitectureTest de verdad detectan una violación,
 * usando clases de ejemplo que rompen la arquitectura a propósito (paquete archfixture).
 */
class ArchitectureRulesSelfTest {

	private final JavaClasses fixtures = new ClassFileImporter()
			.importPackages("dev.isaacguzman.portfolio.archfixture");

	@Test
	void domainRuleFailsWhenDomainDependsOnSpring() {
		assertThat(ArchitectureTest.DOMAIN_IS_PURE.evaluate(fixtures).hasViolation()).isTrue();
	}

	@Test
	void applicationRuleFailsWhenApplicationDependsOnInfrastructure() {
		assertThat(ArchitectureTest.APPLICATION_DOES_NOT_DEPEND_ON_INFRASTRUCTURE.evaluate(fixtures).hasViolation())
				.isTrue();
	}
}
