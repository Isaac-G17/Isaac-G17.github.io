package dev.isaacguzman.portfolio.post.infrastructure.out.persistence;

import dev.isaacguzman.portfolio.post.domain.Category;
import dev.isaacguzman.portfolio.post.domain.Post;
import dev.isaacguzman.portfolio.post.domain.Tag;
import dev.isaacguzman.portfolio.shared.domain.Slug;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
interface PostPersistenceMapper {

	// MapStruct confunde Post.unpublish(Instant) y Tag.rename(String) con "setters fluidos" (reciben un
	// valor y devuelven el mismo tipo). No son campos: se ignoran para que no avise.
	@Mapping(target = "unpublish", ignore = true)
	Post toDomain(PostJpaEntity entity);

	Category toDomain(CategoryJpaEntity entity);

	@Mapping(target = "rename", ignore = true)
	Tag toDomain(TagJpaEntity entity);

	/** La categoría y las etiquetas las resuelve el adaptador; el id lo genera la base de datos. */
	@Mapping(target = "category", ignore = true)
	@Mapping(target = "tags", ignore = true)
	void copyToEntity(Post post, @MappingTarget PostJpaEntity entity);

	void copyToEntity(Category category, @MappingTarget CategoryJpaEntity entity);

	void copyToEntity(Tag tag, @MappingTarget TagJpaEntity entity);

	default Slug toSlug(String value) {
		return new Slug(value);
	}

	default String fromSlug(Slug slug) {
		return slug.value();
	}

	/** En orden alfabético para que la respuesta sea estable. */
	default List<Tag> toTags(Set<TagJpaEntity> tags) {
		return tags.stream()
				.map(this::toDomain)
				.sorted(Comparator.comparing(Tag::name, String.CASE_INSENSITIVE_ORDER))
				.toList();
	}
}
