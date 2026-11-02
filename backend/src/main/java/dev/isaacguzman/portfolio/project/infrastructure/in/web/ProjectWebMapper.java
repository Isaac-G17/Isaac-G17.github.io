package dev.isaacguzman.portfolio.project.infrastructure.in.web;

import dev.isaacguzman.portfolio.project.domain.Project;
import dev.isaacguzman.portfolio.project.infrastructure.in.web.dto.ProjectDetailResponse;
import dev.isaacguzman.portfolio.project.infrastructure.in.web.dto.ProjectSummaryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
interface ProjectWebMapper {

	@Mapping(target = "slug", source = "slug.value")
	ProjectSummaryResponse toSummary(Project project);

	@Mapping(target = "slug", source = "slug.value")
	ProjectDetailResponse toDetail(Project project);
}
