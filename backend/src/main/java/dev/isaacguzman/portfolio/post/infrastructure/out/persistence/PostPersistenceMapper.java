package dev.isaacguzman.portfolio.post.infrastructure.out.persistence;

import dev.isaacguzman.portfolio.post.domain.Category;
import dev.isaacguzman.portfolio.post.domain.Post;
import dev.isaacguzman.portfolio.post.domain.Tag;
import dev.isaacguzman.portfolio.shared.domain.Slug;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper
interface PostPersistenceMapper {

	Post toDomain(PostJpaEntity entity);

	Category toDomain(CategoryJpaEntity entity);

	Tag toDomain(TagJpaEntity entity);

	default Slug toSlug(String value) {
		return new Slug(value);
	}

	/** En orden alfabético para que la respuesta sea estable. */
	default List<Tag> toTags(Set<TagJpaEntity> tags) {
		return tags.stream()
				.map(this::toDomain)
				.sorted(Comparator.comparing(Tag::name, String.CASE_INSENSITIVE_ORDER))
				.toList();
	}
}
