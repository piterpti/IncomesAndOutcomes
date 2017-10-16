package pl.piterpti.configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import pl.piterpti.constants.UserRoles;
import pl.piterpti.model.Category;
import pl.piterpti.model.Role;
import pl.piterpti.repository.CategoryRepository;
import pl.piterpti.repository.RoleRepository;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	private Logger logger = Logger.getLogger(InitialDataLoader.class);
	
	private static final String CATEGORIES_FILE_URL = "categories.txt";

	boolean alreadySetup = false;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {

		if (!alreadySetup) {

			checkRoleExistAndAddToDatabase();

		}
		
		File categoriesFile = new File(CATEGORIES_FILE_URL);
		if (categoriesFile.exists()) {
			
			importCategoriesFromFile(categoriesFile);
			
		}
	}
	
	/**
	 * Removes categories from DB and add all from passed file
	 * @param file
	 */
	private void importCategoriesFromFile(File file) {
		
		BufferedReader br = null;
		String line;
		
		List<String> categoryList = new ArrayList<>();
		
		boolean ok = false;
		
		try {
			
			br = new BufferedReader(new FileReader(file));
			
			while ((line = br.readLine()) != null) {
				
				if (!line.isEmpty()) {
					categoryList.add(line);
				}
			}
			
			ok = true;
			
		} catch (FileNotFoundException e) {
			// thats should not happen
			logger.error(e);
			
		} catch (IOException e) {
			
			logger.error(e);
			
		} finally {
			
			if (br != null) {
				try {
					br.close();					
				} catch (Exception e) {
					logger.error(e);
				}
			}
			
		}
		
		if (ok) {
			// import to DB
			logger.info("Deleting all categories and add new: " + categoryList);
			categoryRepository.deleteAll();
			
			Category category;
			for (String s : categoryList) {
				category = new Category();
				category.setName(s);
				categoryRepository.save(category);
			}
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
