package pl.piterpti.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import pl.piterpti.model.Outcome;
import pl.piterpti.repository.OutcomeRepository;

@Service("outcomeService")
public class OutcomeServiceImpl implements OutcomeService {

	@Autowired
	private OutcomeRepository outcomeRepository;
	
	@Override
	public void save(Outcome outcome) {
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
	
	public List<Outcome> findUserOutcomesWithLimit(String login, Pageable pageable) {
		return outcomeRepository.findUserOutcomesWithLimit(login, pageable);		
	}

	@Override
	public Outcome findById(long id) {
		return outcomeRepository.findOne(id);
	}

	@Override
	public void deleteAll() {
		outcomeRepository.deleteAll();
	}

	@Override
	public long userOutcomesCount(String login) {
		return outcomeRepository.userOutcomesCount(login);
	}

	@Override
	public List<Outcome> findUserOutcomesInDate(long userId, Date fromDate, Date toDate) {
		return outcomeRepository.findUserOutcomesInTime(userId, fromDate, toDate);
	}


}
