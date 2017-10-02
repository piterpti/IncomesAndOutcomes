package pl.piterpti.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "outcome")
public class Outcome {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "outcome_id")
	private long id;

	@Column(name = "value")
	private BigDecimal value;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "outcomeDate")
	private Date outcomeDate;

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

	public String getShortDesc() {
		return shortDesc;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	public Date getOutcomeDate() {
		return outcomeDate;
	}

	public void setOutcomeDate(Date outcomeDate) {
		this.outcomeDate = outcomeDate;
	}

}
