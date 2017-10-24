package pl.piterpti.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import pl.piterpti.model.Income;
import pl.piterpti.repository.IncomeRepository;
import pl.piterpti.repository.UserRepository;

@Service
public class IncomeServiceImpl implements IncomeService {

	@Autowired
	private IncomeRepository incomeRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public void addIncome(Income income) {
		incomeRepository.save(income);
	}

	@Override
	public List<Income> findUserIncomesWithLimit(String login, Pageable pageable) {
		return userRepository.findIncomesByUserWithLimit(login, pageable);
	}

	@Override
	public Income findById(long id) {
		return incomeRepository.findById(id);
	}

	@Override
	public void save(Income income) {
		incomeRepository.save(income);
	}

}
