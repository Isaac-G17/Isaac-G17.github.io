package dev.isaacguzman.portfolio.archfixture.application;

import dev.isaacguzman.portfolio.archfixture.infrastructure.SomeAdapter;

/** Ejemplo que rompe la regla a propósito: un caso de uso que usa un adaptador directamente. */
public class BadService {

	private final SomeAdapter adapter = new SomeAdapter();
}
