package dev.isaacguzman.portfolio.post.infrastructure.out.persistence;

import dev.isaacguzman.portfolio.post.domain.PostStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.BatchSize;

/** Fila de la tabla posts. Vive en infraestructura; el dominio usa Post. */
@Entity
@Table(name = "posts")
public class PostJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;
	private String slug;
	private String excerpt;
	private String content;
	private String coverImageUrl;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "category_id")
	private CategoryJpaEntity category;

	// Con paginación no se puede traer una colección en la misma consulta (Hibernate paginaría en
	// memoria). @BatchSize carga las etiquetas de toda la página en una sola consulta extra.
	@ManyToMany
	@JoinTable(
			name = "post_tags",
			joinColumns = @JoinColumn(name = "post_id"),
			inverseJoinColumns = @JoinColumn(name = "tag_id"))
	@BatchSize(size = 50)
	private Set<TagJpaEntity> tags = new HashSet<>();

	private int readingTimeMinutes;

	@Enumerated(EnumType.STRING)
	private PostStatus status;

	private boolean featured;
	private String seoTitle;
	private String seoDescription;
	private Instant publishedAt;
	private Instant createdAt;
	private Instant updatedAt;

	protected PostJpaEntity() {
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

	public String getExcerpt() {
		return excerpt;
	}

	public String getContent() {
		return content;
	}

	public String getCoverImageUrl() {
		return coverImageUrl;
	}

	public CategoryJpaEntity getCategory() {
		return category;
	}

	public Set<TagJpaEntity> getTags() {
		return tags;
	}

	public int getReadingTimeMinutes() {
		return readingTimeMinutes;
	}

	public PostStatus getStatus() {
		return status;
	}

	public boolean isFeatured() {
		return featured;
	}

	public String getSeoTitle() {
		return seoTitle;
	}

	public String getSeoDescription() {
		return seoDescription;
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

	// Setters: los usa PostPersistenceMapper para copiar los datos del dominio al guardar.

	public void setTitle(String title) {
		this.title = title;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setCoverImageUrl(String coverImageUrl) {
		this.coverImageUrl = coverImageUrl;
	}

	public void setCategory(CategoryJpaEntity category) {
		this.category = category;
	}

	public void setTags(Set<TagJpaEntity> tags) {
		this.tags = tags;
	}

	public void setReadingTimeMinutes(int readingTimeMinutes) {
		this.readingTimeMinutes = readingTimeMinutes;
	}

	public void setStatus(PostStatus status) {
		this.status = status;
	}

	public void setFeatured(boolean featured) {
		this.featured = featured;
	}

	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	public void setPublishedAt(Instant publishedAt) {
		this.publishedAt = publishedAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}
}
