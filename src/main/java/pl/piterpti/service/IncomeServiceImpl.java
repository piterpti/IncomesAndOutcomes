package pl.piterpti.service;

import org.springframework.beans.factory.annotation.Autowired;

import pl.piterpti.model.Income;
import pl.piterpti.repository.IncomeRepository;

public class IncomeServiceImpl implements IncomeService {

	@Autowired
	private IncomeRepository incomeRepository;
	
	@Override
	public void addIncome(Income income) {
		incomeRepository.save(income);
	}

}
