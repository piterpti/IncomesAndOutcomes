package pl.piterpti.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OutcomesPerDay {

	private Date outcomeDate;

	private List<Outcome> outcomes;

	public Date getOutcomeDate() {
		return outcomeDate;
	}

	public void setOutcomeDate(Date outcomeDate) {
		this.outcomeDate = outcomeDate;
	}

	public List<Outcome> getOutcomes() {
		return outcomes;
	}

	public void setOutcomes(List<Outcome> outcomes) {
		this.outcomes = outcomes;
	}

}
