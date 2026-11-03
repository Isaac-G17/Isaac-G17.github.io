package dev.isaacguzman.portfolio.post.application.service;

import dev.isaacguzman.portfolio.post.application.port.in.GetPublishedPostsQuery;
import dev.isaacguzman.portfolio.post.application.port.out.PostRepositoryPort;
import dev.isaacguzman.portfolio.post.domain.Post;
import dev.isaacguzman.portfolio.post.domain.PostNotFoundException;
import dev.isaacguzman.portfolio.post.domain.PostSearch;
import dev.isaacguzman.portfolio.shared.application.PageQuery;
import dev.isaacguzman.portfolio.shared.application.PageResult;
import dev.isaacguzman.portfolio.shared.domain.Slug;
import java.time.Clock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostService implements GetPublishedPostsQuery {

	private final PostRepositoryPort posts;
	private final Clock clock;

	public PostService(PostRepositoryPort posts, Clock clock) {
		this.posts = posts;
		this.clock = clock;
	}

	@Override
	public PageResult<Post> getPublishedPosts(PostSearch search, PageQuery page) {
		return posts.findPublished(search, clock.instant(), page);
	}

	@Override
	public Post getPublishedPost(String slug) {
		if (!Slug.isValid(slug)) {
			throw PostNotFoundException.publishedWithSlug(slug);
		}
		return posts.findPublishedBySlug(new Slug(slug), clock.instant())
				.orElseThrow(() -> PostNotFoundException.publishedWithSlug(slug));
	}
}
