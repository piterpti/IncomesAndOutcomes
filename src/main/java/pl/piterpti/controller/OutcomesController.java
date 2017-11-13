package pl.piterpti.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

import pl.piterpti.constants.Constants;
import pl.piterpti.model.Category;
import pl.piterpti.model.DateFromTo;
import pl.piterpti.model.Operation;
import pl.piterpti.model.OperationsPerDay;
import pl.piterpti.model.Outcome;
import pl.piterpti.model.User;
import pl.piterpti.service.CategoryService;
import pl.piterpti.service.OutcomeService;
import pl.piterpti.service.UserService;
import pl.piterpti.toolkit.OperationToolkit;
import pl.piterpti.toolkit.Toolkit;

@Controller
public class OutcomesController {

	private static final String VIEW_ADD_OUTCOME = "outcomes/addOutcome";
	private static final String VIEW_USER_OUTCOMES = "outcomes/userOutcomes";
	private static final String VIEW_DATE_OUTCOMES = "outcomes/outcomesDateReport";
	private static final String VIEW_EDIT_OUTCOME = "outcomes/editOutcome";
	
	/**
	 * Maximum number to display on user outcomes page
	 */
	private static final int MAX_OUTCOMES_TO_DISPLAY = 25;

	private Logger logger = Logger.getLogger(OutcomesController.class);

	@Autowired
	private OutcomeService outcomeService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private CategoryService categoryService;

	@RequestMapping(value = "/outcomes/addOutcome", method = RequestMethod.GET)
	public ModelAndView addOutcome() {
		ModelAndView modelAndView = new ModelAndView();

		Outcome outcome = new Outcome();

		outcome.setDate(new Date());
		outcome.setCategory(new Category());
		
		List<Category> categorires = categoryService.findUserActiveCategories(Toolkit.getLoggerUserName());
		
		modelAndView.addObject("outcome", outcome);
		modelAndView.addObject("categories", categorires);

		modelAndView.setViewName(VIEW_ADD_OUTCOME);

		return modelAndView;
	}

