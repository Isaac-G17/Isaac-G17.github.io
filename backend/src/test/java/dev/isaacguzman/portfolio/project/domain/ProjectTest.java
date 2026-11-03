package dev.isaacguzman.portfolio.project.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class ProjectTest {

	@Test
	void publishedProjectRequiresPublicationDate() {
		assertThatThrownBy(() -> project(ProjectStatus.PUBLISHED, null, List.of()))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void draftProjectDoesNotNeedPublicationDate() {
		assertThat(project(ProjectStatus.DRAFT, null, List.of()).isPublished()).isFalse();
	}

	@Test
	void technologiesAreCopiedSoTheProjectCannotChangeFromOutside() {
		List<String> technologies = new ArrayList<>(List.of("Angular"));
		Project project = project(ProjectStatus.PUBLISHED, Instant.now(), technologies);

		technologies.add("Spring Boot");

		assertThat(project.technologies()).containsExactly("Angular");
		assertThat(project.isPublished()).isTrue();
	}

	@Test
	void missingTechnologiesBecomeAnEmptyList() {
		assertThat(project(ProjectStatus.DRAFT, null, null).technologies()).isEmpty();
	}

	@Test
	void publishingTwiceKeepsTheFirstPublicationDate() {
		Instant first = Instant.parse("2026-10-01T00:00:00Z");
		Project published = project(ProjectStatus.DRAFT, null, List.of()).publish(first);

		Project again = published.publish(Instant.parse("2026-11-01T00:00:00Z"));

		assertThat(again.publishedAt()).isEqualTo(first);
	}

	@Test
	void unpublishingReturnsToDraftWithoutPublicationDate() {
		Project published = project(ProjectStatus.PUBLISHED, Instant.parse("2026-10-01T00:00:00Z"), List.of());

		Project draft = published.unpublish(Instant.parse("2026-11-01T00:00:00Z"));

		assertThat(draft.status()).isEqualTo(ProjectStatus.DRAFT);
		assertThat(draft.publishedAt()).isNull();
	}

	@Test
	void unpublishingADraftChangesNothing() {
		Project draft = project(ProjectStatus.DRAFT, null, List.of());

		assertThat(draft.unpublish(Instant.now())).isSameAs(draft);
	}

	@Test
	void detailsNormalizeTechnologyNames() {
		ProjectDetails details = new ProjectDetails("Título", new Slug("app"), ProjectType.CODE, "Resumen", null,
				null, Arrays.asList(" Angular ", "angular", "", null, "Java"), null, null, false);

		assertThat(details.technologies()).containsExactly("Angular", "Java");
		assertThat(details.content()).isEmpty();
	}

	private static Project project(ProjectStatus status, Instant publishedAt, List<String> technologies) {
		return new Project(1L, "Portafolio", new Slug("portafolio"), ProjectType.CODE, "Resumen", "",
				null, technologies, null, null, false, status, publishedAt, Instant.now(), Instant.now());
	}
}
