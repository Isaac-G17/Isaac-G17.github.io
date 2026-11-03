package dev.isaacguzman.portfolio.post.application.port.in;

import dev.isaacguzman.portfolio.post.domain.Post;
import dev.isaacguzman.portfolio.post.domain.PostNotFoundException;
import dev.isaacguzman.portfolio.shared.application.PageQuery;
import dev.isaacguzman.portfolio.shared.application.PageResult;
import dev.isaacguzman.portfolio.shared.domain.AlreadyExistsException;
import dev.isaacguzman.portfolio.shared.domain.InvalidDataException;
import java.time.Instant;

/**
 * Casos de uso del panel para los artículos. Ve los artículos en cualquier estado.
 * Los métodos con id lanzan {@link PostNotFoundException} si no existe.
 */
public interface ManagePostsUseCase {

	PageResult<Post> getAll(PageQuery page);

	Post getById(Long id);

	/**
	 * Crea el artículo como borrador.
	 *
	 * @throws AlreadyExistsException si el slug ya está en uso
	 * @throws InvalidDataException   si la categoría no existe
	 */
	Post create(PostCommand command);

	Post update(Long id, PostCommand command);

	/** Publica ya ({@code publicationDate} null) o programa la publicación para esa fecha. */
	Post publish(Long id, Instant publicationDate);

	/** Vuelve a borrador; también cancela una publicación programada. */
	Post unpublish(Long id);

	void delete(Long id);
}
