package dev.isaacguzman.portfolio.post.application.port.out;

import dev.isaacguzman.portfolio.post.domain.Post;
import dev.isaacguzman.portfolio.post.domain.PostSearch;
import dev.isaacguzman.portfolio.shared.application.PageQuery;
import dev.isaacguzman.portfolio.shared.application.PageResult;
import dev.isaacguzman.portfolio.shared.domain.Slug;
import java.time.Instant;
import java.util.Optional;

/** Lo que la aplicación necesita de la base de datos de artículos. */
public interface PostRepositoryPort {

	/** Publicados con fecha {@code <= now}, del más reciente al más antiguo. */
	PageResult<Post> findPublished(PostSearch search, Instant now, PageQuery page);

	Optional<Post> findPublishedBySlug(Slug slug, Instant now);
}
