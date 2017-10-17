package pl.piterpti.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import pl.piterpti.constants.UserRoles;
import pl.piterpti.model.Role;
import pl.piterpti.model.User;
import pl.piterpti.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDAOTest {

	@Autowired
	private UserService userService;
	
	
	@Test
	@Transactional
	public void testSaveAndGetUserFromDb() {
		
		User user = new User();
		user.setLogin("login");
		user.setPassword("password".toCharArray());
		user.setUserName("username");
		user.setEnabled(true);
		
		userService.saveUser(user);
		
		user = userService.findByLogin("login");
		assertNotNull(user);
		
		Set<Role> roles = user.getRoles();
		
		Role role = roles.iterator().next();
		assertTrue(role.getRole().equals(UserRoles.USER_ROLE_ADMIN));
		assertEquals("username", user.getUserName());
		
		user.setUserName("tmpusername");
		userService.updateUser(user);
		
		user = userService.findByLogin("login");
		assertEquals("tmpusername", user.getUserName());
	}
	
	
}
