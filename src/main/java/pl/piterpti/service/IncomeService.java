package pl.piterpti.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import pl.piterpti.model.Income;

public interface IncomeService {

	public void addIncome(Income income);
	
	public List<Income> findUserIncomesWithLimit(String login, Pageable pageable);
	
	public Income findById(long id);
	
	public void save(Income income);
	
	public void deleteAll();
	
	public long userIncomesCount(String login);
	
	public void delete(Income income);
	
	public void deleteById(long id);
	
}
