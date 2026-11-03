package dev.isaacguzman.portfolio.post.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.isaacguzman.portfolio.post.application.port.out.CategoryRepositoryPort;
import dev.isaacguzman.portfolio.post.application.port.out.TagRepositoryPort;
import dev.isaacguzman.portfolio.post.domain.Category;
import dev.isaacguzman.portfolio.post.domain.Tag;
import dev.isaacguzman.portfolio.shared.domain.AlreadyExistsException;
import dev.isaacguzman.portfolio.shared.domain.InUseException;
import dev.isaacguzman.portfolio.shared.domain.Slug;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class TaxonomyServiceTest {

	private final CategoryRepositoryPort categories = mock(CategoryRepositoryPort.class);
	private final TagRepositoryPort tags = mock(TagRepositoryPort.class);
	private final TaxonomyService service = new TaxonomyService(categories, tags);

	@Test
	void createsACategoryWithTheSlugFromItsName() {
		when(categories.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		assertThat(service.createCategory("Bases de datos", null, null).slug()).isEqualTo(new Slug("bases-de-datos"));
	}

	@Test
	void categoryNameMustBeUnique() {
		when(categories.existsByName("Backend", null)).thenReturn(true);

		assertThatThrownBy(() -> service.createCategory("Backend", null, null))
				.isInstanceOf(AlreadyExistsException.class);
		verify(categories, never()).save(any());
	}

	@Test
	void categoryWithPostsCannotBeDeleted() {
		when(categories.findById(1L)).thenReturn(Optional.of(new Category(1L, "Backend", new Slug("backend"), null)));
		when(categories.hasPosts(1L)).thenReturn(true);

		assertThatThrownBy(() -> service.deleteCategory(1L)).isInstanceOf(InUseException.class);
		verify(categories, never()).deleteById(any());
	}

	@Test
	void tagsWithTheSameSlugAreTheSameTag() {
		// "JAVA" genera el slug "java", que ya existe.
		when(tags.existsBySlug(new Slug("java"), null)).thenReturn(true);

		assertThatThrownBy(() -> service.createTag("JAVA")).isInstanceOf(AlreadyExistsException.class);
	}

	@Test
	void tagInUseCannotBeDeleted() {
		when(tags.findById(1L)).thenReturn(Optional.of(new Tag(1L, "Java", new Slug("java"))));
		when(tags.hasPosts(1L)).thenReturn(true);

		assertThatThrownBy(() -> service.deleteTag(1L)).isInstanceOf(InUseException.class);
		verify(tags, never()).deleteById(any());
	}
}
