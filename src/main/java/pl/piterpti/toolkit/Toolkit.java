package pl.piterpti.toolkit;

import java.util.Calendar;
import java.util.Date;

import pl.piterpti.model.DateFromTo;

/**
 * Toolkit for various operation
 * @author piter
 *
 */
public class Toolkit {
	
	public static DateFromTo getDatePeriodToForm() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return new DateFromTo(cal.getTime(), new Date());
	}

}
