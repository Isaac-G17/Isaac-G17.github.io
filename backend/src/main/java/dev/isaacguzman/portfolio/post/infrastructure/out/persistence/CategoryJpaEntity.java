package dev.isaacguzman.portfolio.post.infrastructure.out.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "categories")
public class CategoryJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String slug;
	private String description;

	protected CategoryJpaEntity() {
		// Requerido por JPA
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSlug() {
		return slug;
	}

	public String getDescription() {
		return description;
	}

	// Setters: los usa PostPersistenceMapper para copiar los datos del dominio al guardar.

	public void setName(String name) {
		this.name = name;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
