package pl.piterpti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import pl.piterpti.service.IncomeService;
import pl.piterpti.service.OutcomeService;
import pl.piterpti.toolkit.Toolkit;

@Controller
public class HomeController {

	public static final String VIEW_HOME = "index";
	
	public static final String ACTIVE_HOME = "activeHome";
	
	@Autowired
	private OutcomeService outcomeService;
	
	@Autowired
	private IncomeService incomeService;
	
	
	@RequestMapping(value = {"/home", "/", "/index"}, method = RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName(VIEW_HOME);
		mav.addObject("datePeriod", Toolkit.getDatePeriodToForm());
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
			String userName = auth.getName();

			long count = incomeService.userIncomesCount(userName);
			count += outcomeService.userOutcomesCount(userName);
			
			if (count < 1) {
				mav.addObject("errorMessage", "There is not any incomes/outcomes. Go to Incomes/Outcomes to add some.");
			}
			
		}
		
		mav.addObject(ACTIVE_HOME, "active");
		
		return mav;
	}
	
	
}
