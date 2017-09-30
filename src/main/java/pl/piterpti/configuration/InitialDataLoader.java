package pl.piterpti.configuration;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import pl.piterpti.constants.UserRoles;
import pl.piterpti.model.Role;
import pl.piterpti.repository.RoleRepository;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	private Logger logger = Logger.getLogger(InitialDataLoader.class);

	boolean alreadySetup = false;

	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {

		if (!alreadySetup) {

			checkRoleExistAndAddToDatabase();

		}
	}

	/**
	 * Check that roles exist in Database and if not add them
	 */
	private void checkRoleExistAndAddToDatabase() {
		String[] userRoles = UserRoles.getRoles();

		Role role;
		for (final String userRole : userRoles) {

			role = roleRepository.findByRole(userRole);

			if (role == null) {

				role = new Role();
				role.setRole(userRole);

				logger.info("Trying add new role to Db: " + role.toString());

				roleRepository.save(role);
			}
		}

		alreadySetup = true;
	}
}
