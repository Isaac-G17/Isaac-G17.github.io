package dev.isaacguzman.portfolio.archfixture.domain;

import org.springframework.http.HttpStatus;

/** Ejemplo que rompe la regla a propósito: un modelo de dominio que depende de Spring. */
public class BadDomainModel {

	private HttpStatus status;
}
