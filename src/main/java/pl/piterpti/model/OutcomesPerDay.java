package pl.piterpti.model;

import java.util.Date;
import java.util.List;

public class OutcomesPerDay {

	private Date date;

	private List<Operation> operations;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<Operation> getOperations() {
		return operations;
	}

	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}	
}
