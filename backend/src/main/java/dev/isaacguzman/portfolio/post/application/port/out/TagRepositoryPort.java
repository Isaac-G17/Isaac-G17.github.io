package dev.isaacguzman.portfolio.post.application.port.out;

import dev.isaacguzman.portfolio.post.domain.Tag;
import dev.isaacguzman.portfolio.shared.domain.Slug;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/** Lo que la aplicación necesita de la base de datos de etiquetas. */
public interface TagRepositoryPort {

	/** Todas las etiquetas, en orden alfabético por nombre. */
	List<Tag> findAll();

	Optional<Tag> findById(Long id);

	/** Las etiquetas cuyos slugs están en la lista (las que no existen simplemente no aparecen). */
	List<Tag> findBySlugs(Collection<Slug> slugs);

	/** {@code exceptId} ignora la propia etiqueta al editarla; puede ser null. */
	boolean existsBySlug(Slug slug, Long exceptId);

	/** Si algún artículo (en cualquier estado) usa la etiqueta. */
	boolean hasPosts(Long id);

	Tag save(Tag tag);

	void deleteById(Long id);
}
