package pl.piterpti.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import pl.piterpti.model.Outcome;
import pl.piterpti.repository.OutcomeRepository;
import pl.piterpti.repository.UserRepository;

@Service("outcomeService")
public class OutcomeServiceImpl implements OutcomeService {

	@Autowired
	private OutcomeRepository outcomeRepository;
	
	@Autowired
	private UserRepository userRepository;
	
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
	
	public List<Outcome> findUserOutcomesWithLimit(String login, Pageable pageable) {
		return userRepository.findByUserWithLimit(login, pageable);		
	}

	@Override
	public Outcome findById(long id) {
		return outcomeRepository.findById(id);
	}


}
