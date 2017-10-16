package pl.piterpti.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import pl.piterpti.model.Income;

public interface IncomeService {

	public void addIncome(Income income);
	
	public List<Income> findUserIncomesWithLimit(String login, Pageable pageable);
	
}
