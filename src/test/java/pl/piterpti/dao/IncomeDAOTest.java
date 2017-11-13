package pl.piterpti.dao;

import java.math.BigDecimal;
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
import pl.piterpti.service.IncomeService;


@RunWith(SpringRunner.class)
@SpringBootTest
public class IncomeDAOTest {
	
	@Autowired
	private IncomeService incomeService;
	
	private Random random = new Random();
	
	@SuppressWarnings("unused")
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
