package dev.isaacguzman.portfolio.post.application.port.out;

import dev.isaacguzman.portfolio.post.domain.Category;
import dev.isaacguzman.portfolio.shared.domain.Slug;
import java.util.List;
import java.util.Optional;

/** Lo que la aplicación necesita de la base de datos de categorías. */
public interface CategoryRepositoryPort {

	/** Todas las categorías, en orden alfabético por nombre. */
	List<Category> findAll();

	Optional<Category> findById(Long id);

	/** Sin importar mayúsculas. {@code exceptId} ignora la propia categoría al editarla; puede ser null. */
	boolean existsByName(String name, Long exceptId);

	boolean existsBySlug(Slug slug, Long exceptId);

	/** Si algún artículo (en cualquier estado) usa la categoría. */
	boolean hasPosts(Long id);

	Category save(Category category);

	void deleteById(Long id);
}
