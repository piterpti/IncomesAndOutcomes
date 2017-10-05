package pl.piterpti.model;

import java.util.Date;

/**
 * Date to generate reports
 * 
 * @author piter
 *
 */
public class DateFromTo {

	private Date fromDate;
	private Date toDate;

	public DateFromTo(Date fromDate, Date toDate) {
		this.fromDate = fromDate;
		this.toDate = toDate;
	}

	public DateFromTo() {
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

}
