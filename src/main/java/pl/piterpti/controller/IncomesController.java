package pl.piterpti.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import pl.piterpti.model.Income;
import pl.piterpti.service.IncomeService;

@Controller
public class IncomesController {
	
	public static final String VIEW_USER_INCOMES = "incomes/userIncomes";
	
	private static final int MAX_INCOMES_TO_DISPLAY = 10;
	
	private Logger logger = Logger.getLogger(IncomesController.class);
	
	@Autowired
	private IncomeService incomeService;
	
	
	@RequestMapping(value = "/incomes/userIncomes", method = RequestMethod.GET)
	public ModelAndView userIncomes() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(VIEW_USER_INCOMES);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();

		Pageable topResults = new PageRequest(0, MAX_INCOMES_TO_DISPLAY);

		List<Income> incomes = incomeService.findUserIncomesWithLimit(userName, topResults);
		
		if (incomes.isEmpty()) {
			// show msg that there is no outcomes
			modelAndView.addObject("message", "There is not any outcomes for user " + userName);

		} else {

			// ok

			List<Income> tmpIncomes = incomes;
			if (incomes.size() > MAX_INCOMES_TO_DISPLAY) {
				tmpIncomes = new ArrayList<>();

				int i = 0;
				int idx = incomes.size() - 1;
				while (i++ < MAX_INCOMES_TO_DISPLAY) {
					tmpIncomes.add(incomes.get(idx--));
				}
			}

			modelAndView.addObject("incomes", tmpIncomes);

		}

		return modelAndView;
	}

}
