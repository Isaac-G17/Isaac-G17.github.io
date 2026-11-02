package dev.isaacguzman.portfolio.project.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.util.ArrayList;
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

	private static Project project(ProjectStatus status, Instant publishedAt, List<String> technologies) {
		return new Project(1L, "Portafolio", new Slug("portafolio"), ProjectType.CODE, "Resumen", "",
				null, technologies, null, null, false, status, publishedAt, Instant.now(), Instant.now());
	}
}
