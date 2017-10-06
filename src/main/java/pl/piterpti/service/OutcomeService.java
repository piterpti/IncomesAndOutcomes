package pl.piterpti.service;

import java.util.Date;
import java.util.List;

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
	
	public List<Outcome> findByUserIdAndInDatePeroid(long userId, Date fromDate, Date toDate);
	
	
}
