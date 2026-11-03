package dev.isaacguzman.portfolio.post.application.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.isaacguzman.portfolio.post.application.port.out.PostRepositoryPort;
import dev.isaacguzman.portfolio.post.domain.PostNotFoundException;
import dev.isaacguzman.portfolio.post.domain.PostSearch;
import dev.isaacguzman.portfolio.shared.application.PageQuery;
import dev.isaacguzman.portfolio.shared.application.PageResult;
import dev.isaacguzman.portfolio.shared.domain.Slug;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/** Prueba el caso de uso con un puerto falso: sin Spring y sin base de datos. */
class PostServiceTest {

	private static final Instant NOW = Instant.parse("2026-11-02T12:00:00Z");

	private final PostRepositoryPort repository = mock(PostRepositoryPort.class);
	private final PostService service = new PostService(repository, Clock.fixed(NOW, ZoneOffset.UTC));

	@Test
	void searchesWithTheCurrentTimeSoScheduledPostsStayHidden() {
		PostSearch search = new PostSearch("backend", null, null);
		PageQuery page = new PageQuery(0, 10);
		when(repository.findPublished(search, NOW, page)).thenReturn(new PageResult<>(List.of(), 0, 10, 0));

		service.getPublishedPosts(search, page);

		verify(repository).findPublished(search, NOW, page);
	}

	@Test
	void missingPostIsNotFound() {
		when(repository.findPublishedBySlug(new Slug("no-existe"), NOW)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.getPublishedPost("no-existe")).isInstanceOf(PostNotFoundException.class);
	}

	@Test
	void invalidSlugIsNotFoundWithoutQueryingTheRepository() {
		assertThatThrownBy(() -> service.getPublishedPost("Slug Invalido")).isInstanceOf(PostNotFoundException.class);

		verify(repository, never()).findPublishedBySlug(any(), eq(NOW));
	}
}
