package pl.piterpti.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pl.piterpti.model.Outcome;
import pl.piterpti.model.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * Find User by login
	 * @param login user login
	 * @return found user
	 */
	public User findByLogin(String login);
	
	/**
	 * Find user outcomes in passed date period
	 * @param fromDate
	 * @param toDate
	 * @return user outcomes
	 */
	@Query("SELECT o FROM User u JOIN u.outcomes o WHERE u.id = :id AND o.outcomeDate >= :fromDate AND o.outcomeDate <= :toDate ORDER BY o.outcomeDate")
	public List<Outcome> findUserOutcomesInTime(@Param("id") long userId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
	
}
