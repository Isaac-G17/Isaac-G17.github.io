package dev.isaacguzman.portfolio.post.application.service;

import dev.isaacguzman.portfolio.post.application.port.in.GetCategoriesQuery;
import dev.isaacguzman.portfolio.post.application.port.in.ManageTaxonomyUseCase;
import dev.isaacguzman.portfolio.post.application.port.out.CategoryRepositoryPort;
import dev.isaacguzman.portfolio.post.application.port.out.TagRepositoryPort;
import dev.isaacguzman.portfolio.post.domain.Category;
import dev.isaacguzman.portfolio.post.domain.PostNotFoundException;
import dev.isaacguzman.portfolio.post.domain.Tag;
import dev.isaacguzman.portfolio.shared.domain.AlreadyExistsException;
import dev.isaacguzman.portfolio.shared.domain.InUseException;
import dev.isaacguzman.portfolio.shared.domain.Slug;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Categorías y etiquetas del blog: la consulta pública y la gestión desde el panel. */
@Service
@Transactional(readOnly = true)
public class TaxonomyService implements GetCategoriesQuery, ManageTaxonomyUseCase {

	private final CategoryRepositoryPort categories;
	private final TagRepositoryPort tags;

	public TaxonomyService(CategoryRepositoryPort categories, TagRepositoryPort tags) {
		this.categories = categories;
		this.tags = tags;
	}

	// --- Categorías ---

	@Override
	public List<Category> getCategories() {
		return categories.findAll();
	}

	@Override
	public List<Category> getAllCategories() {
		return categories.findAll();
	}

	@Override
	@Transactional
	public Category createCategory(String name, Slug slug, String description) {
		Category category = Category.create(name, slug, description);
		ensureCategoryIsUnique(category, null);
		return categories.save(category);
	}

	@Override
	@Transactional
	public Category updateCategory(Long id, String name, Slug slug, String description) {
		Category updated = findCategory(id).update(name, slug, description);
		ensureCategoryIsUnique(updated, id);
		return categories.save(updated);
	}

	@Override
	@Transactional
	public void deleteCategory(Long id) {
		Category category = findCategory(id);
		if (categories.hasPosts(id)) {
			throw new InUseException("La categoría \"" + category.name() + "\" tiene artículos; muévelos antes de borrarla");
		}
		categories.deleteById(id);
	}

	private Category findCategory(Long id) {
		return categories.findById(id).orElseThrow(() -> PostNotFoundException.categoryWithId(id));
	}

	private void ensureCategoryIsUnique(Category category, Long exceptId) {
		if (categories.existsByName(category.name(), exceptId)) {
			throw new AlreadyExistsException("una categoría con el nombre", category.name());
		}
		if (categories.existsBySlug(category.slug(), exceptId)) {
			throw new AlreadyExistsException("una categoría con el slug", category.slug().value());
		}
	}

	// --- Etiquetas ---

	@Override
	public List<Tag> getAllTags() {
		return tags.findAll();
	}

	@Override
	@Transactional
	public Tag createTag(String name) {
		Tag tag = Tag.named(name);
		ensureTagIsUnique(tag, null);
		return tags.save(tag);
	}

	@Override
	@Transactional
	public Tag renameTag(Long id, String name) {
		Tag renamed = findTag(id).rename(name);
		ensureTagIsUnique(renamed, id);
		return tags.save(renamed);
	}

	@Override
	@Transactional
	public void deleteTag(Long id) {
		Tag tag = findTag(id);
		if (tags.hasPosts(id)) {
			throw new InUseException("La etiqueta \"" + tag.name() + "\" está en uso; quítala de los artículos antes de borrarla");
		}
		tags.deleteById(id);
	}

	private Tag findTag(Long id) {
		return tags.findById(id).orElseThrow(() -> PostNotFoundException.tagWithId(id));
	}

	/** El slug sale del nombre, así que comparar slugs detecta "Java" y "java" como la misma etiqueta. */
	private void ensureTagIsUnique(Tag tag, Long exceptId) {
		if (tags.existsBySlug(tag.slug(), exceptId)) {
			throw new AlreadyExistsException("una etiqueta con el nombre", tag.name());
		}
	}
}
