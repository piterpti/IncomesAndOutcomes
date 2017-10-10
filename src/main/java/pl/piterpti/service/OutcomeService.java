package pl.piterpti.service;

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

	public void saveOutcome(Outcome outcome);
	
	public List<Outcome> findAll();
	
	public void deleteOutcome(long outcomeId);
	
	public List<Outcome> findUserOutcomesWithLimit(String login, Pageable pageable);
	
}
