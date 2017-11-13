package pl.piterpti.service;

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
	 * Deactivate user with passed id
	 * @param id
	 * @return operation result - true = successful
	 */
	public boolean deactivateUser(long id);
	
}
