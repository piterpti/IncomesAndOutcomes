package pl.piterpti.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import pl.piterpti.model.Outcome;


/**
 * Service to outcomes
 * 
 * @author Piter
 *
 */
public interface OutcomeService {

	/**
	 * Save outcome to db
	 * @param outcome
	 */
	public void save(Outcome outcome);
	
	/**
	 * Find all outcomes
	 * @return
	 */
	public List<Outcome> findAll();
	
	/**
	 * Delete outcome by id
	 * @param outcomeId
	 */
	public void deleteOutcome(long outcomeId);
	
	/**
	 * Find user outcomes
	 * @param login
	 * @param pageable
	 * @return
	 */
	public List<Outcome> findUserOutcomesWithLimit(String login, Pageable pageable);
	
	/**
	 * Count user outcomes
	 * @param login
	 * @return
	 */
	public long userOutcomesCount(String login);
	
	/**
	 * Find outcome by id
	 * @param id
	 * @return
	 */
	public Outcome findById(long id);
	
	/**
	 * Delete all outcomes
	 */
	public void deleteAll();
	
	/**
	 * Find user outcomes in passed date period
	 * @param userId
	 * @param fromDate
	 * @param toDate
	 * @return user ourcomes in date period
	 */
	public List<Outcome> findUserOutcomesInDate(long userId, Date fromDate, Date toDate);
	
}
