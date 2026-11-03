package dev.isaacguzman.portfolio.post.infrastructure.in.web;

import dev.isaacguzman.portfolio.post.domain.Category;
import dev.isaacguzman.portfolio.post.domain.Post;
import dev.isaacguzman.portfolio.post.domain.Tag;
import dev.isaacguzman.portfolio.post.infrastructure.in.web.dto.PostResponses.CategoryResponse;
import dev.isaacguzman.portfolio.post.infrastructure.in.web.dto.PostResponses.PostDetailResponse;
import dev.isaacguzman.portfolio.post.infrastructure.in.web.dto.PostResponses.PostSummaryResponse;
import dev.isaacguzman.portfolio.post.infrastructure.in.web.dto.PostResponses.TagResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
interface PostWebMapper {

	@Mapping(target = "slug", source = "slug.value")
	PostSummaryResponse toSummary(Post post);

	// El SEO sale de effectiveSeo*(): si no hay uno propio, se usan el título y el extracto.
	@Mapping(target = "slug", source = "slug.value")
	@Mapping(target = "seoTitle", expression = "java(post.effectiveSeoTitle())")
	@Mapping(target = "seoDescription", expression = "java(post.effectiveSeoDescription())")
	PostDetailResponse toDetail(Post post);

	@Mapping(target = "slug", source = "slug.value")
	CategoryResponse toResponse(Category category);

	@Mapping(target = "slug", source = "slug.value")
	TagResponse toResponse(Tag tag);
}
