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

	@Test
	void draftCalculatesTheReadingTimeFromTheContent() {
		Post draft = Post.draft(details("palabra ".repeat(ReadingTime.WORDS_PER_MINUTE * 3)), NOW);

		assertThat(draft.status()).isEqualTo(PostStatus.DRAFT);
		assertThat(draft.readingTimeMinutes()).isEqualTo(3);
		assertThat(draft.publishedAt()).isNull();
	}

	@Test
	void updatingRecalculatesTheReadingTimeAndKeepsTheStatus() {
		Post published = Post.draft(details("corto"), NOW).publishAt(NOW, NOW);

		Post updated = published.update(details("palabra ".repeat(ReadingTime.WORDS_PER_MINUTE * 2)), NOW);

		assertThat(updated.readingTimeMinutes()).isEqualTo(2);
		assertThat(updated.status()).isEqualTo(PostStatus.PUBLISHED);
		assertThat(updated.publishedAt()).isEqualTo(NOW);
	}

	@Test
	void publishingWithAFutureDateSchedulesThePost() {
		Instant nextWeek = NOW.plusSeconds(7 * 24 * 3600);

		Post scheduled = Post.draft(details(""), NOW).publishAt(nextWeek, NOW);

		assertThat(scheduled.isScheduledAt(NOW)).isTrue();
		assertThat(scheduled.isVisibleAt(NOW)).isFalse();
		assertThat(scheduled.isVisibleAt(nextWeek)).isTrue();
	}

	@Test
	void unpublishingCancelsTheSchedule() {
		Post scheduled = Post.draft(details(""), NOW).publishAt(NOW.plusSeconds(60), NOW);

		Post draft = scheduled.unpublish(NOW);

		assertThat(draft.status()).isEqualTo(PostStatus.DRAFT);
		assertThat(draft.publishedAt()).isNull();
	}

	@Test
	void newTagsGetTheirSlugFromTheName() {
		assertThat(Tag.named(" Spring Boot ").name()).isEqualTo("Spring Boot");
		assertThat(Tag.named("Spring Boot").slug()).isEqualTo(new Slug("spring-boot"));
	}

	@Test
	void categoryWithoutSlugGetsItFromTheName() {
		assertThat(Category.create("Mi camino", null, null).slug()).isEqualTo(new Slug("mi-camino"));
		assertThat(Category.create("Mi camino", new Slug("camino"), null).slug()).isEqualTo(new Slug("camino"));
	}

	private static PostDetails details(String content) {
		Category category = new Category(1L, "Backend", new Slug("backend"), null);
		return new PostDetails("Título", new Slug("titulo"), "Extracto", content, null, category, List.of(), false,
				null, null);
	}

	private static Post post(PostStatus status, Instant publishedAt, String seoTitle, String seoDescription) {
		Category category = new Category(1L, "Backend", new Slug("backend"), null);
		return new Post(1L, "Título", new Slug("titulo"), "Extracto", "", null, category, List.of(), 1, status,
				false, seoTitle, seoDescription, publishedAt, NOW, NOW);
	}
}
