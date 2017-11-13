package pl.piterpti.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import pl.piterpti.model.Income;

public interface IncomeService {

	/**
	 * Return user incomes list 
	 * @param login
	 * @param pageable
	 * @return
	 */
	public List<Income> findUserIncomesWithLimit(String login, Pageable pageable);
	
	/**
	 * Find income by id
	 * @param id
	 * @return
	 */
	public Income findById(long id);
	
	/**
	 * Save income to DB
	 * @param income
	 */
	public void save(Income income);
	
	/**
	 * Delete all incomes
	 */
	public void deleteAll();
	
	/**
	 * ReturnuUser incomes count
	 * @param login user login
	 * @return
	 */
	public long userIncomesCount(String login);
	
	/**
	 * Delete passed income from db if exist
	 * @param income
	 */
	public void delete(Income income);
	
	/**
	 * Delete income by id
	 * @param id
	 */
	public void deleteById(long id);
	
	
	/**
	 * Find user incomes in passed date period
	 * @param userId
	 * @param fromDate
	 * @param toDate
	 * @return user incomes in date period
	 */
	public List<Income> findUserIncomesInDate(long userId, Date fromDate, Date toDate);
	
}
