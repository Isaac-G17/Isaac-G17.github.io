package dev.isaacguzman.portfolio.post.application.service;

import dev.isaacguzman.portfolio.post.application.port.in.GetPublishedPostsQuery;
import dev.isaacguzman.portfolio.post.application.port.in.ManagePostsUseCase;
import dev.isaacguzman.portfolio.post.application.port.in.PostCommand;
import dev.isaacguzman.portfolio.post.application.port.out.CategoryRepositoryPort;
import dev.isaacguzman.portfolio.post.application.port.out.PostRepositoryPort;
import dev.isaacguzman.portfolio.post.application.port.out.TagRepositoryPort;
import dev.isaacguzman.portfolio.post.domain.Category;
import dev.isaacguzman.portfolio.post.domain.Post;
import dev.isaacguzman.portfolio.post.domain.PostDetails;
import dev.isaacguzman.portfolio.post.domain.PostNotFoundException;
import dev.isaacguzman.portfolio.post.domain.PostSearch;
import dev.isaacguzman.portfolio.post.domain.Tag;
import dev.isaacguzman.portfolio.shared.application.PageQuery;
import dev.isaacguzman.portfolio.shared.application.PageResult;
import dev.isaacguzman.portfolio.shared.domain.AlreadyExistsException;
import dev.isaacguzman.portfolio.shared.domain.InvalidDataException;
import dev.isaacguzman.portfolio.shared.domain.Slug;
import java.time.Clock;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostService implements GetPublishedPostsQuery, ManagePostsUseCase {

	private final PostRepositoryPort posts;
	private final CategoryRepositoryPort categories;
	private final TagRepositoryPort tags;
	private final Clock clock;

	public PostService(PostRepositoryPort posts, CategoryRepositoryPort categories, TagRepositoryPort tags,
			Clock clock) {
		this.posts = posts;
		this.categories = categories;
		this.tags = tags;
		this.clock = clock;
	}

	// --- Sitio público ---

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

	// --- Panel de administración ---

	@Override
	public PageResult<Post> getAll(PageQuery page) {
		return posts.findAll(page);
	}

	@Override
	public Post getById(Long id) {
		return posts.findById(id).orElseThrow(() -> PostNotFoundException.withId(id));
	}

	@Override
	@Transactional
	public Post create(PostCommand command) {
		ensureSlugIsFree(command.slug(), null);
		return posts.save(Post.draft(toDetails(command), clock.instant()));
	}

	@Override
	@Transactional
	public Post update(Long id, PostCommand command) {
		Post current = getById(id);
		ensureSlugIsFree(command.slug(), id);
		return posts.save(current.update(toDetails(command), clock.instant()));
	}

	@Override
	@Transactional
	public Post publish(Long id, Instant publicationDate) {
		Instant now = clock.instant();
		return posts.save(getById(id).publishAt(publicationDate != null ? publicationDate : now, now));
	}

	@Override
	@Transactional
	public Post unpublish(Long id) {
		return posts.save(getById(id).unpublish(clock.instant()));
	}

	@Override
	@Transactional
	public void delete(Long id) {
		getById(id);
		posts.deleteById(id);
	}

	private void ensureSlugIsFree(Slug slug, Long exceptId) {
		if (posts.existsBySlug(slug, exceptId)) {
			throw new AlreadyExistsException("un artículo con el slug", slug.value());
		}
	}

	private PostDetails toDetails(PostCommand command) {
		Category category = categories.findById(command.categoryId())
				.orElseThrow(() -> new InvalidDataException("No existe la categoría con id " + command.categoryId()));
		return new PostDetails(command.title(), command.slug(), command.excerpt(), command.content(),
				command.coverImageUrl(), category, resolveTags(command.tagNames()), command.featured(),
				command.seoTitle(), command.seoDescription());
	}

	/**
	 * Convierte los nombres en etiquetas: reutiliza las que ya existen (comparando por slug, así
	 * "Spring Boot" y "spring boot" son la misma) y crea las nuevas.
	 */
	private List<Tag> resolveTags(List<String> names) {
		Map<Slug, Tag> requested = names.stream()
				.filter(name -> name != null && !name.isBlank())
				.map(Tag::named)
				.collect(Collectors.toMap(Tag::slug, Function.identity(), (first, repeated) -> first,
						LinkedHashMap::new));
		Map<Slug, Tag> existing = tags.findBySlugs(requested.keySet()).stream()
				.collect(Collectors.toMap(Tag::slug, Function.identity()));
		return requested.values().stream()
				.map(tag -> existing.containsKey(tag.slug()) ? existing.get(tag.slug()) : tags.save(tag))
				.toList();
	}
}
