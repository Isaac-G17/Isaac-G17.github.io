package dev.isaacguzman.portfolio.post.infrastructure.in.web;

import dev.isaacguzman.portfolio.post.application.port.in.PostCommand;
import dev.isaacguzman.portfolio.post.domain.Category;
import dev.isaacguzman.portfolio.post.domain.Post;
import dev.isaacguzman.portfolio.post.domain.Tag;
import dev.isaacguzman.portfolio.post.infrastructure.in.web.dto.AdminResponses.CategoryAdminResponse;
import dev.isaacguzman.portfolio.post.infrastructure.in.web.dto.AdminResponses.PostAdminResponse;
import dev.isaacguzman.portfolio.post.infrastructure.in.web.dto.AdminResponses.TagAdminResponse;
import dev.isaacguzman.portfolio.post.infrastructure.in.web.dto.PostRequest;
import dev.isaacguzman.portfolio.shared.domain.Slug;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/** Conversiones del panel del blog (las del sitio público están en PostWebMapper). */
@Mapper
interface PostAdminWebMapper {

	/** {@code scheduled} se calcula en el controlador porque depende de la hora actual. */
	@Mapping(target = "slug", source = "post.slug.value")
	PostAdminResponse toAdmin(Post post, boolean scheduled);

	@Mapping(target = "slug", source = "slug.value")
	CategoryAdminResponse toAdmin(Category category);

	@Mapping(target = "slug", source = "slug.value")
	TagAdminResponse toAdmin(Tag tag);

	@Mapping(target = "tagNames", source = "tags")
	PostCommand toCommand(PostRequest request);

	/** Slug opcional: si no viene, el dominio lo genera del nombre. */
	default Slug toSlug(String value) {
		return value == null || value.isBlank() ? null : new Slug(value);
	}
}
