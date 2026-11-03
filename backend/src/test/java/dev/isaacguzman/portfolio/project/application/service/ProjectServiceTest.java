package dev.isaacguzman.portfolio.project.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.isaacguzman.portfolio.project.application.port.out.ProjectRepositoryPort;
import dev.isaacguzman.portfolio.project.domain.DuplicateSlugException;
import dev.isaacguzman.portfolio.project.domain.Project;
import dev.isaacguzman.portfolio.project.domain.ProjectDetails;
import dev.isaacguzman.portfolio.project.domain.ProjectNotFoundException;
import dev.isaacguzman.portfolio.project.domain.ProjectStatus;
import dev.isaacguzman.portfolio.project.domain.ProjectType;
import dev.isaacguzman.portfolio.shared.domain.Slug;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/** Prueba los casos de uso con un puerto falso: sin Spring y sin base de datos. */
class ProjectServiceTest {

	private static final Instant NOW = Instant.parse("2026-11-02T12:00:00Z");

	private final ProjectRepositoryPort repository = mock(ProjectRepositoryPort.class);
	private final ProjectService service = new ProjectService(repository, Clock.fixed(NOW, ZoneOffset.UTC));

	// --- Sitio público ---

	@Test
	void passesTheTypeFilterToTheRepository() {
		Project project = publishedProject(1L, "app");
		when(repository.findPublished(ProjectType.CODE)).thenReturn(List.of(project));

		assertThat(service.getPublishedProjects(ProjectType.CODE)).containsExactly(project);
	}

	@Test
	void returnsThePublishedProjectBySlug() {
		Project project = publishedProject(1L, "app");
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

	// --- Panel de administración ---

	@Test
	void createsTheProjectAsDraft() {
		when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		Project created = service.create(details("nuevo"));

		assertThat(created.status()).isEqualTo(ProjectStatus.DRAFT);
		assertThat(created.publishedAt()).isNull();
		assertThat(created.createdAt()).isEqualTo(NOW);
	}

	@Test
	void doesNotCreateAProjectWithADuplicateSlug() {
		when(repository.existsBySlug(new Slug("repetido"), null)).thenReturn(true);

		assertThatThrownBy(() -> service.create(details("repetido")))
				.isInstanceOf(DuplicateSlugException.class);

		verify(repository, never()).save(any());
	}

	@Test
	void updatingIgnoresItsOwnSlugButRejectsAnotherProjectsSlug() {
		when(repository.findById(1L)).thenReturn(Optional.of(publishedProject(1L, "app")));
		when(repository.existsBySlug(new Slug("de-otro"), 1L)).thenReturn(true);

		assertThatThrownBy(() -> service.update(1L, details("de-otro")))
				.isInstanceOf(DuplicateSlugException.class);
	}

	@Test
	void updatingKeepsTheStatusAndPublicationDate() {
		Project current = publishedProject(1L, "app");
		when(repository.findById(1L)).thenReturn(Optional.of(current));
		when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		Project updated = service.update(1L, details("app"));

		assertThat(updated.status()).isEqualTo(ProjectStatus.PUBLISHED);
		assertThat(updated.publishedAt()).isEqualTo(current.publishedAt());
		assertThat(updated.updatedAt()).isEqualTo(NOW);
	}

	@Test
	void publishingSetsThePublicationDate() {
		Project draft = Project.draft(details("borrador"), Instant.parse("2026-10-01T00:00:00Z"));
		when(repository.findById(1L)).thenReturn(Optional.of(draft));
		when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		Project published = service.publish(1L);

		assertThat(published.isPublished()).isTrue();
		assertThat(published.publishedAt()).isEqualTo(NOW);
	}

	@Test
	void unpublishingReturnsTheProjectToDraft() {
		when(repository.findById(1L)).thenReturn(Optional.of(publishedProject(1L, "app")));
		when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		Project draft = service.unpublish(1L);

		assertThat(draft.status()).isEqualTo(ProjectStatus.DRAFT);
		assertThat(draft.publishedAt()).isNull();
		assertThat(draft.updatedAt()).isEqualTo(NOW);
	}

	@Test
	void deletingAMissingProjectIsNotFound() {
		when(repository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.delete(99L)).isInstanceOf(ProjectNotFoundException.class);

		verify(repository, never()).deleteById(any());
	}

	private static ProjectDetails details(String slug) {
		return new ProjectDetails("Título", new Slug(slug), ProjectType.CODE, "Resumen", "", null,
				List.of("Java"), null, null, false);
	}

	private static Project publishedProject(Long id, String slug) {
		return new Project(id, "App", new Slug(slug), ProjectType.CODE, "Resumen", "", null, List.of(),
				null, null, false, ProjectStatus.PUBLISHED, Instant.parse("2026-09-01T00:00:00Z"),
				Instant.parse("2026-09-01T00:00:00Z"), Instant.parse("2026-09-01T00:00:00Z"));
	}
}
