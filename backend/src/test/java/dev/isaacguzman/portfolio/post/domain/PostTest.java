package dev.isaacguzman.portfolio.post.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.isaacguzman.portfolio.shared.domain.Slug;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

class PostTest {

	private static final Instant NOW = Instant.parse("2026-11-02T12:00:00Z");

	@Test
	void scheduledPostIsNotVisibleUntilItsDate() {
		Post scheduled = post(PostStatus.PUBLISHED, NOW.plusSeconds(60), null, null);

		assertThat(scheduled.isVisibleAt(NOW)).isFalse();
		assertThat(scheduled.isVisibleAt(NOW.plusSeconds(60))).isTrue();
	}

	@Test
	void draftIsNeverVisible() {
		assertThat(post(PostStatus.DRAFT, null, null, null).isVisibleAt(NOW)).isFalse();
	}

	@Test
	void publishedPostRequiresPublicationDate() {
		assertThatThrownBy(() -> post(PostStatus.PUBLISHED, null, null, null))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void seoFallsBackToTitleAndExcerpt() {
		Post withoutSeo = post(PostStatus.DRAFT, null, " ", null);

		assertThat(withoutSeo.effectiveSeoTitle()).isEqualTo("Título");
		assertThat(withoutSeo.effectiveSeoDescription()).isEqualTo("Extracto");
	}

	@Test
	void ownSeoValuesWin() {
		Post withSeo = post(PostStatus.DRAFT, null, "Título SEO", "Descripción SEO");

		assertThat(withSeo.effectiveSeoTitle()).isEqualTo("Título SEO");
		assertThat(withSeo.effectiveSeoDescription()).isEqualTo("Descripción SEO");
	}

	@Test
	void searchTurnsBlankFiltersIntoNoFilter() {
		PostSearch search = new PostSearch(" ", "", "  spring ");

		assertThat(search.categorySlug()).isNull();
		assertThat(search.tagSlug()).isNull();
		assertThat(search.titleText()).isEqualTo("spring");
	}

	private static Post post(PostStatus status, Instant publishedAt, String seoTitle, String seoDescription) {
		Category category = new Category(1L, "Backend", new Slug("backend"), null);
		return new Post(1L, "Título", new Slug("titulo"), "Extracto", "", null, category, List.of(), 1, status,
				false, seoTitle, seoDescription, publishedAt, NOW, NOW);
	}
}
