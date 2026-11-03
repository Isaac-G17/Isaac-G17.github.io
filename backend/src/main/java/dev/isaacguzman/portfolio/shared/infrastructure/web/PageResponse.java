package dev.isaacguzman.portfolio.shared.infrastructure.web;

import dev.isaacguzman.portfolio.shared.application.PageResult;
import java.util.List;

/** Formato JSON de una página de resultados. La primera página es la 0. */
public record PageResponse<T>(List<T> items, int page, int size, long totalItems, int totalPages) {

	public static <T> PageResponse<T> from(PageResult<T> result) {
		return new PageResponse<>(result.items(), result.page(), result.size(), result.totalItems(),
				result.totalPages());
	}
}
