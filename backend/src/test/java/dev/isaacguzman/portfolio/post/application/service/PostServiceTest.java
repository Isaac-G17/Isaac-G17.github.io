package dev.isaacguzman.portfolio.post.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.isaacguzman.portfolio.post.application.port.in.PostCommand;
import dev.isaacguzman.portfolio.post.application.port.out.CategoryRepositoryPort;
import dev.isaacguzman.portfolio.post.application.port.out.PostRepositoryPort;
import dev.isaacguzman.portfolio.post.application.port.out.TagRepositoryPort;
import dev.isaacguzman.portfolio.post.domain.Category;
import dev.isaacguzman.portfolio.post.domain.Post;
import dev.isaacguzman.portfolio.post.domain.PostNotFoundException;
import dev.isaacguzman.portfolio.post.domain.PostSearch;
import dev.isaacguzman.portfolio.post.domain.PostStatus;
import dev.isaacguzman.portfolio.post.domain.Tag;
import dev.isaacguzman.portfolio.shared.application.PageQuery;
import dev.isaacguzman.portfolio.shared.application.PageResult;
import dev.isaacguzman.portfolio.shared.domain.AlreadyExistsException;
import dev.isaacguzman.portfolio.shared.domain.InvalidDataException;
import dev.isaacguzman.portfolio.shared.domain.Slug;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/** Prueba los casos de uso con puertos falsos: sin Spring y sin base de datos. */
class PostServiceTest {

	private static final Instant NOW = Instant.parse("2026-11-02T12:00:00Z");
	private static final Category BACKEND = new Category(1L, "Backend", new Slug("backend"), null);

	private final PostRepositoryPort posts = mock(PostRepositoryPort.class);
	private final CategoryRepositoryPort categories = mock(CategoryRepositoryPort.class);
	private final TagRepositoryPort tags = mock(TagRepositoryPort.class);
	private final PostService service = new PostService(posts, categories, tags, Clock.fixed(NOW, ZoneOffset.UTC));

	// --- Sitio público ---

	@Test
	void searchesWithTheCurrentTimeSoScheduledPostsStayHidden() {
		PostSearch search = new PostSearch("backend", null, null);
		PageQuery page = new PageQuery(0, 10);
		when(posts.findPublished(search, NOW, page)).thenReturn(new PageResult<>(List.of(), 0, 10, 0));

		service.getPublishedPosts(search, page);

		verify(posts).findPublished(search, NOW, page);
	}

	@Test
	void missingPostIsNotFound() {
		when(posts.findPublishedBySlug(new Slug("no-existe"), NOW)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.getPublishedPost("no-existe")).isInstanceOf(PostNotFoundException.class);
	}

	@Test
	void invalidSlugIsNotFoundWithoutQueryingTheRepository() {
		assertThatThrownBy(() -> service.getPublishedPost("Slug Invalido")).isInstanceOf(PostNotFoundException.class);

		verify(posts, never()).findPublishedBySlug(any(), eq(NOW));
	}

	// --- Panel de administración ---

	@Test
	void createsADraftReusingExistingTagsAndCreatingNewOnes() {
		Tag java = new Tag(10L, "Java", new Slug("java"));
		when(categories.findById(1L)).thenReturn(Optional.of(BACKEND));
		when(tags.findBySlugs(anyCollection())).thenReturn(List.of(java));
		when(tags.save(any())).thenAnswer(invocation -> withId(invocation.getArgument(0), 11L));
		when(posts.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		// "java" y "JAVA" son la misma etiqueta que ya existe; "Spring Boot" es nueva.
		Post created = service.create(command("nuevo", 1L, List.of("java", "JAVA", "Spring Boot")));

		assertThat(created.status()).isEqualTo(PostStatus.DRAFT);
		assertThat(created.tags()).extracting(Tag::name).containsExactly("Java", "Spring Boot");
		verify(tags, times(1)).save(any());
	}

	@Test
	void unknownCategoryIsInvalidData() {
		when(categories.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.create(command("nuevo", 99L, List.of())))
				.isInstanceOf(InvalidDataException.class);
		verify(posts, never()).save(any());
	}

	@Test
	void duplicateSlugIsRejectedBeforeSaving() {
		when(posts.existsBySlug(new Slug("repetido"), null)).thenReturn(true);

		assertThatThrownBy(() -> service.create(command("repetido", 1L, List.of())))
				.isInstanceOf(AlreadyExistsException.class);
		verify(posts, never()).save(any());
	}

	@Test
	void publishingWithoutDateUsesNow() {
		when(posts.findById(1L)).thenReturn(Optional.of(draft()));
		when(posts.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		assertThat(service.publish(1L, null).publishedAt()).isEqualTo(NOW);
	}

	@Test
	void publishingWithAFutureDateSchedulesThePost() {
		Instant nextWeek = NOW.plusSeconds(7 * 24 * 3600);
		when(posts.findById(1L)).thenReturn(Optional.of(draft()));
		when(posts.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		Post scheduled = service.publish(1L, nextWeek);

		assertThat(scheduled.publishedAt()).isEqualTo(nextWeek);
		assertThat(scheduled.isScheduledAt(NOW)).isTrue();
	}

	@Test
	void deletingAMissingPostIsNotFound() {
		when(posts.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.delete(99L)).isInstanceOf(PostNotFoundException.class);
		verify(posts, never()).deleteById(any());
	}

	private static PostCommand command(String slug, Long categoryId, List<String> tagNames) {
		return new PostCommand("Título", new Slug(slug), "Extracto", "Contenido", null, categoryId, tagNames, false,
				null, null);
	}

	private static Post draft() {
		return new Post(1L, "Título", new Slug("titulo"), "Extracto", "", null, BACKEND, List.of(), 1,
				PostStatus.DRAFT, false, null, null, null, NOW, NOW);
	}

	private static Tag withId(Tag tag, Long id) {
		return new Tag(id, tag.name(), tag.slug());
	}
}
