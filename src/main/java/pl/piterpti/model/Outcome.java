package pl.piterpti.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name= "outcome")
public class Outcome {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "outcome_id")
	private long id;
	
	@Column(name = "value")
	private BigDecimal value;
	
	@Column(name = "date")
	private Date date;
	
	@Column(name = "short_desc")
	private String shortDesc;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}	
}
