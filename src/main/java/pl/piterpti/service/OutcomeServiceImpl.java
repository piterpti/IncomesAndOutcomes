package pl.piterpti.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.piterpti.model.Outcome;
import pl.piterpti.repository.OutcomeRepository;

@Service("outcomeService")
public class OutcomeServiceImpl implements OutcomeService {

	@Autowired
	private OutcomeRepository outcomeRepository;
	
	@Override
	public void saveOutcome(Outcome outcome) {
		outcomeRepository.save(outcome);
	}

	@Override
	public List<Outcome> findAll() {
		return outcomeRepository.findAll();
	}

	@Override
	public void deleteOutcome(long outcomeId) {
		outcomeRepository.delete(outcomeId);
		
	}

	@Override
	public List<Outcome> findByUserIdAndInDatePeroid(long userId, Date fromDate, Date toDate) {
		return outcomeRepository.findByDate(userId);
	}

}
