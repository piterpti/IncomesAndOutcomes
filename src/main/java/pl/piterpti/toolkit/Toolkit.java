package pl.piterpti.toolkit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import pl.piterpti.constants.Constants;
import pl.piterpti.model.Category;
import pl.piterpti.model.DateFromTo;
import pl.piterpti.model.User;;

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
	
	/**
	 * USer password validator
	 * @param password
	 * @return validate ok or not
	 */
	public static boolean validatePassword(String password) {
		if (password.length() < Constants.USER_PASSWORD_MIN_LENGTH) {
			return false;
		}
		
		boolean isDigit = false;
		boolean isLetter = false;
		for (int i = 0; i < password.length(); i++) {
			if (Character.isDigit(password.charAt(i))) {
				isDigit = true;
			} else if (Character.isLetter(password.charAt(i))) {
				isLetter = true;
			}
		}
		
		return isDigit && isLetter;
	}
	
	public static boolean validateUserPassword(User user, String oldPassword) {
		
		if (user == null || oldPassword == null || oldPassword.isEmpty()) {
			throw new IllegalArgumentException("Data not complete - validating password failed");
		}
		
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String old = String.valueOf(user.getPassword());
		
		if (bCryptPasswordEncoder.matches(oldPassword, old)) {
			return true;
		} else {
			return false;
		}
	}
	
	
}
