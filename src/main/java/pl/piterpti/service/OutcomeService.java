package pl.piterpti.service;

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
	
	
}
