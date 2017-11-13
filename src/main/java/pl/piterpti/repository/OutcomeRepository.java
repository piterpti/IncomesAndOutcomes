package pl.piterpti.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pl.piterpti.model.Outcome;

@Repository
public interface OutcomeRepository extends JpaRepository<Outcome, Long> {

	/**
	 * Find user outcomes in passed date period
	 * @param fromDate
	 * @param toDate
	 * @return user outcomes
	 */
	@Query("SELECT o FROM User u JOIN u.outcomes o WHERE u.id = :id AND o.date >= :fromDate AND o.date <= :toDate ORDER BY o.date")
	public List<Outcome> findUserOutcomesInTime(@Param("id") long userId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
	
	/**
	 * Find outcomes for user with limit
	 * @param userId
	 * @param limit
	 * @return user outcomes
	 */
	@Query("SELECT o FROM User u JOIN u.outcomes o WHERE u.login = :login ORDER BY o.date DESC, o.id DESC")
	public List<Outcome> findUserOutcomesWithLimit(@Param("login") String login, Pageable pageable);
	
	/**
	 * Find outcomes count for user 
	 * @param login
	 * @return
	 */
	@Query("SELECT COUNT(o) FROM User u JOIN u.outcomes o WHERE u.login = :login")
	public long userOutcomesCount(@Param("login") String login);

}
