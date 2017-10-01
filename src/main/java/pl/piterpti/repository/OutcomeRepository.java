package pl.piterpti.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.piterpti.model.Outcome;

public interface OutcomeRepository extends JpaRepository<Outcome, Long> {
	
	

}
