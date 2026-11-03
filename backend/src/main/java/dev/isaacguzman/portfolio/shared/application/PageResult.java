package dev.isaacguzman.portfolio.shared.application;

import java.util.List;
import java.util.function.Function;

/** Una página de resultados y el total, para que el cliente sepa cuántas páginas hay. */
public record PageResult<T>(List<T> items, int page, int size, long totalItems) {

	public PageResult {
		items = List.copyOf(items);
	}

	public int totalPages() {
		return (int) Math.ceil((double) totalItems / size);
	}

	public <R> PageResult<R> map(Function<T, R> mapper) {
		return new PageResult<>(items.stream().map(mapper).toList(), page, size, totalItems);
	}
}
