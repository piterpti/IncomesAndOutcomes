package pl.piterpti.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import pl.piterpti.model.Outcome;
import pl.piterpti.model.User;
import pl.piterpti.service.OutcomeService;
import pl.piterpti.service.UserService;

@Controller
public class OutcomesController {
	
	private static final String VIEW_ADD_OUTCOME = "outcomes/addOutcome";
	private static final String VIEW_USER_OUTCOMES = "outcomes/userOutcomes";
	
	@Autowired
	private OutcomeService outcomeService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/addOutcome", method = RequestMethod.GET)
	public ModelAndView addOutcomePage() {
		ModelAndView modelAndView = new ModelAndView();
		
		Outcome outcome = new Outcome();
		
		modelAndView.addObject("outcome", outcome);
		
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
		
			//saving outcome to DB
			List<Outcome> outcomes = user.getOutcomes() != null ? user.getOutcomes() : new ArrayList<>();
			outcomes.add(outcome);
			
			user.setOutcomes(outcomes);
			
			outcomeService.saveOutcome(outcome);
			
			userService.updateUser(user);
			
			modelAndView.addObject("outcomes", outcomes);
			
			modelAndView.setViewName(VIEW_USER_OUTCOMES);
		
		}
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/outcomes/userOutcomes", method = RequestMethod.GET)
	public ModelAndView userOutcomes() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(VIEW_USER_OUTCOMES);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();
		
		User user = userService.findByLogin(userName);
		
		modelAndView.addObject("outcomes", user.getOutcomes());
		
		return modelAndView;
	}	
}
