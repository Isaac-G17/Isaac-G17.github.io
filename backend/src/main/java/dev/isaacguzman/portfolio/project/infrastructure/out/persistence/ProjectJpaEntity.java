package dev.isaacguzman.portfolio.project.infrastructure.out.persistence;

import dev.isaacguzman.portfolio.project.domain.ProjectStatus;
import dev.isaacguzman.portfolio.project.domain.ProjectType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/** Fila de la tabla projects. Vive en infraestructura; el dominio usa Project. */
@Entity
@Table(name = "projects")
public class ProjectJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;
	private String slug;

	@Enumerated(EnumType.STRING)
	private ProjectType type;

	private String summary;
	private String content;
	private String coverImageUrl;
	private String liveUrl;
	private String repoUrl;
	private boolean featured;

	@Enumerated(EnumType.STRING)
	private ProjectStatus status;

	private Instant publishedAt;
	private Instant createdAt;
	private Instant updatedAt;

	@ManyToMany
	@JoinTable(
			name = "project_technologies",
			joinColumns = @JoinColumn(name = "project_id"),
			inverseJoinColumns = @JoinColumn(name = "technology_id"))
	private Set<TechnologyJpaEntity> technologies = new HashSet<>();

	protected ProjectJpaEntity() {
		// Requerido por JPA
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getSlug() {
		return slug;
	}

	public ProjectType getType() {
		return type;
	}

	public String getSummary() {
		return summary;
	}

	public String getContent() {
		return content;
	}

	public String getCoverImageUrl() {
		return coverImageUrl;
	}

	public String getLiveUrl() {
		return liveUrl;
	}

	public String getRepoUrl() {
		return repoUrl;
	}

	public boolean isFeatured() {
		return featured;
	}

	public ProjectStatus getStatus() {
		return status;
	}

	public Instant getPublishedAt() {
		return publishedAt;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public Set<TechnologyJpaEntity> getTechnologies() {
		return technologies;
	}
}
