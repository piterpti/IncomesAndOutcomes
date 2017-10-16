package pl.piterpti.dao;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import pl.piterpti.model.Outcome;
import pl.piterpti.service.OutcomeService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OutcomeDAOTest {

	@Autowired
	private OutcomeService outcomeService;
	
	@Test
	@Transactional 
	public void testGetAllOutcomes() {
		
		List<Outcome> outcomes = outcomeService.findAll();
		
		int outcomeSize = outcomes.size();
		outcomeService.saveOutcome(new Outcome(new BigDecimal(200), new Date(), "ABC"));
		outcomes = outcomeService.findAll();
		
		assertEquals(outcomeSize + 1, outcomes.size());
	}
	
	@Test
	@Transactional
	public void testOutcomesOperations() {
		
		outcomeService.deleteAll();
		
		Outcome outcome = new Outcome(new BigDecimal(200), new Date(), "ABC");
		outcomeService.saveOutcome(outcome);
		List<Outcome> outcomes = outcomeService.findAll();
		
		assertEquals(1, outcomes.size());
		assertEquals("ABC", outcomes.get(0).getShortDesc());
	}
	
}
