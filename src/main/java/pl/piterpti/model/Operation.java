package pl.piterpti.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Operation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(name = "income_id")
	private long id;

	@Column(name = "value", precision = 10, scale = 2)
	private BigDecimal value;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "date")
	private Date date;

	@Column(name = "short_desc")
	private String shortDesc;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	private Category category;
	
	public Operation() {
		
	}

	public Operation(BigDecimal value, Date date, String shortDesc) {
		this.value = value;
		this.date = date;
		this.shortDesc = shortDesc;
	}

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

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}	
	
	public abstract String getOperationType();
}
