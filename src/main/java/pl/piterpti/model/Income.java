package pl.piterpti.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "income")
public class Income extends Operation {

	public Income() {}
	
	public Income(BigDecimal value, Date date, String shortDesc) {
		super(value, date, shortDesc);
	}
	
	@Override
	public String getOperationType() {
		return "Income";
	}
	
}
