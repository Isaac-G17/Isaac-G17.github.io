package dev.isaacguzman.portfolio.post.infrastructure.out.persistence;

import dev.isaacguzman.portfolio.post.domain.PostSearch;
import dev.isaacguzman.portfolio.post.domain.PostStatus;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.data.jpa.domain.Specification;

/**
 * Arma la consulta de la lista pública solo con los filtros que llegan.
 * Cada Specification es un pedazo del WHERE; allOf los une con AND.
 */
final class PostSpecifications {

	private PostSpecifications() {
	}

	static Specification<PostJpaEntity> publishedMatching(PostSearch search, Instant now) {
		List<Specification<PostJpaEntity>> filters = new ArrayList<>();
		filters.add(visibleAt(now));
		if (search.categorySlug() != null) {
			filters.add(inCategory(search.categorySlug()));
		}
		if (search.tagSlug() != null) {
			filters.add(withTag(search.tagSlug()));
		}
		if (search.titleText() != null) {
			filters.add(titleContains(search.titleText()));
		}
		return Specification.allOf(filters);
	}

	/** Publicado y con fecha cumplida: así funcionan los artículos programados. */
	static Specification<PostJpaEntity> visibleAt(Instant now) {
		return (root, query, cb) -> cb.and(
				cb.equal(root.get("status"), PostStatus.PUBLISHED),
				cb.lessThanOrEqualTo(root.get("publishedAt"), now));
	}

	private static Specification<PostJpaEntity> inCategory(String categorySlug) {
		return (root, query, cb) -> cb.equal(root.get("category").get("slug"), categorySlug);
	}

	private static Specification<PostJpaEntity> withTag(String tagSlug) {
		// Un artículo tiene cada etiqueta una sola vez, así que el join no duplica filas.
		return (root, query, cb) -> cb.equal(root.join("tags").get("slug"), tagSlug);
	}

	private static Specification<PostJpaEntity> titleContains(String text) {
		String pattern = "%" + escapeLike(text.toLowerCase(Locale.ROOT)) + "%";
		return (root, query, cb) -> cb.like(cb.lower(root.get("title")), pattern, '\\');
	}

	/** Sin esto, buscar "100%" trataría el % como comodín en vez de como texto. */
	private static String escapeLike(String text) {
		return text.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
	}
}
