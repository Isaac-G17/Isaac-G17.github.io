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

	/** Todos, en cualquier estado, del editado más recientemente al más antiguo. */
	PageResult<Post> findAll(PageQuery page);

	Optional<Post> findById(Long id);

	/** {@code exceptId} permite ignorar el propio artículo al editarlo; puede ser null. */
	boolean existsBySlug(Slug slug, Long exceptId);

	/** Crea el artículo si no tiene id, o lo actualiza si ya existe. Devuelve la versión guardada. */
	Post save(Post post);

	void deleteById(Long id);
}
