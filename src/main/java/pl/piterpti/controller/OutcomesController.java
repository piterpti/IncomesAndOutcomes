package pl.piterpti.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import pl.piterpti.model.Category;
import pl.piterpti.model.DateFromTo;
import pl.piterpti.model.Outcome;
import pl.piterpti.model.OutcomesPerDay;
import pl.piterpti.model.User;
import pl.piterpti.service.CategoryService;
import pl.piterpti.service.OutcomeService;
import pl.piterpti.service.UserService;

@Controller
public class OutcomesController {

	private static final String VIEW_ADD_OUTCOME = "outcomes/addOutcome";
	private static final String VIEW_USER_OUTCOMES = "outcomes/userOutcomes";
	private static final String VIEW_DATE_OUTCOMES = "outcomes/outcomesDateReport";
	private static final String VIEW_EDIT_OUTCOME = "outcomes/editOutcome";
	
	@Value("${pl.piterpti.currency}")
	private String currency;
	
	/**
	 * Maximum number to display on user outcomes page
	 */
	private static final int MAX_OUTCOMES_TO_DISPLAY = 10;

	private Logger logger = Logger.getLogger(OutcomesController.class);

	@Autowired
	private OutcomeService outcomeService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private CategoryService categoryService;

	@RequestMapping(value = "/addOutcome", method = RequestMethod.GET)
	public ModelAndView addOutcomePage() {
		ModelAndView modelAndView = new ModelAndView();

		Outcome outcome = new Outcome();

		outcome.setOutcomeDate(new Date());
		outcome.setCategory(new Category());
		
		List<Category> categorires = categoryService.findAll();
		
		modelAndView.addObject("outcome", outcome);
		modelAndView.addObject("categories", categorires);

		modelAndView.setViewName(VIEW_ADD_OUTCOME);

		return modelAndView;
	}

	@RequestMapping(value = "/addOutcome", method = RequestMethod.POST)
	public ModelAndView addOutcome(@Valid Outcome outcome, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();

		User user = userService.findByLogin(userName);
		if (user == null) {

			modelAndView.setViewName(VIEW_ADD_OUTCOME);

		} else {

			if (outcome != null && outcome.getCategory() != null) {
				
				Category category = categoryService.findByName(outcome.getCategory().getName());
				if (category != null) {
					outcome.setCategory(category);
				}
			}
			
			// saving outcome to DB
			List<Outcome> outcomes = user.getOutcomes() != null ? user.getOutcomes() : new ArrayList<>();
			outcomes.add(outcome);

			user.setOutcomes(outcomes);

			outcomeService.saveOutcome(outcome);

			userService.updateUser(user);

			modelAndView.addObject("outcomes", outcomes);

			modelAndView.setViewName("redirect:/" + VIEW_USER_OUTCOMES);

		}

		return modelAndView;
	}

	@RequestMapping(value = "/outcomes/userOutcomes", method = RequestMethod.GET)
	public ModelAndView userOutcomes() {

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(VIEW_USER_OUTCOMES);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();

		Pageable topResults = new PageRequest(0, MAX_OUTCOMES_TO_DISPLAY);

		List<Outcome> outcomes = outcomeService.findUserOutcomesWithLimit(userName, topResults);
		
		if (outcomes.isEmpty()) {
			// show msg that there is no outcomes
			modelAndView.addObject("message", "There is not any outcomes for user " + userName);

		} else {
			// ok
			
			List<Outcome> tmpOutcomes = outcomes;
			if (outcomes.size() > MAX_OUTCOMES_TO_DISPLAY) {
				tmpOutcomes = new ArrayList<>();
				
				int i = 0;
				int idx = outcomes.size() - 1;
				while (i++ < MAX_OUTCOMES_TO_DISPLAY) {
					tmpOutcomes.add(outcomes.get(idx--));
				}
			}
			
			modelAndView.addObject("outcomes", tmpOutcomes);

		}

		return modelAndView;
	}

	@RequestMapping(value = "outcomes/deleteOutcome")
	public ModelAndView deleteOutcome(@Param("id") long id, @Param("date") String date) {
		ModelAndView modelAndView = new ModelAndView();
		
		if (date != null) {
			modelAndView.setViewName("redirect:/" + VIEW_DATE_OUTCOMES);
		} else {
			modelAndView.setViewName("redirect:/" + VIEW_USER_OUTCOMES);
		}		

		outcomeService.deleteOutcome(id);

		return modelAndView;
	}

	@RequestMapping(value = "outcomes/outcomesDateReport", method = RequestMethod.GET)
	public ModelAndView prepareOutcomeDateReport() {
		ModelAndView modelAndView = new ModelAndView();

		modelAndView.setViewName(VIEW_DATE_OUTCOMES);

		modelAndView.addObject("datePeriod", getDatePeriodToForm());

		return modelAndView;
	}

	@RequestMapping(value = "outcomes/outcomesDateReport", method = RequestMethod.POST)
	public ModelAndView showOutcomeDateReport(DateFromTo dft, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();

		modelAndView.setViewName(VIEW_DATE_OUTCOMES);

		if (bindingResult.hasErrors()) {
			logger.warn("Binding result has errors when generating outcomes report");
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();

		User user = userService.findByLogin(userName);

		List<Outcome> outcomes = userService.findUserOutcomesInDate(user.getId(), dft.getFromDate(), dft.getToDate());

		if (!outcomes.isEmpty()) {

			List<OutcomesPerDay> opds = new ArrayList<>();
			List<Outcome> outcomeList = new ArrayList<>();
			BigDecimal summary = new BigDecimal("0");
			
			for (Outcome outcome : outcomes) {

				summary = summary.add(outcome.getValue());
				
				if (outcomeList.isEmpty()) {

					outcomeList.add(outcome);

				} else {

					if (outcome.getOutcomeDate().getTime() == outcomeList.get(outcomeList.size() - 1).getOutcomeDate()
							.getTime()) {
						
						outcomeList.add(outcome);						
						
					} else {
						
						OutcomesPerDay opd = new OutcomesPerDay();
						opd.setOutcomeDate(outcomeList.get(0).getOutcomeDate());
						opd.setOutcomes(outcomeList);
						
						opds.add(opd);
						
						outcomeList = new ArrayList<>();
						outcomeList.add(outcome);
					}

				}

			}
			
			if (!outcomeList.isEmpty()) {
				OutcomesPerDay opd = new OutcomesPerDay();
				opd.setOutcomeDate(outcomeList.get(0).getOutcomeDate());
				opd.setOutcomes(outcomeList);
				
				opds.add(opd);
				
			}

			modelAndView.addObject("opds", opds);
			
			String summaryStr = summary.toString() + " " + currency;
			
			modelAndView.addObject("summary", summaryStr);
		}

		modelAndView.addObject("datePeriod", dft);

		return modelAndView;
	}
	
	@RequestMapping(value = "outcomes/editOutcome", method = RequestMethod.GET)
	public ModelAndView editOutcome(@Param("id") long id) {
		ModelAndView modelAndView = new ModelAndView();
		
		if (id < 1) {
			// TODO display error about wrong outcome
		}
		
		Outcome outcome = outcomeService.findById(id);
		
		if (outcome == null) {
			// TODO display error about wrong outcome
		}
		
		modelAndView.addObject("outcome", outcome);
		
		modelAndView.setViewName(VIEW_EDIT_OUTCOME);
		return modelAndView;
	}

	private DateFromTo getDatePeriodToForm() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return new DateFromTo(cal.getTime(), new Date());
	}
}
