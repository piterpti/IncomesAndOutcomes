package pl.piterpti.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pl.piterpti.model.Outcome;

@Repository
public interface OutcomeRepository extends JpaRepository<Outcome, Long> {

	@Query("SELECT o FROM Outcome o WHERE o.id = :id")
	public List<Outcome> findByDate(@Param("id") long id);

}
