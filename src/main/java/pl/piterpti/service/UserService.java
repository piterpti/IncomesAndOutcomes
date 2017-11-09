package pl.piterpti.service;

import java.util.Date;
import java.util.List;

import pl.piterpti.model.Income;
import pl.piterpti.model.Outcome;
import pl.piterpti.model.User;

/**
 * User serivce 
 * 
 * @author Piter
 *
 */
public interface UserService {
	
	/**
	 * Find user by passed login
	 * @param login
	 * @return user or null if not found
	 */
	public User findByLogin(String login);
	
	/**
	 * Save user to db
	 * @param user
	 */
	public void saveUser(User user);
	
	/**
	 * Update user
	 * @param user
	 */
	public void updateUser(User user);
	
	/**
	 * Find user outcomes in passed date period
	 * @param userId
	 * @param fromDate
	 * @param toDate
	 * @return user ourcomes in date period
	 */
	public List<Outcome> findUserOutcomesInDate(long userId, Date fromDate, Date toDate);
	
	/**
	 * Find user incomes in passed date period
	 * @param userId
	 * @param fromDate
	 * @param toDate
	 * @return user incomes in date period
	 */
	public List<Income> findUserIncomesInDate(long userId, Date fromDate, Date toDate);
	
	/**
	 * Deactivate user with passed id
	 * @param id
	 * @return operation result - true = successful
	 */
	public boolean deactivateUser(long id);
	
}
