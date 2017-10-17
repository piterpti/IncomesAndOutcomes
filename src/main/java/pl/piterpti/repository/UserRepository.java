package pl.piterpti.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pl.piterpti.model.Income;
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
	@Query("SELECT o FROM User u JOIN u.outcomes o WHERE u.id = :id AND o.date >= :fromDate AND o.date <= :toDate ORDER BY o.date")
	public List<Outcome> findUserOutcomesInTime(@Param("id") long userId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
	
	
	/**
	 * Find user incomes in passed date period
	 * @param userId
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	@Query("SELECT i FROM User u JOIN u.incomes i WHERE u.id = :id AND i.date >= :fromDate AND i.date <= :toDate ORDER BY i.date")
	public List<Income> findUserIncomesInTime(@Param("id") long userId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate); 
	
	/**
	 * Find outcomes for user with limit
	 * @param userId
	 * @param limit
	 * @return user outcomes
	 */
	@Query("SELECT o FROM User u JOIN u.outcomes o WHERE u.login = :login ORDER BY o.date DESC, o.id DESC")
	public List<Outcome> findByUserWithLimit(@Param("login") String login, Pageable pageable);
	
	
	/**
	 * Find incomes for user with limit
	 * @param login
	 * @param pageable
	 * @return
	 */
	@Query("SELECT i FROM User u JOIN u.incomes i WHERE u.login = :login ORDER BY i.date DESC, i.id DESC")
	public List<Income> findIncomesByUserWithLimit(@Param("login") String login, Pageable pageable);
}
