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

}
