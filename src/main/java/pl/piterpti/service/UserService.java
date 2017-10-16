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
	
	public User findByLogin(String login);
	public void saveUser(User user);
	public void updateUser(User user);
	
	public List<Outcome> findUserOutcomesInDate(long userId, Date fromDate, Date toDate);
	
	public List<Income> findUserIncomesInDate(long userId, Date fromDate, Date toDate);
	

}
