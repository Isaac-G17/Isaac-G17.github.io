package dev.isaacguzman.portfolio.project.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.isaacguzman.portfolio.project.application.port.out.ProjectRepositoryPort;
import dev.isaacguzman.portfolio.project.domain.Project;
import dev.isaacguzman.portfolio.project.domain.ProjectNotFoundException;
import dev.isaacguzman.portfolio.project.domain.ProjectStatus;
import dev.isaacguzman.portfolio.project.domain.ProjectType;
import dev.isaacguzman.portfolio.project.domain.Slug;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/** Prueba el caso de uso con un puerto falso: sin Spring y sin base de datos. */
class ProjectServiceTest {

	private final ProjectRepositoryPort repository = mock(ProjectRepositoryPort.class);
	private final ProjectService service = new ProjectService(repository);

	@Test
	void passesTheTypeFilterToTheRepository() {
		Project project = publishedProject("app");
		when(repository.findPublished(ProjectType.CODE)).thenReturn(List.of(project));

		assertThat(service.getPublishedProjects(ProjectType.CODE)).containsExactly(project);
	}

	@Test
	void returnsThePublishedProjectBySlug() {
		Project project = publishedProject("app");
		when(repository.findPublishedBySlug(new Slug("app"))).thenReturn(Optional.of(project));

		assertThat(service.getPublishedProject("app")).isEqualTo(project);
	}

	@Test
	void throwsNotFoundWhenTheProjectDoesNotExist() {
		when(repository.findPublishedBySlug(any())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.getPublishedProject("no-existe"))
				.isInstanceOf(ProjectNotFoundException.class);
	}

	@Test
	void invalidSlugIsNotFoundWithoutQueryingTheRepository() {
		assertThatThrownBy(() -> service.getPublishedProject("Slug Invalido"))
				.isInstanceOf(ProjectNotFoundException.class);

		verify(repository, never()).findPublishedBySlug(any());
	}

	private static Project publishedProject(String slug) {
		return new Project(1L, "App", new Slug(slug), ProjectType.CODE, "Resumen", "", null, List.of(),
				null, null, false, ProjectStatus.PUBLISHED, Instant.now(), Instant.now(), Instant.now());
	}
}
