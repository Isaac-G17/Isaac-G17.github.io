package dev.isaacguzman.portfolio.project.application.service;

import dev.isaacguzman.portfolio.project.application.port.in.GetPublishedProjectsQuery;
import dev.isaacguzman.portfolio.project.application.port.out.ProjectRepositoryPort;
import dev.isaacguzman.portfolio.project.domain.Project;
import dev.isaacguzman.portfolio.project.domain.ProjectNotFoundException;
import dev.isaacguzman.portfolio.project.domain.ProjectType;
import dev.isaacguzman.portfolio.project.domain.Slug;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProjectService implements GetPublishedProjectsQuery {

	private final ProjectRepositoryPort projects;

	public ProjectService(ProjectRepositoryPort projects) {
		this.projects = projects;
	}

	@Override
	public List<Project> getPublishedProjects(ProjectType type) {
		return projects.findPublished(type);
	}

	@Override
	public Project getPublishedProject(String slug) {
		// Un slug con formato inválido no puede existir: se responde igual que si no existiera.
		if (!Slug.isValid(slug)) {
			throw new ProjectNotFoundException(slug);
		}
		return projects.findPublishedBySlug(new Slug(slug))
				.orElseThrow(() -> new ProjectNotFoundException(slug));
	}
}
