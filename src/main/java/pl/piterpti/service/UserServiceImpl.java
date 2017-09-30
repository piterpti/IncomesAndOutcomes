package pl.piterpti.service;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import pl.piterpti.model.Role;
import pl.piterpti.model.User;
import pl.piterpti.repository.RoleRepository;
import pl.piterpti.repository.UserRepository;

@Service("userService")
public class UserServiceImpl implements UserService {

	private String ROLE_ADMIN = "ADMIN";
	
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
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setEnabled(true);
		Role userRole = roleRepository.findByRole(ROLE_ADMIN);
		user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		userRepository.save(user);
	}

}
