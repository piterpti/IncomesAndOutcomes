package pl.piterpti.toolkit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import pl.piterpti.model.Category;
import pl.piterpti.model.DateFromTo;

/**
 * Toolkit for various operation
 * @author piter
 *
 */
public class Toolkit {
		
	/**
	 * Start operation categories 
	 */
	private static final String[] START_CATEGORIES = new String[] {
			"Others", "Food", "Fuel", "Sport", "Alcohol", "Entertaiment"	
	};
	
	/**
	 * Get date period - from first to last day current of month
	 * @return
	 */
	public static DateFromTo getDatePeriodToForm() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		
		int lastMonthDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		Date fromDate = cal.getTime();
		
		cal.set(Calendar.DAY_OF_MONTH, lastMonthDay);
		Date toDate = cal.getTime();
		
		return new DateFromTo(fromDate, toDate);
	}
	
	/**
	 * Get start example categories for user
	 * @return list of operaiton categories
	 */
	public static List<Category> getUserStartCategories() {
		List<Category> categories = new ArrayList<>();
		Category category;
		for (String s : START_CATEGORIES) {
			category = new Category();
			category.setName(s);
			category.setActive(true);
			categories.add(category);
		}
		return categories;
	}
	
	/**
	 * Get logged user
	 * @return
	 */
	public static String getLoggerUserName() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getName();
	}
}
