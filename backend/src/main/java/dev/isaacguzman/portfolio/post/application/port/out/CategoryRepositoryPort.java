package dev.isaacguzman.portfolio.post.application.port.out;

import dev.isaacguzman.portfolio.post.domain.Category;
import java.util.List;

/** Lo que la aplicación necesita de la base de datos de categorías. */
public interface CategoryRepositoryPort {

	/** Todas las categorías, en orden alfabético por nombre. */
	List<Category> findAll();
}
