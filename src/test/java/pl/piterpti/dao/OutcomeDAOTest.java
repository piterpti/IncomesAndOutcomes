package pl.piterpti.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import pl.piterpti.model.Category;
import pl.piterpti.model.Outcome;
import pl.piterpti.model.User;
import pl.piterpti.service.CategoryService;
import pl.piterpti.service.IncomeService;
import pl.piterpti.service.OutcomeService;
import pl.piterpti.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OutcomeDAOTest {

	@Autowired
	private OutcomeService outcomeService;
	
	@Autowired
	private IncomeService incomeService;
	
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private UserService userService;
	
	private Random random = new Random();
	
	@Test
	@Transactional 
	public void testGetAllOutcomes() {
		
		List<Outcome> outcomes = outcomeService.findAll();
		
		int outcomeSize = outcomes.size();
		outcomeService.saveOutcome(new Outcome(new BigDecimal(200), new Date(), "ABC"));
		outcomes = outcomeService.findAll();
		
		assertEquals(outcomeSize + 1, outcomes.size());
	}
	
	@Test
	@Transactional
	public void testOutcomesOperations() {
		
		outcomeService.deleteAll();
		
		Outcome outcome = new Outcome(new BigDecimal(200), new Date(), "ABC");
		outcomeService.saveOutcome(outcome);
		List<Outcome> outcomes = outcomeService.findAll();
		
		assertEquals(1, outcomes.size());
		assertEquals("ABC", outcomes.get(0).getShortDesc());
	}
	
	@Test
	public void testAddRandomOutcomes() {
		
		List<Outcome> outcomes = new ArrayList<>();
		List<Category> categories = categoryService.findAll();
		
		User user = userService.findByLogin("piter");
		
		assertNotNull(user);
		
		for (int i = 0; i < 45; i++) {
			outcomes.add(generateRandomOutcome(categories, user));
		}
		
		user.setOutcomes(outcomes);
		
		for (Outcome o : outcomes) {
			outcomeService.saveOutcome(o);
		}
		
		userService.updateUser(user);
	}
	
	private Outcome generateRandomOutcome(List<Category> categories, User user) {
		Outcome outcome = new Outcome();
		
		outcome.setValue(new BigDecimal(random.nextInt(250) + 30));
		outcome.setCategory(categories.get(random.nextInt(categories.size())));
		outcome.setShortDesc(randomString(random.nextInt(25) + 1));
		
		Date date = new Date(new Date().getTime() + random.nextInt((int)TimeUnit.DAYS.toMillis(10)));
		outcome.setDate(date);
		return outcome;
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
	 * Deleting all operations: incomes. outcomes from DB
	 */
	@Test
	public void testClearOutcomesAndIncomes() {
		outcomeService.deleteAll();
		incomeService.deleteAll();
	}
	
}