	@RequestMapping(value = "/outcomes/addOutcome", method = RequestMethod.POST)
	public ModelAndView addOutcome(@Valid Outcome outcome, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		
		// validate outcome
		String errorMsg = OperationToolkit.validateOperation(outcome);
		
		if (errorMsg != null) {
			modelAndView = addOutcome();
			modelAndView.addObject("errorMessage", errorMsg);
			modelAndView.addObject("outcome", outcome);
			return modelAndView;
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();

		User user = userService.findByLogin(userName);
		if (user == null) {

			modelAndView.setViewName(VIEW_ADD_OUTCOME);

		} else {

			if (outcome != null && outcome.getCategory() != null) {
				
				Category category = categoryService.findUserCategoryByName(Toolkit.getLoggerUserName(),
						outcome.getCategory().getName());
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
	public ModelAndView userOutcomes(@Param("page") Integer page) {

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(VIEW_USER_OUTCOMES);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();

		if (page == null) {
			page = new Integer(1);
		}
		
		long count = outcomeService.userOutcomesCount(userName);
		
		Pageable topResults = new PageRequest(page.intValue() - 1, MAX_OUTCOMES_TO_DISPLAY);

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
		
		int pagesCount = (int)(Math.ceil((double)count / MAX_OUTCOMES_TO_DISPLAY));
		
		List<Integer> pages = new ArrayList<>();
		if (pagesCount < 6) {
			for (int i = 1; i <= pagesCount; i++) {
				pages.add(i);
			}
			
			modelAndView.addObject("pages", pages);
		} else {
			modelAndView.addObject("lastPage", pagesCount);
			int startIdx = page.intValue() -1;
			if (startIdx < 2) {
				startIdx = 2;
			}
			if (startIdx >= pagesCount - 3) {
				startIdx = (int) (pagesCount - 3);
			}
			
			for (int i = startIdx; i < startIdx + 3; i++) {
				pages.add(i);
				if (i + 1 >= pagesCount) {
					break;
				}
			}
			modelAndView.addObject("selectedPages", pages);
			
			if (pages.get(0) > 2) {
				modelAndView.addObject("prefix", "...");
			}
			
			if (pages.size() == 3 && pages.get(pages.size() - 1) + 1 < pagesCount) {
				modelAndView.addObject("suffix", "...");
			}
			
		}
		
		modelAndView.addObject("currentPage", page.intValue());
		
		return modelAndView;
	}

	@RequestMapping(value = "outcomes/deleteOutcome")
	public ModelAndView deleteOutcome(@Param("id") long id, @Param("date") String date) {
		ModelAndView modelAndView = new ModelAndView();
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();

		User user = userService.findByLogin(userName);

		
		if (id < 1) {
			return ErrorController.getErrorMav("Wrong operation to delete");
		}
		
		if (!user.hasOperation(id)) {
			return ErrorController.getErrorMav("User " + userName + " cannot delete selected outcome.");
		}
		
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

		modelAndView.addObject("datePeriod", Toolkit.getDatePeriodToForm());
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();
		
		long count = outcomeService.userOutcomesCount(userName);
		
		if (count < 1) {
			modelAndView.addObject("errorMessage", "There is not any outcomes. Try add some");
		}

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

		Date toDate = new Date(dft.getToDate().getTime() + TimeUnit.DAYS.toMillis(1));
		
		List<Outcome> outcomes = userService.findUserOutcomesInDate(user.getId(), dft.getFromDate(), toDate);

		if (!outcomes.isEmpty()) {

			List<OperationsPerDay> opds = new ArrayList<>();
			List<Operation> outcomeList = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			BigDecimal summary = new BigDecimal("0");
			
			for (Outcome outcome : outcomes) {

				summary = summary.add(outcome.getValue());
				
				if (outcomeList.isEmpty()) {

					outcomeList.add(outcome);

				} else {

					Date firstDate = outcome.getDate();
					Date secondDate = outcomeList.get(outcomeList.size() - 1).getDate();
					
					if (sdf.format(firstDate).equals(sdf.format(secondDate))) {
						
						outcomeList.add(outcome);
						
					} else {
						
						OperationsPerDay opd = new OperationsPerDay();
						opd.setDate(outcomeList.get(0).getDate());
						opd.setOperations(outcomeList);
						
						opds.add(opd);
						
						outcomeList = new ArrayList<>();
						outcomeList.add(outcome);
					}

				}

			}
			
			if (!outcomeList.isEmpty()) {
				OperationsPerDay opd = new OperationsPerDay();
				opd.setDate(outcomeList.get(0).getDate());
				opd.setOperations(outcomeList);
				
				opds.add(opd);
				
			}

			modelAndView.addObject("opds", opds);
			
			String summaryStr = summary.toString() + " " + Constants.CURRENCY;
			
			modelAndView.addObject("summary", summaryStr);
		}

		modelAndView.addObject("datePeriod", dft);
		
		

		return modelAndView;
	}
	
	@RequestMapping(value = "/outcomes/editOutcome", method = RequestMethod.GET)
	public ModelAndView editOutcome(@Param("id") long id) {
		ModelAndView modelAndView = new ModelAndView();
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();

		User user = userService.findByLogin(userName);
		
		if (id < 1) {
			return ErrorController.getErrorMav("Wrong outcome id");
		}
		
		Outcome outcome = outcomeService.findById(id);
		
		if (outcome == null) {
			return ErrorController.getErrorMav("Outcome with id " + id + " not found");
		}
		
		boolean userOutcome = false;
		for (Outcome o : user.getOutcomes()) {
			if (o.getId() == outcome.getId()) {
				userOutcome = true;
			}
		}
		
		if (!userOutcome) {
			return ErrorController.getErrorMav("You do not have access to edit note with id " + id);
		}
		
		modelAndView.addObject("outcome", outcome);
		
		List<Category> categorires = categoryService.findUserActiveCategories(Toolkit.getLoggerUserName());	
		modelAndView.addObject("categories", categorires);
		
		modelAndView.setViewName(VIEW_EDIT_OUTCOME);
		return modelAndView;
	}
	
	
	@RequestMapping(value = "/outcomes/editOutcome", method = RequestMethod.POST)
	public ModelAndView saveEditedOutcome(@Param("outcome") @Valid Outcome outcome) {
		ModelAndView modelAndView = new ModelAndView();
		
		// validate outcome
		String errorMsg = OperationToolkit.validateOperation(outcome);
		
		if (errorMsg != null) {
			modelAndView = editOutcome(outcome.getId());
			modelAndView.addObject("errorMessage", errorMsg);
			modelAndView.addObject("outcome", outcome);
			return modelAndView;
		}
		
		if (outcome.getCategory() != null) {
			Category category = categoryService.findUserCategoryByName(Toolkit.getLoggerUserName(),
					outcome.getCategory().getName());
			
			if (category != null) {
				outcome.setCategory(category);
			}
		}
		
		if (outcome != null) {	
			
			outcomeService.saveOutcome(outcome);
		}
		
		modelAndView.setViewName("redirect:/" + VIEW_USER_OUTCOMES);
		
		return modelAndView;
	}
}
