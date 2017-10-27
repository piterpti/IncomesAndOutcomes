package pl.piterpti.dao;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import pl.piterpti.model.Category;
import pl.piterpti.model.Income;
import pl.piterpti.model.User;
import pl.piterpti.service.CategoryService;
import pl.piterpti.service.IncomeService;
import pl.piterpti.service.UserService;


@RunWith(SpringRunner.class)
@SpringBootTest
public class IncomeDAOTest {
	
	@Autowired
	private IncomeService incomeService;
	
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private UserService userService;
	
	private Random random = new Random();
	
	public void testAddRandomIncomes() {
		
	}

	@Test
	public void testAddRandomOutcomes() {
		
		List<Income> incomes = new ArrayList<>();
		List<Category> categories = categoryService.findAll();
		
		User user = userService.findByLogin("piter");
		
		assertNotNull(user);
		
		for (int i = 0; i < 105; i++) {
			incomes.add(generateRandomOutcome(categories, user));
		}
		
		user.setIncomes(incomes);
		
		for (Income i : incomes) {
			incomeService.save(i);
		}
		
		userService.updateUser(user);
	}
	
	private Income generateRandomOutcome(List<Category> categories, User user) {
		Income income = new Income();
		
		income.setValue(new BigDecimal(random.nextInt(250) + 30));
		income.setCategory(categories.get(random.nextInt(categories.size())));
		income.setShortDesc(randomString(random.nextInt(25) + 1));
		
		Date date = new Date(new Date().getTime() + random.nextInt((int)TimeUnit.DAYS.toMillis(10)));
		income.setDate(date);
		return income;
	}
	
	private String randomString(int length) {
		char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
		    char c = chars[random.nextInt(chars.length)];
		    sb.append(c);
		}
		return sb.toString();
	}
	
	/**
	 * Deleting all incomes from DB
	 */
	@Test
	public void testClearALLIncomes() {
		incomeService.deleteAll();
	}
	
	
}
