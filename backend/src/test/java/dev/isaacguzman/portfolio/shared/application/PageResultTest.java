package dev.isaacguzman.portfolio.shared.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;

class PageResultTest {

	@Test
	void calculatesTotalPagesRoundingUp() {
		assertThat(new PageResult<>(List.of(), 0, 10, 0).totalPages()).isZero();
		assertThat(new PageResult<>(List.of(), 0, 10, 10).totalPages()).isEqualTo(1);
		assertThat(new PageResult<>(List.of(), 0, 10, 11).totalPages()).isEqualTo(2);
	}

	@Test
	void pageQueryRejectsInvalidValues() {
		assertThatThrownBy(() -> new PageQuery(-1, 10)).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> new PageQuery(0, 0)).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> new PageQuery(0, PageQuery.MAX_SIZE + 1)).isInstanceOf(IllegalArgumentException.class);
	}
}
