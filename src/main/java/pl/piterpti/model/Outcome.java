package pl.piterpti.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "outcome")
public class Outcome extends Operation {

	public Outcome() {}
	
	public Outcome(BigDecimal value, Date date, String shortDesc) {
		super(value, date, shortDesc);
	}
	
	
	@Override
	public String getOperationType() {
		return "Outcome";
	}

}
