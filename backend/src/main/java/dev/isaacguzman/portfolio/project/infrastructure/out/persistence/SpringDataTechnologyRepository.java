package dev.isaacguzman.portfolio.project.infrastructure.out.persistence;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface SpringDataTechnologyRepository extends JpaRepository<TechnologyJpaEntity, Long> {

	/** {@code lowerCaseNames} debe venir en minúsculas: "angular" encuentra "Angular". */
	@Query("select t from TechnologyJpaEntity t where lower(t.name) in :lowerCaseNames")
	List<TechnologyJpaEntity> findByLowerCaseNameIn(Collection<String> lowerCaseNames);
}
