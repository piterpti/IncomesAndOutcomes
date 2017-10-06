package pl.piterpti.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import pl.piterpti.constants.UserRoles;
import pl.piterpti.model.Outcome;
import pl.piterpti.model.Role;
import pl.piterpti.model.User;
import pl.piterpti.repository.RoleRepository;
import pl.piterpti.repository.UserRepository;

@Service("userService")
public class UserServiceImpl implements UserService {

	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	
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
}
