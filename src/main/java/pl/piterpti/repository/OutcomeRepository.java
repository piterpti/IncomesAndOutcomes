package pl.piterpti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.piterpti.model.Outcome;

@Repository
public interface OutcomeRepository extends JpaRepository<Outcome, Long> {

	public Outcome findById(long id);

}
