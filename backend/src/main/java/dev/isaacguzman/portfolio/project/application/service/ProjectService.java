package dev.isaacguzman.portfolio.project.application.service;

import dev.isaacguzman.portfolio.project.application.port.in.GetPublishedProjectsQuery;
import dev.isaacguzman.portfolio.project.application.port.in.ManageProjectsUseCase;
import dev.isaacguzman.portfolio.project.application.port.out.ProjectRepositoryPort;
import dev.isaacguzman.portfolio.project.domain.DuplicateSlugException;
import dev.isaacguzman.portfolio.project.domain.Project;
import dev.isaacguzman.portfolio.project.domain.ProjectDetails;
import dev.isaacguzman.portfolio.project.domain.ProjectNotFoundException;
import dev.isaacguzman.portfolio.project.domain.ProjectType;
import dev.isaacguzman.portfolio.project.domain.Slug;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProjectService implements GetPublishedProjectsQuery, ManageProjectsUseCase {

	private final ProjectRepositoryPort projects;
	private final Clock clock;

	public ProjectService(ProjectRepositoryPort projects, Clock clock) {
		this.projects = projects;
		this.clock = clock;
	}

	// --- Sitio público ---

	@Override
	public List<Project> getPublishedProjects(ProjectType type) {
		return projects.findPublished(type);
	}

	@Override
	public Project getPublishedProject(String slug) {
		// Un slug con formato inválido no puede existir: se responde igual que si no existiera.
		if (!Slug.isValid(slug)) {
			throw ProjectNotFoundException.publishedWithSlug(slug);
		}
		return projects.findPublishedBySlug(new Slug(slug))
				.orElseThrow(() -> ProjectNotFoundException.publishedWithSlug(slug));
	}

	// --- Panel de administración ---

	@Override
	public List<Project> getAll() {
		return projects.findAll();
	}

	@Override
	public Project getById(Long id) {
		return projects.findById(id).orElseThrow(() -> ProjectNotFoundException.withId(id));
	}

	@Override
	@Transactional
	public Project create(ProjectDetails details) {
		ensureSlugIsFree(details.slug(), null);
		return projects.save(Project.draft(details, now()));
	}

	@Override
	@Transactional
	public Project update(Long id, ProjectDetails details) {
		Project current = getById(id);
		ensureSlugIsFree(details.slug(), id);
		return projects.save(current.update(details, now()));
	}

	@Override
	@Transactional
	public Project publish(Long id) {
		return projects.save(getById(id).publish(now()));
	}

	@Override
	@Transactional
	public void delete(Long id) {
		getById(id);
		projects.deleteById(id);
	}

	private void ensureSlugIsFree(Slug slug, Long exceptId) {
		if (projects.existsBySlug(slug, exceptId)) {
			throw new DuplicateSlugException(slug);
		}
	}

	private Instant now() {
		return clock.instant();
	}
}
