package dev.isaacguzman.portfolio;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Reglas de la arquitectura hexagonal (docs/04-arquitectura.md):
 * las dependencias siempre apuntan hacia adentro. Si alguien las rompe, el build falla.
 */
@AnalyzeClasses(packages = "dev.isaacguzman.portfolio", importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchitectureTest {

	@ArchTest
	public static final ArchRule DOMAIN_IS_PURE = noClasses()
			.that().resideInAPackage("..domain..")
			.should().dependOnClassesThat()
			.resideInAnyPackage("org.springframework..", "jakarta.persistence..", "..application..", "..infrastructure..")
			.because("el dominio es Java puro y no conoce frameworks ni otras capas");

	@ArchTest
	public static final ArchRule APPLICATION_DOES_NOT_DEPEND_ON_INFRASTRUCTURE = noClasses()
			.that().resideInAPackage("..application..")
			.should().dependOnClassesThat()
			.resideInAnyPackage("..infrastructure..", "jakarta.persistence..")
			.because("los casos de uso hablan con el exterior solo mediante puertos");
}
