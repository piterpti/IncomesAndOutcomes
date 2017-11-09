package pl.piterpti.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import pl.piterpti.constants.UserRoles;
import pl.piterpti.model.Category;
import pl.piterpti.model.Income;
import pl.piterpti.model.Outcome;
import pl.piterpti.model.Role;
import pl.piterpti.model.User;
import pl.piterpti.repository.RoleRepository;
import pl.piterpti.repository.UserRepository;
import pl.piterpti.toolkit.Toolkit;

@Service("userService")
public class UserServiceImpl implements UserService {

	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private CategoryService categoryService;
	
	@Override
	public User findByLogin(String login) {
		return userRepository.findByLogin(login);
	}

	@Override
	public void saveUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(String.valueOf(user.getPassword())).toCharArray());
		user.setEnabled(true);
		Role userRole = roleRepository.findByRole(UserRoles.USER_ROLE_ADMIN);
		user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		
		user.setCategories(Toolkit.getUserStartCategories());
		
		for (Category category : user.getCategories()) {
			categoryService.save(category);
		}
		
		userRepository.save(user);
	}
	
	@Override
	public void updateUser(User user) {
		userRepository.save(user);
	}

	@Override
	public List<Outcome> findUserOutcomesInDate(long userId, Date fromDate, Date toDate) {
		return userRepository.findUserOutcomesInTime(userId, fromDate, toDate);
	}

	@Override
	public List<Income> findUserIncomesInDate(long userId, Date fromDate, Date toDate) {
		return userRepository.findUserIncomesInTime(userId, fromDate, toDate);
	}

	@Override
	public boolean deactivateUser(long id) {
		User u = userRepository.findOne(id);
		if (u == null) {
			throw new IllegalArgumentException("User with id " + id + " not found - can not delete") ;
		}
		
		u.setEnabled(false);
		
		return userRepository.save(u) != null;
	}
}
