package pl.piterpti.service;

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

}
