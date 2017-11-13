package pl.piterpti.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pl.piterpti.model.Income;

@Repository("incomeRepository")
public interface IncomeRepository extends JpaRepository<Income, Long>{
	
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
	 * Find incomes count for user
	 * @param login
	 * @return
	 */
	@Query("SELECT COUNT(i) FROM User u JOIN u.incomes i WHERE u.login = :login")
	public long userIncomesCount(@Param("login") String login);
	
	/**
	 * Find incomes for user with limit
	 * @param login
	 * @param pageable
	 * @return
	 */
	@Query("SELECT i FROM User u JOIN u.incomes i WHERE u.login = :login ORDER BY i.date DESC, i.id DESC")
	public List<Income> findUserIncomesWithLimit(@Param("login") String login, Pageable pageable);
}
