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
@Table(name = "income")
public class Income {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "income_id")
	private long id;

	@Column(name = "value", precision = 10, scale = 2)
	private BigDecimal value;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "incomeDate")
	private Date incomeDate;

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

	public Date getOutcomeDate() {
		return incomeDate;
	}

	public void setOutcomeDate(Date outcomeDate) {
		this.incomeDate = outcomeDate;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	@Override
	public String toString() {
		return "Income [id=" + id + ", value=" + value + ", outcomeDate=" + incomeDate + ", shortDesc=" + shortDesc
				+ "]";
	}
}
