package dev.isaacguzman.portfolio.post.application.port.in;

import dev.isaacguzman.portfolio.post.domain.Category;
import dev.isaacguzman.portfolio.post.domain.Tag;
import dev.isaacguzman.portfolio.shared.domain.AlreadyExistsException;
import dev.isaacguzman.portfolio.shared.domain.InUseException;
import dev.isaacguzman.portfolio.shared.domain.Slug;
import java.util.List;

/**
 * Casos de uso del panel para categorías y etiquetas (la "taxonomía" del blog).
 * Crear o renombrar con un nombre o slug repetido lanza {@link AlreadyExistsException};
 * borrar algo que usan artículos lanza {@link InUseException}.
 */
public interface ManageTaxonomyUseCase {

	List<Category> getAllCategories();

	/** {@code slug} es opcional: si es null, sale del nombre. */
	Category createCategory(String name, Slug slug, String description);

	Category updateCategory(Long id, String name, Slug slug, String description);

	void deleteCategory(Long id);

	List<Tag> getAllTags();

	Tag createTag(String name);

	Tag renameTag(Long id, String name);

	void deleteTag(Long id);
}
