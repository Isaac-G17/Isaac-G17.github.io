package dev.isaacguzman.portfolio.post.application.port.in;

import dev.isaacguzman.portfolio.post.domain.Post;
import dev.isaacguzman.portfolio.post.domain.PostNotFoundException;
import dev.isaacguzman.portfolio.post.domain.PostSearch;
import dev.isaacguzman.portfolio.shared.application.PageQuery;
import dev.isaacguzman.portfolio.shared.application.PageResult;

/** Consultas del blog público: solo artículos publicados cuya fecha ya llegó. */
public interface GetPublishedPostsQuery {

	PageResult<Post> getPublishedPosts(PostSearch search, PageQuery page);

	/** @throws PostNotFoundException si no existe, es borrador o está programado para el futuro */
	Post getPublishedPost(String slug);
}
